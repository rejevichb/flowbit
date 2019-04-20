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
This enables easy and accessible integration and transformation operations on complex
data sets and simplifies an otherwise technical process that not all end users may have the competencies to 
execute. Flowbit does all the heavy lifting - importing data into Kafka in a 




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
