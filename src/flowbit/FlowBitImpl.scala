package flowbit

import java.util.Properties

import scala.collection.mutable.HashMap
import scala.collection.mutable.HashSet
import org.apache.kafka.clients.admin.{AdminClient, AdminClientConfig, NewTopic}
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import org.apache.kafka.common.config.TopicConfig
import org.apache.kafka.streams.scala.Serdes
import org.apache.kafka.streams.scala.ImplicitConversions._
import org.apache.kafka.streams.{KafkaStreams, StreamsBuilder, StreamsConfig}
import org.apache.kafka.streams.scala.kstream.{KStream, Produced}

import collection.JavaConverters._

/**
  * Implementation of the FlowBit interface.
  * Stores the topics in a set and components in a hashmap.
  *
  * @param server the server on which runs the pipeline.
  */
class FlowBitImpl(server: String) extends FlowBit {
  var topics = new HashSet[String]
  var components = new HashMap[String, Component]

  // Admin Client properties
  val adminProps = new Properties()
  adminProps.setProperty(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, server)

  val adminClient = AdminClient.create(adminProps)

  // Topic configs
  val topicConfigs = Map(TopicConfig.CLEANUP_POLICY_CONFIG -> TopicConfig.CLEANUP_POLICY_COMPACT,
    TopicConfig.COMPRESSION_TYPE_CONFIG -> "gzip").asJava

  // Producer configs
  val prodProps = new Properties()
  prodProps.put("bootstrap.servers", server)
  prodProps.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
  prodProps.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")

  /**
    * Adds a new Kafka topic into the system.
    *
    * @param topic the topic to be added.
    */
  override def addTopic(topic: String, partitions: Int, replicationFactor: Int): Unit = {
    topics.add(topic)
    val t = new NewTopic(topic, partitions, replicationFactor.toShort)
    t.configs(topicConfigs)
    adminClient.createTopics(List(t).asJavaCollection)
  }

  /**
    * Adds a list of new topics into the system.
    *
    * @param topics list of topics.
    */
  override def addTopics(ts: List[String], partitions: Int, replicationFactor: Int) = {
    topics.++=(ts)
    val listTopics = for (i <- 0 to ts.length;
                          t = new NewTopic(ts(i), partitions, replicationFactor.toShort)) yield t
    adminClient.createTopics(listTopics.asJavaCollection)
  }

  /**
    * Creates a producer that sends the given data to the given list of topics.
    *
    * @param data   the data to be sent into the pipeline.
    * @param topics the topics to which to send the data.
    * @tparam A the key data type
    * @tparam B the value data type
    */
  override def sendData[A, B](data: HashMap[A, B], tpcs: List[String]): Unit = {
    val producer = new KafkaProducer[A,B](prodProps)
    data.foreach[Unit](((d) =>
      for (t <- tpcs) {
        producer.send(new ProducerRecord[A, B](t, d._1, d._2))
      }))
    producer.flush()
    producer.close()
  }

  /**
    * Adds a new filter unit that filters the streams of data from a given {@code fromTopic}
    * to a given {@code toTopic}.
    *
    * @param id        the id of the unit.
    * @param fromTopic the topic to which to subscribe to.
    * @param toTopic   the topic to which to publish to.
    * @param pred      the predicate of the filter.
    * @tparam A the type of the keys to be processed.
    * @tparam B the type of the values to be processed.
    */
  override def addFilter[A,B](id: String, fromTopic: String, toTopic: List[String],
                            pred: (A,B) => Boolean): Unit = {
    val component = new FilterComponent(id, server, fromTopic, toTopic, pred)
    this.components.put(id, component)
    component.execute()
  }

  /**
    * Adds a new map unit that executes a map {@code func} on a stream of data from a given
    * {@code fromTopic} to a given {@code toTopic}.
    *
    * @param id        the id of the unit.
    * @param fromTopic the topic to which to subscribe to.
    * @param toTopic   the topic to which to publish to.
    * @param func      the map function.
    * @tparam A the data type of the data to be processed.
    * @tparam B the data type of the processed data.
    */
  override def addMap[A,B,C,D](id: String, fromTopic: String, toTopic: List[String],
                               func: (A,B) => (C,D)): Unit = {
    val component = new MapComponent(id, server, fromTopic, toTopic, func)
    this.components.put(id, component)
    component.execute()
  }

  /**
    * Creates a consumer that fetches the data from a given topic and writes it to a given path.
    *
    * @param id    the id of the consumer.
    * @param topic the topic from which to get the values from.
    * @param filePath the path to write to.
    */
  override def getConsumer(id: String, topic: String, filePath: String): Unit = {
    
  }

  /**
    * Removes a unit from the pipeline.
    *
    * @param id the id of the unit.
    */
  override def removeUnit(id: String): Unit = ???

  /**
    * Returns the list of all units ids in this pipeline.
    *
    * @return the list of units ids.
    */
  override def getUnits(): List[String] = ???

  /**
    * Returns the list of all topics in this pipeline.
    *
    * @return the list of topics.
    */
  override def getTopics(): List[String] = ???
}
