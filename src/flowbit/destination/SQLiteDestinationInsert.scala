package flowbit.destination

class SQLiteDestinationInsert extends Destination[Int, List[String]] {
  /**
    * Records the given record in a data store.
    *
    * @param data the record which to write
    * @tparam K the type of key
    * @tparam V the type of value
    * @return whether the data was successfully recorded
    */
  override def record(data: (Int, List[String])): Boolean = {
    return true
  }
}
