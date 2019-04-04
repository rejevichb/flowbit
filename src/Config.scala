package config
import org.apache.kafka.streams.kstream.{Predicate}

class Config {

  val somePred: Predicate[Int, Map[String, String]] = (k, v) => v.apply("length").toDouble > 3

  val topics = List("toBeFiltered1", "toBeMapped1", "done1")

  val filter1: (String, (String, List[String], Predicate[Int, Map[String, String]])) =
                ("filter1", ("tBeFiltered1", List("toBeMapped1"), somePred))






}
