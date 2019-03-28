package flowbit

import flowbit.endpoints.Source
import flowbit.serdes.AnySerde
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}

class ProducerComponent[A, B](id: String, server: String, source: Source[A, B], toTopics: List[String])
  extends AbsComponent(id, server) {

  // Producer-specific configs
  val serde = new AnySerde
  properties.put("value.serializer", serde.serializer().getClass().getName)
  properties.put("key.serializer", serde.serializer().getClass().getName)


  override def execute(): Unit = {
    val producer = new KafkaProducer[A, B](properties)

    source.poll.foreach[Unit]((entry) =>
      for (t <- toTopics) {
        producer.send(new ProducerRecord[A, B](t, entry._1, entry._2))
      }
    )

    producer.flush()
    producer.close()
  }
}
