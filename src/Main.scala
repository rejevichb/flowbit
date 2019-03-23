import flowbit.{FlowBitImpl, TestDestination, TestSource}
import org.apache.kafka.streams.KeyValue
import org.apache.kafka.streams.kstream.Predicate

object Main {

  def main(args: Array[String]): Unit = {

    val flowbit = new FlowBitImpl("localhost:9092")

    println("there should be no topics in the flowbit")
    flowbit.getTopics()

    println("adding topics")
    flowbit.addTopics(List("toBeFiltered", "toBeMapped", "done"), 1, 1)

    println("there should be 3 topics")
    flowbit.getTopics()

    val source = new TestSource
    println("adding producer")
    flowbit.addProducer[String, String]("producer1", source, List("toBeFiltered"))

    println("adding filter")
    flowbit.addFilter[String, String]("filter1", "toBeFiltered", List("toBeMapped"),
      new TestPred())

    println("adding map")
    flowbit.addMap[String, String, String, String]("map1", "toBeMapped", List("done"),
      (k,v) => new KeyValue(k, v + (v.last.toInt * 10).toString))

    val dest = new TestDestination
    println("adding consumer")
    flowbit.addConsumer[String, String]("consumer1", "done", "group1", dest)
  }

}

class TestPred extends Predicate[String, String] {
  override def test(k: String, v: String): Boolean = {
    k.last.toInt % 2 == 0
  }
}
