package flowbit

import scala.collection.immutable.HashMap

/**
  * Temp trait representing source and sink of the data for the pipeline.
  * @tparam A
  * @tparam B
  */
trait Source[A,B] {

  def getData(): HashMap[A,B]

  def putData[A,B](data: HashMap[A,B]): Unit

}
