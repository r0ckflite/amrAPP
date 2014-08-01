package models;

import anorm._
import anorm.SqlParser._
import java.math.BigDecimal
import org.joda.time.DateTime
import play.api.db.DB
import play.api.Play.current
import global._
import java.sql.Connection
import java.math.BigInteger

object CallerType {

  def apply(caller_type_key: BigDecimal): CallerType = {
    new CallerType(caller_type_key, None)
  }

  def apply(caller_type_key: BigDecimal, caller_type: Option[String]): CallerType = {
    new CallerType(caller_type_key, caller_type)
  }

  val callerType = {
    get[BigDecimal]("caller_type_key") ~
      get[Option[String]]("caller_type") map {
        case caller_type_key ~ caller_type => CallerType.apply(caller_type_key,
          caller_type)
      }
  }

  def getAllCallertypes(implicit conn: Connection): List[CallerType] = {
    val query = "select caller_type_key, caller_type from astorm2.caller_type"
    SQL(query).as(callerType *)
  }


  def getCallerTypes(implicit conn: Connection, callerTypes: Seq[String]): List[CallerType] = {
    val inClause = callerTypes.mkString("'", "','", "'") // gives us 'foo','bar','xyz'
    val query = s"select caller_type_key, caller_type from astorm2.caller_type where caller_type in ($inClause)"
    SQL(query).as(callerType *)
  }
  
  def addCallerType(implicit conn: Connection, value: String) {
    // todo : configurable for later schemas
    val firstRow = SQL(s"select count(*) as count from astorm2.caller_type where caller_type = \'$value\'").apply().head
    val count = firstRow[BigDecimal]("count").intValue()
    println(s"*** DEBUG : checking caller type $value exists. " + (if( count > 0 ) "Exists" else "Does not exist"))
    if( count < 1 ) {

      val query = s"insert into astorm2.caller_type (caller_type_key, caller_type) values ((select max(caller_type_key)+1 from astorm2.caller_type), \'$value\')"
      println(s"*** DEBUG : Inserting caller type $value, $query")
      SQL(query).execute    
    }
  }

}

class CallerType(
  var caller_type_key: BigDecimal,
  var caller_type: Option[String]) {

  override def toString(): String = {
    "caller_type_key: " + caller_type_key + ", caller_type: " + caller_type
  }
}
    

