package flowbit

import org.apache.kafka.streams.kstream.{KStream, KeyValueMapper}
import org.apache.kafka.streams.{KafkaStreams, KeyValue, StreamsBuilder, StreamsConfig}
import org.apache.kafka.streams.scala.ImplicitConversions._


final class MapComponent[A,B,C,D](id: String, server: String, fromTopic: String,
                                  toTopic: List[String], func: KeyValueMapper[A,B,KeyValue[C,D]]) extends AbsComponent(id, server) {

  // Map-specific configs
  properties.put(StreamsConfig.APPLICATION_ID_CONFIG, id)

  override def execute(): Unit = {

    val builder: StreamsBuilder = new StreamsBuilder()
    val stream: KStream[A, B] = builder.stream[A, B](fromTopic)
    val filteredStream: KStream[C,D] = stream.map(func)
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
