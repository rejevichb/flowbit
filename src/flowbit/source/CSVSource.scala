package flowbit.source

import scala.collection.immutable.HashMap

/**
  * Represents a class that polls data from a CSV file.
  * It requires to know the columns so it can associate each values to its column.
  *
  * @param filePath path to the file.
  * @param columnNames the list of column names.
  */
class CSVSource(filePath: String, columnNames: List[String]) extends Source[Int, Map[String, String]] {
  /** *
    * Polls the data source for records which come in the form of mappings
    * from key to value.
    *
    * @return a stream of the maps.
    */
  override def poll: Stream[(Int, Map[String, String])] = {
    val bufferedSource = io.Source.fromFile(filePath)
    var stream = Stream.empty[(Int, Map[String, String])]
    var rowLine = 0;
    for (line <- bufferedSource.getLines) {
      val cols = line.split(",").map(_.trim)
      var map: Map[String, String] = new HashMap().empty
      for (i <- 0 until columnNames.length) {
        map = map + ((columnNames(i)) -> (cols(i)))
      }
      stream = stream :+ ((rowLine) -> map)
    }
    bufferedSource.close
    return stream
  }
}


