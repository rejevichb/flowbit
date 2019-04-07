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

  def createConfigMap(): scala.collection.mutable.LinkedHashMap[String, AnyRef] = {
    var configMap = scala.collection.mutable.LinkedHashMap[String, AnyRef]()

    for (line <- configFile.getLines) {
      if (!line.contains("//")) {
        if (line.contains("topic")) {
          val lineAsList = line.split("\\s+")
          configMap("topics") = lineAsList.tail.toList
        }
        else if (line.contains("filter")) {
          // Get the line as a list of string separated by comma
          val lineAsList = line.split("\\s+")
          // Get the predicate to be passed to the filter
          val pred = line.substring(line.lastIndexOf(":") + 1)
          // Get string version of predicate as lambda function
          val predLambda = predParser(pred)
          // create args tuple
          configMap(lineAsList(1)) = List(lineAsList(2), lineAsList(3), predLambda)
        }
        else if (line.contains("map")) {
          // Get the line as a list of string separated by comma
          val lineAsList = line.split("\\s+")
          // Get the predicate to be passed to the filter
          val pred = line.substring(line.lastIndexOf(":") + 1)
          // Get string version of predicate as lambda function
          val predLambda = predParser(pred)
          // create args tuple
          configMap(lineAsList(1)) = List(lineAsList(2), lineAsList(3), predLambda)
        }
      }
    }
    configMap
  }

}
