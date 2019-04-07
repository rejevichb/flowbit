
package main.scala

import main.scala.source.{CSVSource, SQLiteSource}
import main.scala.destination.CSVDestination


object Main {


  def main(args: Array[String]): Unit = {

    val flowbit = new FlowBitImpl("localhost:9092")

    println("there should be no topics in the flowbit")
    flowbit.getTopics()

    println("adding topics")
    flowbit.addTopics(List("toBeFiltered1", "toBeMapped1", "done1"), 1, 1)

    println("there should be 3 topics")
    flowbit.getTopics()

    val source = new CSVSource("flights.csv", List("YEAR","MONTH","DAY_OF_MONTH","ORIGIN_AIRPORT_ID","ORIGIN",
                          "DEST_AIRPORT_ID","DEST","DEP_TIME","ARR_TIME","CANCELLED","AIR_TIME","FLIGHTS","DISTANCE"))

    println("adding producer")
    flowbit.addProducer[Int, Map[String, Any]]("producer1", source, List("toBeFiltered1"))

    println("adding filter")
    flowbit.addFilter[Int, Map[String, Any]]("filter1", "toBeFiltered1", List("done1"),
      (k, v) =>  v.getOrElse("ORIGIN", "") == "ORD")

//    println("adding map")
//    flowbit.addMap[Int, Map[String, String], Int, Map[String, String]]("map1", "toBeMapped1", List("done1"),
//      (k,v) => new KeyValue(k, v))

    // val dest = new SQLiteDestination("songs.db", "filtered")
    val dest = new CSVDestination("filteredFlights.csv")
    println("adding consumer")
    flowbit.addConsumer[Int, Map[String, Any]]("consumer1", "done1", "group1", dest)
  }


}
