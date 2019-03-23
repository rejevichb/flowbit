package flowbit

import org.apache.kafka.streams.KeyValue
import org.apache.kafka.streams.kstream.{KeyValueMapper, Predicate}

/**
  * Flowbit API to build customized ETL pipelines using Kafka.
  * How to use:
  * - create topics where components will publish/subscribe to
  * - add components
  */
trait FlowBit {

  /**
    * Adds a new Kafka topic into the system with the given partition and replication factor.
    *
    * @param topic the topic to be added.
    */
  def addTopic(topic: String, partitions: Int, replicationFactor: Int): Unit

  /**
    * Adds a list of new topics into the system where each topic has the given partitions
    * and replication factor.
    *
    * @param topics list of topics.
    */
  def addTopics(topics: List[String], partitions: Int, replicationFactor: Int): Unit

  /**
    * Creates a producer that sends the data from the given {@link Source} to the given list of
    * topics.
    *
    * @param id the id of this component.
    * @param source the source of the data.
    * @param toTopics the topics to which to send the data.
    * @tparam A the key data type
    * @tparam B the value data type
    */
  def addProducer[A,B](id: String, source: Source[A,B], toTopics: List[String]): Unit

  /**
    * Adds a new filter unit that filters the streams of data from a given {@code fromTopic}
    * to a given {@code toTopic}.
    *
    * @param id the id of the unit.
    * @param fromTopic the topic to which to subscribe to.
    * @param toTopic the topic to which to publish to.
    * @param pred the predicate of the filter.
    * @tparam A the type of the keys to be processed.
    * @tparam B the type of the values to be processed.
    */
  def addFilter[A,B](id: String, fromTopic: String, toTopic: List[String], pred: Predicate[A,B]): Unit

  /**
    * Adds a new map unit that executes a map {@code func} on a stream of data from a given
    * {@code fromTopic} to a given {@code toTopic}.
    *
    * @param id the id of the unit.
    * @param fromTopic the topic to which to subscribe to.
    * @param toTopic the topic to which to publish to.
    * @param func the map function.
    * @tparam A the data type of the data to be processed.
    * @tparam B the data type of the processed data.
    */
  def addMap[A,B,C,D](id: String, fromTopic: String, toTopic: List[String], func: KeyValueMapper[A,B,KeyValue[C,D]]): Unit

  /**
    * Creates a consumer that fetches the data from a given topic and sends it to a given
    * {@link Source} destination.
    *
    * @param id the id of the consumer.
    * @param topic the topic from which to get the values from.
    * @param groupId the id of the group that this consumer is subscribed to.
    * @param dest the destination to which to send the data.
    */
  def addConsumer[A,B](id: String, topic: String, groupId: String, dest: Source[A,B]): Unit

  /**
    * Prints the list of all units ids in this pipeline.
    */
  def getUnits(): Unit

  /**
    * Prints the list of all topics in this pipeline.
    */
  def getTopics(): Unit
}
