package flowbit.destination


import java.io.{BufferedWriter, File, FileOutputStream, FileWriter}
import java.nio.file.Files
import java.util

class CSVDestination(filePath: String) extends Destination[String, Map[String,String]] {
  /**
    * Records the given record in a data store.
    *
    * @param data the record which to write
    * @tparam K the type of key
    * @tparam V the type of value
    * @return whether the data was successfully recorded
    */

  var header = false;
  var outputFile : BufferedWriter = null

  val file: File = new File(filePath + "output.csv")
  file.createNewFile
  outputFile = new BufferedWriter(new FileWriter(file))


  override def record(data: (String, Map[String,String])): Boolean = {
    if (!header) {
      data._2.foreach(tuple => outputFile.append(tuple._1 + ","))
      outputFile.newLine();
      header = true;
    }

    for (item <- data._2) {
      println(item)
      outputFile.append(item._2)
      outputFile.append(",")
    }
    outputFile.newLine()

    return true
  }

  sys.ShutdownHookThread {
    outputFile.close()
  }
}


