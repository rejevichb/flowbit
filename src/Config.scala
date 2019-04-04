package config
import org.apache.kafka.streams.kstream.{Predicate}

// Please define your desired configuration here

// Any topics you would like to add place them in the topics variable list

// Any filters you would like to add please write them as the example one is written, where
//   the desired predicate is defined above and then passed into the filter variable like so

// Any maps you would like to add please write them as the example one is written, where
//   the desired predicate is defined above and then passed into the map variable like so

class Config {

  val topics = List("toBeFiltered1", "toBeMapped1", "done1")

  val somePred: Predicate[Int, Map[String, String]] = (k, v) => v.apply("length").toDouble > 3

  val filter1: (String, (String, List[String], Predicate[Int, Map[String, String]])) =
                ("filter1", ("tBeFiltered1", List("toBeMapped1"), somePred))






}
