package main.scala

import main.scala.destination.{CSVDestination, Destination}
import main.scala.source.{SQLiteSource, Source}
import org.apache.kafka.streams.KeyValue
import org.apache.kafka.streams.kstream.Predicate
//import _root_.sun.plugin.dom.exception.InvalidStateException

object MainFromConfig {

  def main(args: Array[String]): Unit = {
    val flowbit = new FlowBitImpl("localhost:9092")

    val parser = new Parser()
    val configMap :Map[String, Any] = parser.createConfigMap()

    configMap.foreach(entry => {
      val (nodeName, nodeTuple) = entry
      if (nodeName.startsWith("topics")) {
        println("adding topics: " + nodeTuple)
        flowbit.addTopics(nodeTuple.asInstanceOf[List[String]], 1, 1)
      } else if (nodeName.startsWith("producer")) {
        val producerTuple = nodeTuple.asInstanceOf[(String, Source[Int, Map[String, Any]], List[String])]
        println("adding producer: " + producerTuple._1 + "\tfrom: " + producerTuple._2 + "\tto: " + producerTuple._3)
        flowbit.addProducer[Int, Map[String, Any]](producerTuple._1, producerTuple._2, producerTuple._3)
      } else if (nodeName.startsWith("filter")) {
        val filterTuple = nodeTuple.asInstanceOf[(String, String, List[String], Predicate[Int, Map[String, Any]])]
        println("adding filter: " + filterTuple._1 + "\tfrom: " + filterTuple._2 + "\tto:" + filterTuple._3)
        flowbit.addFilter[Int, Map[String, Any]](filterTuple._1, filterTuple._2, filterTuple._3, filterTuple._4);
      } else if (nodeName.startsWith("map")) {
        val mapTuple = nodeTuple.asInstanceOf[(String, String, List[String], Function[(Int, Map[String, Any]), (Int, Map[String, Any])])]
        println("adding map: " + mapTuple._1 + "\tfrom: " + mapTuple._2 + "\tto:" + mapTuple._3)
        flowbit.addMap[Int, Map[String, Any], Int, Map[String, Any]](mapTuple._1, mapTuple._2, mapTuple._3, (k, v) => {
          val result = mapTuple._4.apply(k, v)
          new KeyValue[Int, Map[String, Any]](result._1, result._2)
        })
      } else if (nodeName.startsWith("consumer")) {
        val consumerTuple = nodeTuple.asInstanceOf[(String, String, String, Destination[Int, Map[String, Any]])]
        println("adding consumer: " + consumerTuple._1 + "\tfrom: " + consumerTuple._2)
        flowbit.addConsumer[Int, Map[String, Any]](consumerTuple._1, consumerTuple._2, consumerTuple._3, consumerTuple._4)
      } //else throw new InvalidStateException("Unexpected node name doesn't begin with identifier:" + nodeName)
    })

    println("UP AND RUNNING")
  }

}
