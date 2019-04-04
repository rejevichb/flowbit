package main.scala.source

/**
  * Represents a class that polls data from a CSV file.
  * It requires to know the columns so it can associate each values to its column.
  *
  * @param filePath path to the file.
  * @param columnNames the list of column names.
  */
class CSVSource(filePath: String, columnNames: List[String]) extends Source[String, String] {
  /** *
    * Polls the data source for records which come in the form of mappings
    * from key to value.
    *
    * @return a stream of the maps.
    */
  override def poll: Stream[(String, String)] = {
    val bufferedSource = io.Source.fromFile(filePath)
    var stream = Stream.empty[(String, String)]
    for (line <- bufferedSource.getLines) {
      val cols = line.split(",").map(_.trim)
      for (i <- 0 until columnNames.length) {
        stream = stream :+ ((columnNames(i)) -> (cols(i)))
      }
    }
    bufferedSource.close
    return stream
  }
}

