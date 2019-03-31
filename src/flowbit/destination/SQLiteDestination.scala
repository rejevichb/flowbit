package flowbit.destination

import java.sql.{Connection, DriverManager, PreparedStatement, SQLException}

class SQLiteDestination(dbPath :String, tableName :String) extends Destination[Int, Map[String,String]] {

  var connection: Connection = DriverManager.getConnection("jdbc:sqlite:" + dbPath)
  var inserted = false
  var ps: PreparedStatement = null

  override def record(data: (Int, Map[String,String])): Boolean = {
    try {
      val statement = connection.createStatement()

      if (!inserted) {
        statement.execute("drop table if exists " + tableName + ";")
        val table_headers = data._2.map(_._1 + " text not null").reduce(_ + ", " + _)
        statement.execute("create table " + tableName + "(" + table_headers + ");")
        inserted = true
      }

      val values = data._2.values.map("\"" + _ + "\"").reduce(_ + ", " + _)
      val query = "insert into " + tableName + " values (" + values + ");"

      println(query)
      statement.execute(query)
      return true
    } catch {
      case e: SQLException => throw e
    }
    return false
  }

  sys.ShutdownHookThread {
    connection.close()
  }
}
