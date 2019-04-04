package main.scala.destination

class SQLiteDestinationTable() extends Destination[Int, List[String]] {
  /**
    * Records the given record in a data store.
    *
    * @param data the record which to write
    * @tparam K the type of key
    * @tparam V the type of value
    * @return whether the data was successfully recorded
    */
  override def record(data: (Int, List[String])): Boolean = {
    for (name <- data._2) println(name)
    return true
  }



}
