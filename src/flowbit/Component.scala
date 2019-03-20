package flowbit

trait Component {

  def execute[A,B](pred: (A,B) => Boolean): Unit

}
