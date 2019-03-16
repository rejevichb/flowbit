import java.util.Properties

import org.apache.kafka.clients.admin.{AdminClient, AdminClientConfig, NewTopic}
import collection.JavaConverters._

import org.apache.kafka.common.config.TopicConfig

object Main {

  def main(args: Array[String]): Unit = {

    val props = new Properties()
    props.setProperty(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")

    val adminClient = AdminClient.create(props)

    val numPartitions = 1
    val replicationFactor = 1.toShort
    val newTopic = new NewTopic("unprocessed", numPartitions, replicationFactor)
    val newTopic2 = new NewTopic("processed", numPartitions, replicationFactor)
    val configs = Map(TopicConfig.CLEANUP_POLICY_CONFIG -> TopicConfig.CLEANUP_POLICY_COMPACT,
      TopicConfig.COMPRESSION_TYPE_CONFIG -> "gzip")
    // settings some configs
    newTopic.configs(configs.asJava)
    newTopic2.configs(configs.asJava)
    adminClient.createTopics(List(newTopic, newTopic2).asJavaCollection)
    adminClient.close()

    val producer = new ProducerTest("unprocessed")
    producer.send()
    val streamer = new StreamerTest("unprocessed", "processed")
    streamer.stream()
    val consumer = new ConsumerTest("concat00", List("processed"),
      "/Users/danijj/Desktop/DANI/projects/flowbit/output.txt")
    consumer.consume()
  }

}
