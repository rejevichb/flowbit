package flowbit
import org.apache.kafka.streams.kstream.{Predicate}
import config.Config

import net.objecthunter.exp4j.{Expression, ExpressionBuilder}

import scala.collection.mutable
import scala.collection.parallel.immutable
import scala.io.{BufferedSource, Source}

class Parser {

  val configFile: BufferedSource = Source.fromFile("config.scala")

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

//  def getFilterArgs[A, B](): scala.collection.immutable.Map[String,(String, List[String], Predicate[A, B])]= {
//    var argsSeq = Map[String, (String, List[String], Predicate[A, B])]
//
//    for (line <- configFile.getLines) {
//      if (line.contains("filter")) {
//        // Get the line as a list of string separated by comma
//        val lineAsList = line.split("\\s+")
//        // Get the predicate to be passed to the filter
//        val pred = line.substring(line.lastIndexOf(":")+1)
//        // Get string version of predicate as lambda function
//        val predLambda: ExpressionBuilder = new ExpressionBuilder(pred).variables("k", "v")
//        // create args tuple
//        argsSeq(lineAsList(1)) = (lineAsList(2), List(lineAsList(3)), new Config somePred)
//      }
//    }
//    argsSeq.
//  }

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
