import org.apache.kafka.streams.kstream.Predicate

import javax.script.{ScriptEngine, ScriptEngineManager}

object PlainTextToPredicate {

  def main(args :Array[String]): Unit = {

    def textToPred(text: String): Predicate[Int, Map[String, Any]] = {
      (key, map) => {
        val fact = new ScriptEngineManager()
        val engine = fact.getEngineByName("JavaScript")

        val pattern = "\\$(\\w+)".r
        val vars = pattern.findAllIn(text)

        vars.foreach(v => {
          engine.put(v, map.apply(v.substring(1)))
        })

        val res = engine.eval(text).toString
        java.lang.Boolean.valueOf(res)
      }
    }

    def textToMap(text: String): Function[(Int, Map[String, Any]), (Int, Map[String, Any])] = {
      return (tuple: (Int, Map[String, Any])) => {
        try {
          val key: Int = tuple._1
          val map: Map[String, Any] = tuple._2
          val fact = new ScriptEngineManager()

          val exprs = text.split(",")
          val assignments = exprs.map(e => e.split("="))
            .map(assignment => {
              if (assignment.length != 2) {
                throw new RuntimeException;
              }
              else (assignment.head, assignment.tail.head);
            })

          val pattern = "\\$(\\w+)".r

          var endMap = map.filter(_ => true)
          assignments.foreach(assignment => {
            val engine = fact.getEngineByName("JavaScript")

            val vars = pattern.findAllIn(assignment._2)
            vars.foreach(v => { engine.put(v, endMap.apply(v.trim.substring(1)))})

            val res = engine.eval(assignment._2)
            endMap += (assignment._1.trim.substring(1) -> res)
          })

          (key, endMap);
        } catch {
          case e: Exception => throw e // throw new RuntimeException("Map function was applied on bad parameters")
        }
      }
    }

    val map = Map[String, Any]("name" -> "PG", "hours" -> 50, "overtime" -> 40, "bonus" -> 1.5, "wage" -> 20)
    println(textToPred("$name == \"PG\"").test(1, map))

    val getPayText =
      """
        | $pay = ($hours < $overtime) ? ($hours * $wage) : ($overtime * $wage) + (($hours - $overtime) * $wage * $bonus),
        | $copy = $pay
      """.stripMargin
    println(textToMap(getPayText).apply(1, map))
  }

}
