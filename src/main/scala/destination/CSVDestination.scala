package main.scala.destination

import java.io.{BufferedWriter, File, FileWriter}


class CSVDestination(filePath: String) extends Destination[Int, Map[String,String]] {
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

  val file: File = new File(filePath)
  file.createNewFile
  outputFile = new BufferedWriter(new FileWriter(file))


  override def record(data: (Int, Map[String,String])): Boolean = {
    if (!header) {
      outputFile.append(data._2.keys.reduce(_ + "," + _))
      outputFile.newLine();
      header = true;
    }

    outputFile.append(data._2.values.reduce(_ + "," + _))
    outputFile.newLine()

    return true
  }

  sys.ShutdownHookThread {
    outputFile.close()
  }
}


