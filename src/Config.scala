package config
import org.apache.kafka.streams.kstream.{Predicate}

class Config[A, B] {

  def someMap:collection.mutable.Map[String, (String, List[String], Predicate[A, B])] =
    collection.mutable.Map[String, (String, List[String], Predicate[A, B])]()

  def somePred: Predicate[A, B] = (k, v) => v.apply("length").toDouble > 3

  someMap.put("filter1", ("tBeFiltered1", List("toBeMapped1"), somePred))



}
