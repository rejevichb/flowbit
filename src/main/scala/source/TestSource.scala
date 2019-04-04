package main.scala.source

class TestSource extends Source[String, String] {

  override def poll: Stream[(String, String)] = {
    def convertToTuple(x :Int): (String, String) = (("key" + x) -> ("value" + x))
    return Stream.range(0, 100).map(convertToTuple)
  }
}
