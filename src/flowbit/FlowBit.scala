package flowbit

import scala.collection.immutable.HashMap

/**
  * Flowbit API to build customized ETL pipelines using Kafka.
  * How to use:
  * - create topics where components will publish/subscribe to
  * - add components
  */
trait FlowBit {

  /**
    * Adds a new Kafka topic into the system.
    *
    * @param topic the topic to be added.
    */
  def addTopic(topic: String): Unit

  /**
    * Adds a list of new topics into the system.
    *
    * @param topics list of topics.
    */
  def addTopics(topics: List[String]): Unit

  /**
    * Creates a producer that sends the given data to the given list of topics.
    *
    * @param data the data to be sent into the pipeline.
    * @param topics the topics to which to send the data.
    * @tparam A the key data type
    * @tparam B the value data type
    */
  def sendData[A,B](data: HashMap[A,B], topics: List[String]): Unit

  /**
    * Adds a new filter unit that filters the streams of data from a given {@code fromTopic}
    * to a given {@code toTopic}.
    *
    * @param id the id of the unit.
    * @param fromTopic the topic to which to subscribe to.
    * @param toTopic the topic to which to publish to.
    * @param pred the predicate of the filter.
    * @tparam A the values beings processed.
    */
  def addFilter[A](id: String, fromTopic: String, toTopic: String, pred: (A) => Boolean): Unit

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
  def addMap[A,B](id: String, fromTopic: String, toTopic: String, func: (A) => B): Unit

  /**
    * Creates a consumer that returns the data from a given topic as a list.
    *
    * @param id the id of the consumer.
    * @param topic the topic from which to get the values from.
    * @tparam A the data type of the values to be retrieved.
    *
    * @return the values as a list
    */
  def getConsumer[A](id: String, topic: String): List[A]

  /**
    * Removes a unit from the pipeline.
    *
    * @param id the id of the unit.
    */
  def removeUnit(id: String): Unit

  /**
    * Returns the list of all units ids in this pipeline.
    *
    * @return the list of units ids.
    */
  def getUnits(): List[String]

  /**
    * Returns the list of all topics in this pipeline.
    *
    * @return the list of topics.
    */
  def getTopics(): List[String]

}
