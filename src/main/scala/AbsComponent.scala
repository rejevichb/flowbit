package main.scala

import java.util.Properties

abstract class AbsComponent(id: String, server: String) extends Component {

  val properties = new Properties()
  properties.put("bootstrap.servers", server)
}
