import flowbit.FlowBitImpl
import flowbit.destination.{MapDestination, SQLiteDestination}
import flowbit.source.{MapSource, SQLiteSource, TSource}
import org.apache.kafka.streams.KeyValue

import scala.collection.immutable.HashMap


object Main {


  def main(args: Array[String]): Unit = {

    /*
    val flowbit = new FlowBitImpl("localhost:9092")

    println("there should be no topics in the flowbit")
    flowbit.getTopics()

    println("adding topics")
    flowbit.addTopics(List("toBeFiltered1", "toBeMapped1", "done1"), 1, 1)

    println("there should be 3 topics")
    flowbit.getTopics()

    val source = new SQLiteSource("/usr/local/Cellar/sqlite/3.27.1/bin/chinook.db", List("GenreId", "Name"), "SELECT * FROM genres")
    println("adding producer")
    flowbit.addProducer[Int, List[String]]("producer1", source, List("toBeFiltered1"))

    println("adding filter")
    flowbit.addFilter[Int, List[String]]("filter1", "toBeFiltered1", List("toBeMapped1"),
      (k, v) => k % 2 == 0)

    println("adding map")
    flowbit.addMap[Int, List[String], Int, List[String]]("map1", "toBeMapped1", List("done1"),
      (k,v) => new KeyValue(k, v.tail))

    val dest = new SQLiteDestination
    println("adding consumer")
    flowbit.addConsumer[Int, List[String]]("consumer1", "done1", "group1", dest)
    */

    val twitter = new TSource
    twitter.poll

  }


}

