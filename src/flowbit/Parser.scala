package flowbit
import scala.collection.mutable
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

  def getFilterArgs(): mutable.HashMap[String, Array[String]] = {
    val filterMap = new mutable.HashMap[String, Array[String]]()
    var filterCounter: Int = 0
    for (line <- configFile.getLines) {
      if (line.contains("filter")) {
        val lineAsList = line.split("\\s+")
        val pred = line.substring(line.lastIndexOf(":")+1)
        val args: Array[String] = lineAsList.slice(2, 4) :+ pred
        filterMap.put(lineAsList(1), args)
        filterCounter += 1
      }
    }
    filterMap
  }

  def getMapArgs(): mutable.HashMap[String, List[String]] = {
    val mapMap = new mutable.HashMap[String, List[String]]()
    var mapCounter: Int = 0
    for (line <- configFile.getLines) {
      if (line.contains("map")) {
        val lineAsList = line.split("\\s+")
        mapMap.put(lineAsList.head.concat(mapCounter.toString), lineAsList.tail.toList)
        mapCounter += 1
      }
    }
    mapMap
  }


}
