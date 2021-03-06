package main.scala.destination

import java.io.{BufferedWriter, File, FileWriter}


class CSVDestination(filePath: String) extends Destination[Int, Map[String, Any]] {
  /**
    * Records the given record in a data store.
    *
    * @param data the record which to write
    * @tparam K the type of key
    * @tparam V the type of value
    * @return whether the data was successfully recorded
    */

  var header = false;
  var outputFile : BufferedWriter = _

  val file: File = new File(filePath)
  file.createNewFile
  outputFile = new BufferedWriter(new FileWriter(file))


  override def record(data: (Int, Map[String, Any])): Boolean = {
    if (!header) {
      outputFile.append(data._2.keys.reduce(_ + "," + _))
      outputFile.newLine();
      header = true;
    }

    outputFile.append(data._2.values.map(_.toString).reduce(_ + "," + _))
    outputFile.newLine()
    true
  }

  sys.ShutdownHookThread {
    outputFile.close()
  }
}


