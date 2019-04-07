package main.scala.serdes.serializer

import java.io.{ByteArrayOutputStream, ObjectOutputStream}
import java.util

import org.apache.kafka.common.serialization.Serializer

class AnySerializer extends Serializer[Any] {
  override def configure(map: util.Map[String, _], b: Boolean): Unit = {}

  override def serialize(s: String, t: Any): Array[Byte] = {
    val stream: ByteArrayOutputStream = new ByteArrayOutputStream()
    val oos = new ObjectOutputStream(stream)
    oos.writeObject(t)
    oos.close()
    stream.toByteArray
  }

  override def close(): Unit = {}
}
