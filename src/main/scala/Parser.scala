package main.scala

import javax.script.ScriptEngineManager
import org.apache.kafka.streams.kstream.Predicate

import scala.io.{BufferedSource, Source}

class Parser {

  val configFile: BufferedSource = Source.fromFile("config.txt")

  def predParser(text: String): Predicate[Int, Map[String, AnyVal]] = {
    return (key, map) => {
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

  def createSourceObject(): SQLiteSource = {
    for (line <- configFile.getLines) {
      if(!line.contains("//") {
        if (line.contains("$source") {
          val producerSource = line.substring(line.lastIndexOf("=") + 1)
        }
        else if (line.contains("$sourceCols") {
          val lineAsList =  line.split("\\s+")
          val sourceCols = List(lineAsList(1), lineAsList(2), lineAsList(3))
        }
        else if (line.contains("$sourceQuery") {
          val sourceQuery = line.substring(line.lastIndexOf("=") + 1)
        }
      }
    }
    val sourceObject = new SQLiteSource(producerSource, sourceCols, sourceQuery)
  }

  def createConfigMap(): scala.collection.mutable.LinkedHashMap[String, AnyRef] = {
    var configMap = scala.collection.mutable.LinkedHashMap[String, AnyRef]()

    for (line <- configFile.getLines) {
      if (!line.contains("//")) {
        val lineAsList = line.split("\\s+")
        
        if (line.contains("producer") {
          configMap("producer") = (lineAsList(1), createConfigMap(), lineAsList(2))
        }
        else if (line.contains("topic")) {
          configMap("topics") = lineAsList.tail.toList
        }
        else if (line.contains("filter")) {
          // Get the predicate to be passed to the filter
          val pred = line.substring(line.lastIndexOf(":") + 1)
          // Get string version of predicate as lambda function
          val predLambda = predParser(pred)
          // create args tuple
          configMap(lineAsList(1)) = (lineAsList(2), lineAsList(3), predLambda)
        }
        else if (line.contains("map")) {
          // Get the predicate to be passed to the filter
          val pred = line.substring(line.lastIndexOf(":") + 1)
          // Get string version of predicate as lambda function
          val predLambda = predParser(pred)
          // create args tuple
          configMap(lineAsList(1)) = (lineAsList(2), lineAsList(3), predLambda)
        }
        else if (line.contains("$destination") {
          val dest = new CSVDestination(line.substring(line.lastIndexOf("=") + 1))
        }
        else if (line.contains("consumer")) {
          configMap(lineAsList(1)) = (lineAsList(2), lineAsList(3), dest)
        }
      }
    }
    configMap
  }

}
