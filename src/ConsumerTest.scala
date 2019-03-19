import java.io.{File, PrintWriter}
import java.time.Duration
import java.util.Properties

import collection.JavaConverters._
import org.apache.kafka.clients.consumer.KafkaConsumer

class ConsumerTest(id: String, topics: List[String], filePath: String) {

  def consume(): Unit = {
    val props = new Properties()
    props.put("bootstrap.servers", "localhost:9092")
    props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
    props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
    props.put("group.id", id)

    val consumer = new KafkaConsumer[String, String](props)
    println(consumer.listTopics() )

    // Need to convert to java collection
    consumer.subscribe(topics.asJava)

    val writer = new PrintWriter(new File(filePath));

    var counter = 0;
    while(true) {
      if (counter >= 10) {
        writer.flush()
        counter = 0
      }
      val records = consumer.poll(Duration.ofMillis(100)).asScala
      for (record <- records) {
        counter += 1
        writer.write(record.value() + '\n')
      }
    }
    writer.flush()
    writer.close()
    consumer.close()
  }
}



