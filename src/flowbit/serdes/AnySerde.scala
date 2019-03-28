package flowbit.serdes

import java.util

import flowbit.serdes.deserializer.AnyDeserializer
import flowbit.serdes.serializer.AnySerializer
import org.apache.kafka.common.serialization.{Deserializer, Serde, Serializer}

class AnySerde extends Serde[Any] {
  override def configure(map: util.Map[String, _], b: Boolean): Unit = {}

  override def close(): Unit = {}

  override def serializer(): Serializer[Any] = {
    new AnySerializer
  }

  override def deserializer(): Deserializer[Any] = {
    new AnyDeserializer
  }
}

