package flowbit

import java.util.Properties

class ConsumerComponent(id: String, server: String, filePath: String, topic: String)
  extends AbsComponent(id, server) {

  val props = new Properties()
  props.put("bootstrap.servers", server)
  props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
  props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
  props.put("group.id", id)

  override def execute(): Unit = {

  }
}
