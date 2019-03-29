import flowbit.FlowBitImpl
import flowbit.destination.MapDestination
import flowbit.source.MapSource
import org.apache.kafka.streams.KeyValue

import scala.collection.immutable.HashMap


object Main {


  def main(args: Array[String]): Unit = {

    val flowbit = new FlowBitImpl("localhost:9092")

    println("there should be no topics in the flowbit")
    flowbit.getTopics()

    println("adding topics")
    flowbit.addTopics(List("toBeFiltered1", "toBeMapped1", "done1"), 1, 1)

    println("there should be 3 topics")
    flowbit.getTopics()

    val source = new MapSource
    println("adding producer")
    flowbit.addProducer[Int, HashMap[String, Int]]("producer1", source, List("toBeFiltered1"))

    println("adding filter")
    flowbit.addFilter[Int, HashMap[String, Int]]("filter1", "toBeFiltered1", List("toBeMapped1"),
      (k, v) => k % 2 == 0)

    println("adding map")
    flowbit.addMap[Int, HashMap[String, Int], String, HashMap[String, Int]]("map1", "toBeMapped1", List("done1"),
      (k,v) => new KeyValue(k.toString, v))

    val dest = new MapDestination
    println("adding consumer")
    flowbit.addConsumer[String, HashMap[String, Int]]("consumer1", "done1", "group1", dest)
  }


}

