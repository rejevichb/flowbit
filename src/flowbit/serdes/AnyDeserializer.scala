package flowbit.serdes

import java.io.{ByteArrayInputStream, ObjectInputStream}
import java.util

import org.apache.kafka.common.serialization.Deserializer

class AnyDeserializer extends Deserializer[Any] {

  override def configure(map: util.Map[String, _], b: Boolean): Unit = {}

  override def deserialize(s: String, bytes: Array[Byte]): Any = {
    val ois = new ObjectInputStream(new ByteArrayInputStream(bytes))
    val value = ois.readObject
    ois.close()
    value
  }

  override def close(): Unit = {}
}
