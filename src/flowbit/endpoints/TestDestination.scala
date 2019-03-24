package flowbit.endpoints

class TestDestination extends Destination[String, String] {
  override def record(data: (String, String)): Boolean = {
    if (data == null) { println("Failed to record"); return false }
    println(data._1 + ", " + data._2)
    return true
  }
}
