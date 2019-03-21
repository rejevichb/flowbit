package flowbit

import java.util.Properties

import org.apache.kafka.streams.{KafkaStreams, StreamsBuilder, StreamsConfig}
import org.apache.kafka.streams.scala.kstream.{KStream, Produced}
import org.apache.kafka.streams.scala.ImplicitConversions._

final class FilterComponent[A,B](id: String, server: String, fromTopic: String,
                                 toTopic: List[String], pred: (A,B) => Boolean) extends Component {

  // Stream configs
  val streamProps = new Properties()
  streamProps.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, server)
  streamProps.put(StreamsConfig.APPLICATION_ID_CONFIG, id)

  override def execute(): Unit = {

    val builder: StreamsBuilder = new StreamsBuilder()
    val stream: KStream[A, B] = builder.stream[A, B](fromTopic)
    val filteredStream: KStream[A, B] = stream.filter(pred)
    for (t <- toTopic) {
      filteredStream.to(t)(new Produced[A,B])
    }

    val streams: KafkaStreams = new KafkaStreams(builder.build(), streamProps)
    streams.start()

    sys.ShutdownHookThread {
      streams.close()
    }
  }
}
