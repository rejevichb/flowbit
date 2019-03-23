package flowbit

import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.{KafkaStreams, StreamsBuilder, StreamsConfig}
import org.apache.kafka.streams.kstream.{KStream, Predicate}


final class FilterComponent[A,B](id: String, server: String, fromTopic: String,
                                 toTopic: List[String], pred: Predicate[A,B]) extends AbsComponent(id, server) {

  // Filter-specific configs
  properties.put(StreamsConfig.APPLICATION_ID_CONFIG, id)

  // Add to stop casting and serialization issues with strings
  val strings = Serdes.String()
  properties.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, strings.getClass().getName())
  properties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, strings.getClass().getName())


  override def execute(): Unit = {
    val builder: StreamsBuilder = new StreamsBuilder()
    val stream: KStream[A, B] = builder.stream[A, B](fromTopic)
    val filteredStream: KStream[A, B] = stream.filter(pred)
    for (t <- toTopic) {
      filteredStream.to(t)
    }

    val streams: KafkaStreams = new KafkaStreams(builder.build(), properties)
    streams.start()

    sys.ShutdownHookThread {
      streams.close()
    }
  }
}
