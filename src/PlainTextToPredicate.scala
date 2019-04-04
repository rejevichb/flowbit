import net.objecthunter.exp4j.ExpressionBuilder
import org.apache.kafka.streams.kstream.Predicate

import javax.script.{ScriptEngine, ScriptEngineManager}

object PlainTextToPredicate {

  def main(args :Array[String]): Unit = {

    val sampleText = "$length > 3"

    def pred(text: String): Predicate[Int, Map[String, AnyVal]] = {
      return (key, map) => {
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

    val map = Map[String, AnyVal]("length" -> 5, "bob" -> 5)
    println(pred("$length == $bob"))
  }

}
