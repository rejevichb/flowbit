import java.util.function.Function
import javax.script._

val pred = "(k, v) => v.apply(\"length\").toDouble > 3"

val engine = new ScriptEngineManager().getEngineByName("nashorn")
@SuppressWarnings(Array("unchecked"))
val f = engine.eval(String.format("new java.util.function.Function(%s)",
                      pred)).asInstanceOf[Function[Any, Any]]

