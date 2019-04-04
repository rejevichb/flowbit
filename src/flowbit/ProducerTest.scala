package flowbit

import java.util.Properties

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}

class ProducerTest(val topic: String) {

  def send(): Unit = {
    val props = new Properties()
    props.put("bootstrap.servers", "localhost:9092")
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")

    val producer = new KafkaProducer[String, String](props)
    for(i<- 1 to 5000){
      val record = new ProducerRecord(topic, "key", s"hello $i")
      producer.send(record).get()
    }
    producer.flush()
    producer.close()
  }

}
