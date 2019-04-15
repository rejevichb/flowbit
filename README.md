# `Flowbit `
##### Daniele Zahn | Gabriel Centeno | Matt Martin | Parker Griep | Brendan Rejevich 

Please read this file in it's entirety before attempting to execute any code. 

## Description

`Flowbit` is a data pipeline generator that allows end users to specify a variety of data sources
and destinations, as well as a series of operations that the user wants to perform before the
data reaches the destination. The user specifies these sources, destinations, and operations in 
a custom config file. The user must first start ``Apache Zookeeper`` and then ``Apache Kafka`` in 
that order (and when finished shut down in the reverse order). The user then runs the program 

> ``$     ./flowbit.py <config-file-location>``

to start `Flowbit` and perform the transformations specified in the file. The program will run until 
instructed to shut down. Provided with the submission are several sample config files. To run the config
files, please see the appendix. 

<img width="980" alt="Screen Shot 2019-04-15 at 10 15 40 AM" src="https://user-images.githubusercontent.com/31457882/56140006-01073d80-5f68-11e9-9d9f-197b2e16a1e2.png">


#### Motivations
This enables easy and accessible integration and transformation operations on complex
data sets and simplifies an otherwise technical process that not all end users may have the competencies to 
execute. Flowbit does all the heavy lifting - importing data into Kafka in a 




### Configuration File Syntax

>>TODO: analysis of performance/capacity, in records per second - perform analysis




## Analysis 

- 
- 
- 


## Appendix


#### System Requirements
- Java 8
- Scala SDK 2.12.8
- Apache Zookeeper
- Apache Kafka


####  Desired (but unimplemented) improvements
- GUI that will allow users to graphically create pipeline and generate a config file
- More data sources, specifically: 
    - Mongo DB 
    - Redis 
    - Spark 
    - Oracle 
    - Salesforce 
    - AWS storage 
    - Google Cloud Storage 
    - Live Feed APIs 
- More performance optimizations

    

 #### Libraries and Dependencies

 - org.apache.kafka:kafka-clients:2.1.1
 - org.apache.kafka:kafka-streams-scala_2.11:2.1.1
 - org.slf4j:slf4j-api:1.8.0-beta4
 - org.slf4j:slf4j-simple:1.8.0-beta4



 ## Troubleshooting
Connection issues:

In case of kafka server connection issues,

try: 
- open "server.properties" file that kafka uses to start the server
- find "listeners = PLAINTEXT://your.host.name:9092" and uncomment it

## Running Example Configurations

#### MySQL Song Database Example
Included in the submission is a mysql database titled songs. 


#### CSV Flight Data Example
