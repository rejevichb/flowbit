package flowbit

import java.util.Properties

import flowbit.endpoints.{Destination, Source}

import scala.collection.immutable.HashMap
import scala.collection.immutable.HashSet
import org.apache.kafka.clients.admin.{AdminClient, AdminClientConfig, NewTopic}
import org.apache.kafka.common.config.TopicConfig
import org.apache.kafka.streams.KeyValue
import org.apache.kafka.streams.kstream.{KeyValueMapper, Predicate}

import collection.JavaConverters._

/**
  * Implementation of the FlowBit interface.
  * Stores the topics in a set and components in a hashmap.
  *
  * @param server the server on which runs the pipeline.
  */
class FlowBitImpl(server: String) extends FlowBit {
  var topics = new HashSet[String]
  var components = new HashMap[String, Component]().empty

  // Admin Client properties
  val adminProps = new Properties()
  adminProps.setProperty(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, server)

  val adminClient = AdminClient.create(adminProps)

  // Topic configs
  val topicConfigs = Map(TopicConfig.CLEANUP_POLICY_CONFIG -> TopicConfig.CLEANUP_POLICY_COMPACT,
    TopicConfig.COMPRESSION_TYPE_CONFIG -> "gzip").asJava


  val newTopics = this.topics.par.companion

  /**
    * Adds a new Kafka topic into the system.
    *
    * @param topic the topic to be added.
    */
  override def addTopic(topic: String, partitions: Int, replicationFactor: Int): Unit = {
    this.topics = this.topics + topic
    val t = new NewTopic(topic, partitions, replicationFactor.toShort)
    t.configs(this.topicConfigs)
    this.adminClient.createTopics(List(t).asJavaCollection)
  }

  /**
    * Adds a list of new topics into the system.
    *
    * @param topics list of topics.
    */
  override def addTopics(ts: List[String], partitions: Int, replicationFactor: Int) = {
    this.topics = this.topics ++ ts
    val listTopics = for (i <- 0 until ts.length;
                          t = new NewTopic(ts(i), partitions, replicationFactor.toShort)) yield t
    this.adminClient.createTopics(listTopics.asJavaCollection)
  }

  /**
    * Creates a producer that sends the data from the given {@link Source} to the given list of
    * topics.
    *
    * @param id the id of this component.
    * @param source the source of the data.
    * @param topics the topics to which to send the data.
    * @tparam A the key data type
    * @tparam B the value data type
    */
  override def addProducer[A,B](id: String, source: Source[A,B], toTopics: List[String]): Unit = {
    val component = new ProducerComponent(id, server, source, toTopics)
    this.components = this.components + (id -> component)
    component.execute()
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
                            pred: Predicate[A,B]): Unit = {
    val component = new FilterComponent(id, server, fromTopic, toTopic, pred)
    this.components = this.components + (id -> component)
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
                               func: KeyValueMapper[A,B,KeyValue[C,D]]): Unit = {
    val component = new MapComponent(id, server, fromTopic, toTopic, func)
    this.components = this.components + (id -> component)
    component.execute()
  }

  /**
    * Creates a consumer that fetches the data from a given topic and writes it to a given path.
    *
    * @param id    the id of the consumer.
    * @param topic the topic from which to get the values from.
    * @param groupId the id of the group that this consumer is subscribed to.
    * @param dest the destination to which to send the data.
    */
  override def addConsumer[A,B](id: String, topic: String, groupId: String,
                                dest: Destination[A, B]): Unit = {
    val component = new ConsumerComponent(id, server, dest, topic, groupId)
    this.components = this.components + (id -> component)
    component.execute()
  }

  /**
    * Prints the list of all units ids in this pipeline.
    */
  override def getUnits(): Unit = {
    components.foreach[Unit]((pair) => println(pair._1))
  }

  /**
    * Prints the list of all topics in this pipeline.
    */
  override def getTopics(): Unit = {
    topics.foreach(t => println(t))
  }
}
