package flowbit

/**
  * Trait representing a source of data.
  *
  * @tparam K {type of record key}
  * @tparam V {type of record value}
  */
trait Source[K, V] {

  /** *
    * Polls the data source for records which come in the form of mappings
    * from key to value.
    *
    * @return a stream of the maps.
    */
  def poll: Stream[(K, V)]

}
