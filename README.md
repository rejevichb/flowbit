Flowbit - Data streaming pipleline using queues and operators to transform data into desired format

Libraries: 

org.apache.kafka:kafka-clients:2.1.1

org.apache.kafka:kafka-streams-scala_2.11:2.1.1

org.slf4j:slf4j-api:1.8.0-beta4

org.slf4j:slf4j-simple:1.8.0-beta4



Connection issues:

In case of kafka server connection issues,

try: 
- open "server.properties" file that kafka uses to start the server
- find "listeners = PLAINTEXT://your.host.name:9092" and uncomment it



