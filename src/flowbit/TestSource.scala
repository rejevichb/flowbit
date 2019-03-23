package flowbit
import scala.collection.immutable.HashMap

class TestSource extends Source[String, String] {
  override def getData(): HashMap[String, String] = {
    var map = new HashMap[String, String]().empty
    for (i <- 0 to 100) {
      map = map + (("key" + i) -> ("value" + i))
    }
    map.foreach[Unit](v => println(v._1 + "," + v._2))
    println("here")
    return map
  }

  override def putData[A, B](data: HashMap[A, B]): Unit = {
    data.foreach[Unit]((pair) => println(pair._1 + " " + pair._2))
  }
}
