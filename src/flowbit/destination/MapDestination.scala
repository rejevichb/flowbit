package flowbit.destination

import scala.collection.immutable.HashMap

class MapDestination extends Destination[String, HashMap[String, Int]] {
  /**
    * Records the given record in a data store.
    *
    * @param data the record which to write
    * @tparam K the type of key
    * @tparam V the type of value
    * @return whether the data was successfully recorded
    */
  override def record(data: (String, HashMap[String, Int])): Boolean = {
    if (data == null) { println("Failed to record"); return false }
    println(data._1 + ", " + data._2)
    return true
  }
}
