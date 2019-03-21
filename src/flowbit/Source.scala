package flowbit

import scala.collection.immutable.HashMap

trait Source[A,B] {

  def getData(): HashMap[A,B]

  def putData[A,B](data: HashMap[A,B]): Unit

}
