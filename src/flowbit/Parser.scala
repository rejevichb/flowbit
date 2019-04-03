package flowbit
import java.util.function.{BiFunction, Predicate}

import net.objecthunter.exp4j.{Expression, ExpressionBuilder}

import scala.collection.mutable
import scala.collection.parallel.immutable
import scala.io.{BufferedSource, Source}

class Parser {

  val configFile: BufferedSource = Source.fromFile("config.txt")

  def getTopics(): Array[String] = {
    var topics = Array[String]()
    for (line <- configFile.getLines) {
      if (line.contains("topic")) {
        val lineAsList = line.split("\\s+")
        topics = lineAsList.tail
      }
    }
    topics
  }

  def getFilterArgs[A, B](): Array[Any] = {
    var filterSeq = Seq[Any]()

    for (line <- configFile.getLines) {
      if (line.contains("filter")) {
        // Get the line as a list of string separated by comma
        val lineAsList = line.split("\\s+")
        // Get the predicate to be passed to the filter
        val pred = line.substring(line.lastIndexOf(":")+1)
        // Get string version of predicate as lambda function
        val predLambda: ExpressionBuilder = new ExpressionBuilder(pred).variables("k", "v")
        // create args tuple
        val args = (lineAsList(2), List(lineAsList(3)), predLambda)
        // Create map to be returned
        filterSeq = filterSeq :+ lineAsList(1)
        filterSeq = filterSeq :+ args
      }
    }
    filterSeq.toArray
  }

  def getMapArgs(): mutable.HashMap[String, Array[String]] = {
    val mapMap = new mutable.HashMap[String, Array[String]]()
    for (line <- configFile.getLines) {
      if (line.contains("map")) {
        val lineAsList = line.split("\\s+")
        val pred = line.substring(line.lastIndexOf(":")+1)
        val args: Array[String] = lineAsList.slice(2, 4) :+ pred
        mapMap.put(lineAsList(1), args)
      }
    }
    mapMap
  }


}
