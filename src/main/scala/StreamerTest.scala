package main.scala

import java.util.Properties

import org.apache.kafka.streams.scala.ImplicitConversions._
import org.apache.kafka.streams.scala.Serdes._
import org.apache.kafka.streams.scala.StreamsBuilder
import org.apache.kafka.streams.scala.kstream.KStream
import org.apache.kafka.streams.{KafkaStreams, StreamsConfig}

class StreamerTest(fromTopic: String, toTopic: String) {

  def stream(): Unit = {
    val props: Properties = {
      val p = new Properties()
      p.put(StreamsConfig.APPLICATION_ID_CONFIG, "map1")
      p.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
      p
    }

    val builder: StreamsBuilder = new StreamsBuilder
    val textLines: KStream[String, String] = builder.stream[String, String](fromTopic)
    val filterLines: KStream[String, String] = textLines.filter((k,v) => {v.last.toInt % 2 == 0})
    val mapLines: KStream[String, String] = filterLines.map((k,v) => (k, v.concat("00")))
    mapLines.to(toTopic)

    val streams: KafkaStreams = new KafkaStreams(builder.build(), props)
    streams.start()

    sys.ShutdownHookThread {
      streams.close()
    }
  }
}
