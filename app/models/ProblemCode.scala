package models;

import java.math.BigDecimal
import java.sql.Connection
import anorm._
import anorm.SqlParser._
import play.api.Play.current
import play.api.data.Forms._
import play.api.data.format.Formats._
import java.util.UUID
import java.sql.SQLException
import java.sql.PreparedStatement
import global._

case class ProblemCode(
  var problem_code_key: Option[String],
  var problem_code: String,
  var trouble_type_code: String,
  var source: String,
  var event_type: String) {

  override def toString(): String = {
    s"key: $problem_code_key, problem_code: $problem_code, trouble_type_code: $trouble_type_code, source: $source, event_type: $event_type"
  }
}

object ProblemCode {

  val problemCode = {
    get[Option[String]]("problem_code_key") ~
      get[String]("problem_code") ~
      get[String]("trouble_type_code") ~
      get[String]("source") ~
      get[String]("event_type") map {
        case problem_code_key ~ problem_code ~ trouble_type_code ~ source ~ event_type => ProblemCode.apply(problem_code_key, problem_code, trouble_type_code, source, event_type)
      }
  }

  def getProblemCodeCount(implicit conn: Connection): Int = {
    val firstRow = SQL("select count(*) as count from siena_web_services.problem_code").apply().head
    val count = firstRow[BigDecimal]("count").intValue()
    count
  }

  def count(login: String, code: ProblemCode): Long = {
    JDBC.withConnection(login) { implicit connection =>
      val query = "select count(*) as c from siena_web_services.problem_code where problem_code||trouble_type_code||source||event_type = '" + code.problem_code_key.getOrElse("NONE") + "'"
      val firstRow = SQL(query).apply().head
      return firstRow[Long]("c")
    }
    0
  }

  def count(implicit conn: Connection, code: ProblemCode): Long = {
    val query = "select count(*) as c from siena_web_services.problem_code where problem_code||trouble_type_code||source||event_type = '" + code.problem_code_key.getOrElse("NONE") + "'"
    println("DEBUG : saveRow.count : " + query)

    var stmt: PreparedStatement = null
    try {
      stmt = conn.prepareStatement(query)
      val rs = stmt.executeQuery()
      rs.next
      val c = rs.getLong("c")
      println("DEBUG : ProblemCode count returning " + c)
      return c
    } catch {
      case e: SQLException => {
        println("DEBUG : ProblemCode count exception : " + e)
        0
      }
    } finally {
      if (stmt != null) stmt.close
    }
  }

  def getAllProblemCodes(implicit conn: Connection): List[ProblemCode] = {
    val query = "select problem_code||trouble_type_code||source||event_type as problem_code_key, problem_code, trouble_type_code, source, event_type from siena_web_services.problem_code"
    val stmt = conn.prepareStatement(query)
    val rs = stmt.executeQuery()
    rs.next()

    val results = SQL(query).as(problemCode *)
    println("DEBUG : number of rows in problem_code = " + getProblemCodeCount(conn))
    println("DEBUG: getAllProblemCodes returned " + results.size + " items")
    results
  }

  def saveRow(implicit conn: Connection, code: ProblemCode) {

    if (code.problem_code_key.isDefined && !code.problem_code_key.equals("")) {
      if (count(conn, code) > 0) {
        val q = s"update problem_code set problem_code = '${code.problem_code}', source = '${code.source}', trouble_type_code = '${code.trouble_type_code}', event_type = '${code.event_type}' where problem_code||trouble_type_code||source||event_type = '${code.problem_code_key.get}'"
        println("DEBUG : saveRow update : " + q)
        SQL(q).executeUpdate
        return
      }
    }
    val q = s"insert into problem_code (problem_code, source, trouble_type_code, event_type) values ( \'${code.problem_code}\', \'${code.source}\',\'${code.trouble_type_code}\', \'${code.event_type}\')"
    println("DEBUG : saveRow insert : " + q)
    SQL(q).execute
  }

  def save(implicit conn: Connection, codes: Seq[ProblemCode]) = {
    codes.foreach({ pc =>
      saveRow(conn, pc)
    })
  }
}


    

