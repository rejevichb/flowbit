package main.scala.source

import scala.collection.immutable.HashMap

class MapSource extends Source[Int, HashMap[String, Int]] {
  /** *
    * Polls the data source for records which come in the form of mappings
    * from key to value.
    *
    * @return a stream of the maps.
    */
  override def poll: Stream[(Int, HashMap[String, Int])] = {
    def makeMap(n: Int): (Int, HashMap[String, Int]) = {
      var map : HashMap[String,Int] = new HashMap().empty
      for (i <- 0 to n) {
        map = map.+(("key" + i) -> (i))
      }
      ((n) -> (map))
    }
    return Stream.range(0,100).map(makeMap)
  }
}
