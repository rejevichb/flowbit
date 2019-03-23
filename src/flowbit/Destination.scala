package flowbit

/**
  * Trait representing destination for data to be stored.
  *
  * @tparam K {type of record key}
  * @tparam V {type of record value}
  */
trait Destination[K, V] {

  /**
    * Records the given record in a data store.
    *
    * @param data the record which to write
    * @tparam K the type of key
    * @tparam V the type of value
    * @return whether the data was successfully recorded
    */
  def record(data: (K, V)): Boolean
}
