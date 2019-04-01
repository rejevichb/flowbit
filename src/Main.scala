import flowbit.FlowBitImpl
import flowbit.destination._
import flowbit.source._
import org.apache.kafka.streams.KeyValue

object Main {


  def main(args: Array[String]): Unit = {

    val flowbit = new FlowBitImpl("localhost:9092")

    println("there should be no topics in the flowbit")
    flowbit.getTopics()

    println("adding topics")
    flowbit.addTopics(List("toBeFiltered1", "toBeMapped1", "done1"), 1, 1)

    println("there should be 3 topics")
    flowbit.getTopics()

    val source = new SQLiteSource("songs.db", List("id", "title", "length"), "select * from songs")
    println("adding producer")
    flowbit.addProducer[Int, Map[String, String]]("producer1", source, List("toBeFiltered1"))

    println("adding filter")
    flowbit.addFilter[Int, Map[String, String]]("filter1", "toBeFiltered1", List("toBeMapped1"),
      (k, v) => v.apply("length").toDouble > 3)

    println("adding map")
    flowbit.addMap[Int, Map[String, String], Int, Map[String, String]]("map1", "toBeMapped1", List("done1"),
      (k,v) => new KeyValue(k, v))

    // val dest = new SQLiteDestination("songs.db", "filtered")
    val dest = new CSVDestination("long-songs.csv")
    println("adding consumer")
    flowbit.addConsumer[Int, Map[String, String]]("consumer1", "done1", "group1", dest)
  }


}

