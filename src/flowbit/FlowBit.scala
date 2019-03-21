package flowbit

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
  def addFilter[A,B](id: String, fromTopic: String, toTopic: List[String], pred: (A,B) => Boolean): Unit

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
  def addMap[A,B,C,D](id: String, fromTopic: String, toTopic: List[String], func: (A,B) => (C,D)): Unit

  /**
    * Creates a consumer that fetches the data from a given topic and writes it to a given path.
    *
    * @param id the id of the consumer.
    * @param topic the topic from which to get the values from.
    * @param filePath the path to write to.
    */
  def getConsumer(id: String, topic: String, filePath: String): Unit

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
