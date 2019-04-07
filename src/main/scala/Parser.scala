package main.scala

import javax.script.ScriptEngineManager
import main.scala.destination.{CSVDestination, Destination, SQLiteDestination}
import main.scala.source.SQLiteSource
import org.apache.kafka.streams.kstream.Predicate

import scala.collection.mutable
import scala.io.{BufferedSource, Source}

class Parser {

  val fileName = "config.txt"
  val configFile: BufferedSource = Source.fromFile("config.txt")

  def getTopics(line: String): List[String] = {
    val squarePattern = "\\[(.*?)\\]".r
    squarePattern.findFirstIn(line)
      .getOrElse(throw new RuntimeException).toString
      .replace("[", "")
      .replace("]", "")
      .split(",")
      .map(_.trim)
      .toList
  }

  def predParser(text: String): Predicate[Int, Map[String, Any]] = {
    (key, map) => {
      val fact = new ScriptEngineManager()
      val engine = fact.getEngineByName("JavaScript")

      val pattern = "\\$(\\w+)".r
      val vars = pattern.findAllIn(text)

      vars.foreach(v => {
        engine.put(v, map.apply(v.substring(1)))
      })

      val res = engine.eval(text).toString
      java.lang.Boolean.valueOf(res)
    }
  }

  def mapParser(text: String): Function[(Int, Map[String, Any]), (Int, Map[String, Any])] = {
    return (tuple: (Int, Map[String, Any])) => {
      try {
        val key: Int = tuple._1
        val map: Map[String, Any] = tuple._2
        val fact = new ScriptEngineManager()

        val exprs = text.split(",")
        val assignments = exprs.map(e => e.split("="))
          .map(assignment => {
            if (assignment.length != 2) {
              throw new RuntimeException;
            }
            else (assignment.head, assignment.tail.head);
          })

        val pattern = "\\$(\\w+)".r

        var endMap = map.filter(_ => true)
        assignments.foreach(assignment => {
          val engine = fact.getEngineByName("JavaScript")

          val vars = pattern.findAllIn(assignment._2)
          vars.foreach(v => { engine.put(v, endMap.apply(v.trim.substring(1)))})

          val res = engine.eval(assignment._2)
          endMap += (assignment._1.trim.substring(1) -> res)
        })

        (key, endMap);
      } catch {
        case e: Exception => throw new RuntimeException("Map function was applied on bad parameters")
      }
    }
  }

  def createSourceObject(): SQLiteSource = {
    var producerSource :String = null
    var sourceCols :List[String] = null
    var sourceQuery :String = null
    for (line <- Source.fromFile(fileName).getLines) {
      if(!line.contains("//")) {
        if (line.contains("source:")) {
          producerSource = line.substring(line.lastIndexOf(":") + 1).trim
        }
        else if (line.contains("sourceCols:")) {
          sourceCols = getTopics(line)
          val lineAsList = line.split("\\s+")
          val tableName = lineAsList(1);
          val colsWithComma = sourceCols.reduce(_ + "," + _)
          sourceQuery = "select " + colsWithComma + " from " + tableName + ";"
        }
      }
    }
    if (producerSource != null && sourceCols != null && sourceQuery != null)
      new SQLiteSource(producerSource, sourceCols, sourceQuery)
    else throw new RuntimeException("A valid source is not given.")
  }

  def createConfigMap(): Map[String, Any] = {
    var configMap = mutable.Map[String, Any]()

    var dest :Destination[Int, Map[String, Any]] = null;
    for (line <- configFile.getLines) {
      if (!line.contains("//")) {
        val lineAsList = line.split("\\s+")
        if (line.contains("producer:")) {
          configMap("producer:" + lineAsList(1)) = (lineAsList(1), createSourceObject(), getTopics(line))
        } else if (line.contains("topics:")) {
          configMap("topics") = getTopics(line)
        } else if (line.contains("filter:")) {
          // Get the predicate to be passed to the filter
          val pred = line.substring(line.lastIndexOf(":") + 1)
          // Get string version of predicate as lambda function
          val predLambda = predParser(pred)
          // create args tuple
          configMap("filter:" + lineAsList(1)) = (lineAsList(1), lineAsList(2), getTopics(line), predLambda)
        } else if (line.contains("map:")) {
          // Get the predicate to be passed to the filter
          val func = line.substring(line.lastIndexOf(":") + 1)
          // Get string version of func as lambda function
          val lam = mapParser(func)
          // create args tuple
          configMap("map:" + lineAsList(1)) = (lineAsList(1), lineAsList(2), getTopics(line), lam)
        } else if (line.contains("destination:")) {
          if (line.contains("csv")) {
            dest = new CSVDestination(line.substring(line.lastIndexOf(":") + 1).trim)
          } else {
            dest = new SQLiteDestination(lineAsList(1), lineAsList(2))
          }
        } else if (line.contains("consumer:")) {
          if (dest != null) {
            configMap("consumer:" + lineAsList(1)) = (lineAsList(1), lineAsList(2), lineAsList(3), dest)
          } else {
            throw new NullPointerException("Got to consumer before setting destination.")
          }
        }
      }
    }
    (Map() ++ configMap)
  }

}
