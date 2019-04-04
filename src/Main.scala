import flowbit.FlowBitImpl
import flowbit.destination._
import flowbit.source._
import org.apache.kafka.streams.KeyValue
import config.Config

object Main {


  def main(args: Array[String]): Unit = {

    val flowbit = new FlowBitImpl("localhost:9092")
    val config = new Config

    println("there should be no topics in the flowbit")
    flowbit.getTopics()

    println("adding topics")
    flowbit.addTopics(config.topics, 1, 1)

    println("there should be 3 topics")
    flowbit.getTopics()

    val source = new SQLiteSource("songs.db", List("id", "title", "length"), "select * from songs")
    println("adding producer")
    flowbit.addProducer[Int, Map[String, String]]("producer1", source, List("toBeFiltered1"))

    println("adding filter")
    flowbit.addFilter[Int, Map[String, String]](config.filter1._1, config.filter1._2._1, config.filter1._2._2,
                                                config.filter1._2._3)

    println("adding map")
    flowbit.addMap[Int, Map[String, String], Int, Map[String, String]]("map1", "toBeMapped1", List("done1"),
      (k,v) => new KeyValue(k, v))

    // val dest = new SQLiteDestination("songs.db", "filtered")
    val dest = new CSVDestination("long-songs.csv")
    println("adding consumer")
    flowbit.addConsumer[Int, Map[String, String]]("consumer1", "done1", "group1", dest)
  }


}

