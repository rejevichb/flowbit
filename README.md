# `Flowbit `
##### Daniele Zhan | Gabriel Centeno | Matt Martin | Parker Griep | Brendan Rejevich 

Please read this file in it's entirety before attempting to execute any code. 

## Description

`Flowbit` is a data pipeline generator that allows end users to specify a variety of data sources
and destinations, as well as a series of operations that the user wants to perform before the
data reaches the destination. The user specifies these sources, destinations, and operations in 
a custom config file. 

<img width="1317" alt="Screen Shot 2019-04-15 at 10 15 22 AM" src="https://user-images.githubusercontent.com/31457882/56140081-27c57400-5f68-11e9-8d0f-0474126d3816.png">

The user must first start ``Apache Zookeeper`` and then ``Apache Kafka`` in 
that order (and when finished shut down in the reverse order). The user then runs the program 

> ``$     ./flowbit.py <config-file-location>``

to start `Flowbit` and perform the transformations specified in the file. The program will run until 
instructed to shut down. Provided with the submission are several sample config files. To run the config
files, please see the appendix. 

<img width="980" alt="Screen Shot 2019-04-15 at 10 15 40 AM" src="https://user-images.githubusercontent.com/31457882/56140006-01073d80-5f68-11e9-9d9f-197b2e16a1e2.png">


#### Motivations
We wanted to build a tool that would allow non-technical users to format and perform queries on large or even real-time data sets easily. The config file set up that we have allows for easy integration of the predicates and "blocks" you want the data to flow through while being transformed and in the future we would like to make it into a GUI for even easier interaction. Allowing users to only read subsections of their data was also a driver for this project because most of the time your application or implementation only requires some specific data, and using Kafka topics and consumers enables this functionality. Abstracting ETL pipelines for users is something that is in very high demand in industry so we wanted to see how viable it would be to build within a short timeframe given what we had learned in class, and maybe even bring this knowledge then to our co-ops or full time work later. 




### Configuration File Syntax

- The program is started using a configuration file that defines how the pipeline will be built. All arguments on the lines defined below should be seperated with a space not a comma.
- Every line that begins with '//' is taken is a comment and can be included by the user for more context.

- There should be one line that begins with the keyword "topics:" and then followed with the user defined topics 
   square brackets like so [topic1, topic2]
   
- There should then be 3 lines defining the source information: the first called "source:" followed either a sqlite .db file or a csv file like so "songs.db". Then a line called "sourcecol:" which contains the name of the table as the first argument if it's a .db file and then the columns in the file inside brackets like so [col1, col1]. Finally there should be a line called "producer:" which has the name of the producer followed by the streams it will be connected to in brackets like so [filter1]

- From here you can begin to set up your stream components - we currently only support filters and maps. You can have as manty of these in any order you want so long as they dump and read from the correct topics so that your data flows in the right order.

- Filter lines should be called out with "filter:" followed by the name of the filter, the topic it reads from, the following    topic it should dump in square brackets like so [topic1], and the predicate you wish to use denoted by "pred:". Column names should have $ in front of them and then you can use any arithmetic operations on those variables.

- Map lines will have the same order values as the filter line but the predicate is denoted by "func:" and you can reassign column values in it - there is an example of how this is done the config.txt file in the repo.

- Finally, you need to set up a dump site denoted by "destination:" and followed by either a database file and the table name ex. - "songs.db done" or a csv file ex. - "songs.csv". There also needs to be a "consumer:" line with the consumer name, the topic that it reads from, and the group it's assigned to - that can be any group defined be the user.


## Analysis 

- Implementing the Kafka producer, topics, streams, and producers wasn't as tough as we thougt it would be after spending some time reading the documentation. Writing Scala code with stricter typing than Java and using the less robust Scala Kafka API was the harder portion of this task but nothing we coulnd't tackle as a team.
- The overall flowbit object implementation was a big design decision because we needed to build out every part of the pipeline in order so that Kafka connect all of the components correctly.
- The parser for the config file was the toughest implementation detial we had to deal with; first we had to define what the optimal line format would be for each of the components so that they would be easy to parse and then we had to convert them into the objects we needed to pass to the flowbit object. We put all of the parsed config file information into a LinkedHashMap to perserve order for the flowbit object creation. 
- In hindsight we would have liked to spend more time on implementing other components (e.g. joins) or a GUI that outputs the config file and take metrics of where the bottlenecks are for our implementation. We also now understand why companies many times use basic series of scripts to do this data manipulation because there is a steep learning curve for understanding Kafka and how to build pipelines like these, but in the end it makes the lives of those performing these constant data manipulation operations much easier. 


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
- Additional stream capabilites like joins
- More performance optimizations

    

 #### Libraries and Dependencies

 - org.apache.kafka:kafka-clients:2.1.1
 - org.apache.kafka:kafka-streams-scala_2.11:2.1.1
 - org.slf4j:slf4j-api:1.8.0-beta4
 - org.slf4j:slf4j-simple:1.8.0-beta4



 ## Troubleshooting
Connection issues:

In case of kafka server connection issues try: 
- opening the "server.properties" file that kafka uses to start the server
- find "listeners = PLAINTEXT://your.host.name:9092" and uncomment it

## Running Example Configurations

#### MySQL Song Database Example
Included in the submission is a sqlite database file titled songs.db with a table called songs. Feel free to load in your own database file and change the config.txt accordingly. 
