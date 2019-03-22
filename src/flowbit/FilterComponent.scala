package flowbit

import org.apache.kafka.streams.{KafkaStreams, StreamsBuilder, StreamsConfig}
import org.apache.kafka.streams.scala.kstream.{KStream, Produced}
import org.apache.kafka.streams.scala.ImplicitConversions._

final class FilterComponent[A,B](id: String, server: String, fromTopic: String,
                                 toTopic: List[String], pred: (A,B) => Boolean) extends AbsComponent(id, server) {

  // Filter-specific configs
  properties.put(StreamsConfig.APPLICATION_ID_CONFIG, id)

  override def execute(): Unit = {

    val builder: StreamsBuilder = new StreamsBuilder()
    val stream: KStream[A, B] = builder.stream[A, B](fromTopic)
    val filteredStream: KStream[A, B] = stream.filter(pred)
    for (t <- toTopic) {
      filteredStream.to(t)(new Produced[A,B])
    }

    val streams: KafkaStreams = new KafkaStreams(builder.build(), properties)
    streams.start()

    sys.ShutdownHookThread {
      streams.close()
    }
  }
}
