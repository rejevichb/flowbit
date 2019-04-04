package main.scala.source

import java.sql.{Connection, DriverManager, SQLException}

class SQLiteSource(dbPath: String, columnNames: List[String], query: String) extends Source[Int, Map[String,String]] {
  /** *
    * Polls the data source for records which come in the form of mappings
    * from key to value.
    *
    * @return a stream of the maps.
    */
  @throws(classOf[SQLException])
  override def poll: Stream[(Int, Map[String,String])] = {

    var connection: Connection = null
    try { // db parameters
      val database = "jdbc:sqlite:" + dbPath
      // create a connection to the database
      connection = DriverManager.getConnection(database)
      val statement = connection.createStatement()
      val resultSet = statement.executeQuery(query)
      var result = Stream.empty[(Int, Map[String, String])]
      var counter = 0
      while (resultSet.next()) {
        var row = Map.empty[String, String]
        for (name <- columnNames) {
          row += (name -> resultSet.getString(name))
        }
        result = result :+ ((counter) -> (row))
        counter += 1
      }
      connection.close()
      return result
    } catch {
      case e: SQLException =>
        throw e
    } finally try
        if (connection != null) connection.close
    catch {
      case ex: SQLException =>
        throw ex
    }
  }
}
