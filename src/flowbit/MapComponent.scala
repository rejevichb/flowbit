package flowbit

import java.util.Properties

import org.apache.kafka.streams.{KafkaStreams, StreamsBuilder, StreamsConfig}
import org.apache.kafka.streams.scala.kstream.{KStream, Produced}
import org.apache.kafka.streams.scala.ImplicitConversions._


final class MapComponent[A,B,C,D](id: String, server: String, fromTopic: String,
                                  toTopic: List[String], func: (A,B) => (C,D)) extends AbsComponent(id, server) {

  // Map-specific configs
  properties.put(StreamsConfig.APPLICATION_ID_CONFIG, id)

  override def execute(): Unit = {

    val builder: StreamsBuilder = new StreamsBuilder()
    val stream: KStream[A, B] = builder.stream[A, B](fromTopic)
    val filteredStream: KStream[C, D] = stream.map[C,D](func)
    for (t <- toTopic) {
      filteredStream.to(t)(new Produced[C,D])
    }

    val streams: KafkaStreams = new KafkaStreams(builder.build(), properties)
    streams.start()

    sys.ShutdownHookThread {
      streams.close()
    }
  }
}
