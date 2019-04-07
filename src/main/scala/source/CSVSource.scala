package main.scala.source

import scala.collection.immutable.HashMap

/**
  * Represents a class that polls data from a CSV file.
  * It requires to know the columns so it can associate each values to its column.
  *
  * @param filePath path to the file.
  * @param columnNames the list of column names.
  */
class CSVSource(filePath: String, columnNames: List[String]) extends Source[Int, Map[String, Any]] {

  /** *
    * Polls the data source for records which come in the form of mappings
    * from key to value.
    *
    * @return a stream of the maps.
    */
  override def poll: Stream[(Int, Map[String, Any])] = {
    val sourceIter = io.Source.fromFile(filePath).getLines()
    var map: Map[String, String] = new HashMap().empty
    if (sourceIter.hasNext) {
      val line = sourceIter.next()
      val cols = line.split(",").map(_.trim)
      for (i <- 0 until columnNames.length) {
        map = map + ((columnNames(i)) -> (cols(i)))
      }
    }
    def genStream(iter: Iterator[String]): Map[String, String] = {
      var map: Map[String, String] = new HashMap().empty
      if (sourceIter.hasNext) {
        val line = sourceIter.next()
        val cols = line.split(",").map(_.trim)
        for (i <- 0 until columnNames.length) {
          map = map + ((columnNames(i)) -> (cols(i)))
        }
      }
      map
    }
    var stream = Stream.iterate((0 -> map))(t => (t._1 + 1) -> genStream(sourceIter))
    return stream
  }
}


