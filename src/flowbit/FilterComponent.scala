package flowbit

import org.apache.kafka.streams.{KafkaStreams, StreamsBuilder, StreamsConfig}
import org.apache.kafka.streams.kstream.{KStream, Predicate}


final class FilterComponent[A,B](id: String, server: String, fromTopic: String,
                                 toTopic: List[String], pred: Predicate[A,B]) extends AbsComponent(id, server) {

  // Filter-specific configs
  properties.put(StreamsConfig.APPLICATION_ID_CONFIG, id)

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
