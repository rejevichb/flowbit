package flowbit

import java.time.Duration

import flowbit.endpoints.Destination
import flowbit.serdes.AnySerde
import org.apache.kafka.clients.consumer.KafkaConsumer

import collection.JavaConverters._

class ConsumerComponent[A,B](id: String, server: String, dest: Destination[A,B], topic: String, groupId: String)
  extends AbsComponent(id, server) {

  final val giveUp = 100
  var noRecordsCount = 0

  // Consumer-specific properties
  val anySerdes = new AnySerde();
  properties.put("key.deserializer", anySerdes.deserializer().getClass())
  properties.put("value.deserializer", anySerdes.deserializer().getClass())
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
