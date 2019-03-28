package flowbit.endpoints

import scala.collection.immutable.HashMap

class MapSource extends Source[Int, HashMap[String, Int]] {
  /** *
    * Polls the data source for records which come in the form of mappings
    * from key to value.
    *
    * @return a stream of the maps.
    */
  override def poll: Stream[(Int, HashMap[String, Int])] = {
    def getMap(x: Int): (Int, HashMap[String, Int]) = {
      var map : HashMap[String, Int] = new HashMap().empty
      for (i <- 0 to x) {
        map = map.+(("key" + i) -> (i))
      }
      ((x) -> (map))
    }
    return Stream.range(0, 100).map(getMap)
  }
}
