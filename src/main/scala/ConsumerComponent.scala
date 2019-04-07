package main.scala

import java.time.Duration

import main.scala.destination.Destination
import org.apache.kafka.clients.consumer.KafkaConsumer

import scala.collection.JavaConverters._

class ConsumerComponent[A,B](id: String, server: String, dest: Destination[A,B], topic: String, groupId: String)
  extends AbsComponent(id, server) {

  final val giveUp = 100
  var noRecordsCount = 0

  // Consumer-specific properties
  val serde = new serdes.AnySerde
  properties.put("key.deserializer", serde.deserializer().getClass().getName)
  properties.put("value.deserializer", serde.deserializer().getClass().getName)
  properties.put("group.id", groupId)

  override def execute(): Unit = {
    val consumer = new KafkaConsumer[A, B](properties)

    // Need to convert to java collection
    consumer.subscribe(List(topic).asJavaCollection)

    while (noRecordsCount < giveUp) {
      val records = consumer.poll(Duration.ofMillis(100))

      if (records.count() == 0) noRecordsCount += 1

      records.forEach((r) => dest.record(r.key() -> r.value()))

      // Commit offset returned by last poll call
      consumer.commitAsync()
    }

    consumer.close()
  }
}
