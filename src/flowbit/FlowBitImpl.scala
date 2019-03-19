package flowbit

import java.util.Properties

import scala.collection.mutable.HashMap
import scala.collection.mutable.HashSet
import org.apache.kafka.clients.admin.{AdminClient, AdminClientConfig, NewTopic}
import org.apache.kafka.common.config.TopicConfig
import collection.JavaConverters._

/**
  * Implementation of the FlowBit interface.
  * Stores the topics in a set and units in a hashmap.
  *
  * @param server the server on which runs the pipeline.
  */
class FlowBitImpl(server: String) extends FlowBit {
  var topics = new HashSet[String]
  var units = new HashMap[String, Unit]

  // Admin Client properties
  val props = new Properties()
  props.setProperty(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, server)

  val adminClient = AdminClient.create(props)

  // Topic configs
  val topicConfigs = Map(TopicConfig.CLEANUP_POLICY_CONFIG -> TopicConfig.CLEANUP_POLICY_COMPACT,
    TopicConfig.COMPRESSION_TYPE_CONFIG -> "gzip").asJava

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
  override def sendData[A, B](data: HashMap[A, B], topics: List[String]): Unit = ???

  /**
    * Adds a new filter unit that filters the streams of data from a given {@code fromTopic}
    * to a given {@code toTopic}.
    *
    * @param id        the id of the unit.
    * @param fromTopic the topic to which to subscribe to.
    * @param toTopic   the topic to which to publish to.
    * @param pred      the predicate of the filter.
    * @tparam A the values beings processed.
    */
  override def addFilter[A](id: String, fromTopic: String, toTopic: String, pred: A => Boolean): Unit = ???

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
  override def addMap[A, B](id: String, fromTopic: String, toTopic: String, func: A => B): Unit = ???

  /**
    * Creates a consumer that returns the data from a given topic as a list.
    *
    * @param id    the id of the consumer.
    * @param topic the topic from which to get the values from.
    * @tparam A the data type of the values to be retrieved.
    * @return the values as a list
    */
  override def getConsumer[A](id: String, topic: String): List[A] = ???

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
