package flowbit.source

import java.sql.{Connection, DriverManager, SQLException}

class SQLiteSource(dbPath: String, columnNames: List[String], query: String) extends Source[Int, List[String]] {
  /** *
    * Polls the data source for records which come in the form of mappings
    * from key to value.
    *
    * @return a stream of the maps.
    */
  @throws(classOf[SQLException])
  override def poll: Stream[(Int, List[String])] = {

    var connection: Connection = null
    try { // db parameters
      val database = "jdbc:sqlite:" + dbPath
      // create a connection to the database
      connection = DriverManager.getConnection(database)
      val statement = connection.createStatement()
      val resultSet = statement.executeQuery(query)
      var result = Stream.empty[(Int, List[String])]
      var counter = 0
      while (resultSet.next()) {
        var row = List.empty[String]
        for (name <- columnNames) {
          row = row :+ (resultSet.getString(name))
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
