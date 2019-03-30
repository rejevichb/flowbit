package flowbit.source

import java.io.FileInputStream
import java.util.Properties

import com.fasterxml.jackson.databind.{JsonNode, ObjectMapper}
import com.twitter.hbc.core.endpoint.{StatusesFilterEndpoint, StreamingEndpoint}
import org.apache.flink.api.common.functions.FlatMapFunction
import org.apache.flink.streaming.api.datastream.DataStream
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment
import org.apache.flink.streaming.connectors.twitter.TwitterSource
import org.apache.flink.streaming.connectors.twitter.TwitterSource.EndpointInitializer
import org.apache.flink.util.Collector

import collection.JavaConverters._

class TSource extends Source[String, String] {
  /** *
    * Polls the data source for records which come in the form of mappings
    * from key to value.
    *
    * @return a stream of the maps.
    */
  override def poll: Stream[(String, String)] = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    val properties = new Properties()
    properties.setProperty(TwitterSource.CONSUMER_KEY, "FnaAK4NLooDoq74oa7OXNKQpl")
    properties.setProperty(TwitterSource.CONSUMER_SECRET, "fSlZwEL1iOHhHlU1WRd6YN7faNJfw6MUy6GKN17LSGXpAdUOGT")
    properties.setProperty(TwitterSource.TOKEN, "1111682665877590017-tAXI5MoTfKn8HfJkn3BuL8YsdO2fX7")
    properties.setProperty(TwitterSource.TOKEN_SECRET, "MKill13Tm7ufXiJpaqO72REj8IxQmDs4U0nkVpXFfPEnv")


    /*
    val searchTerms = List("today")
    val endpoint = new StatusesFilterEndpoint()
    endpoint.trackTerms(searchTerms.asJava)
    val initializer : EndpointInitializer = new FilterStatusesEndpoint(endpoint)
    */

    val source = new TwitterSource(properties)
    //source.setCustomEndpointInitializer(initializer)

    val dataStream = env.addSource(source)

    val stream = dataStream.flatMap(new FlatMapFunction[String, String] {
      override def flatMap(in: String, out: Collector[String]): Unit = { out.collect(in) }
    })

    dataStream.setParallelism(1)

    dataStream.print()
    env.execute("go")
    return Stream.empty
  }

  class FilterStatusesEndpoint(endPoint: StreamingEndpoint) extends TwitterSource.EndpointInitializer {
    override def createEndpoint(): StreamingEndpoint = {
      endPoint
    }
  }
}
