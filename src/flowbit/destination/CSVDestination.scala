package flowbit.destination


import java.io.{BufferedWriter, File, FileOutputStream, FileWriter}


class CSVDestination(filePath: String) extends Destination[String, List[String]] {
  /**
    * Records the given record in a data store.
    *
    * @param data the record which to write
    * @tparam K the type of key
    * @tparam V the type of value
    * @return whether the data was successfully recorded
    */

  var outputFile : BufferedWriter = null

  def init(): Unit =  {
    val file: File = new File(filePath + "output.csv")
    file.createNewFile
    outputFile = new BufferedWriter(new FileWriter(file))
  }


  override def record(data: (String, List[String])): Boolean = {

    for (item <- data._2) {
      println(item)
      outputFile.append(item)
      outputFile.append(",")
    }
    outputFile.newLine()

    return true
  }

  def end(): Unit = {
    outputFile.close()
  }
}


