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

case class TroubleType(
  var code: String,
  var light: String,
  var wire: String,
  var transformer: String,
  var pole: String,
  var tree: String,
  var extent: String,
  var doNotAnalyze: Option[String],
  var momentaryOutage: Option[String]) {

  override def toString(): String = {
    s"coee : $code, light: $light, wire: $wire, transformer: $transformer, pole: $pole, tree: $tree, extent: $extent, doNotAnalyze: $doNotAnalyze, momentaryOutage: $momentaryOutage"
  }
}

object TroubleType {

  val troubleType = {
    get[String]("code") ~ get[String]("light") ~ get[String]("wire") ~ get[String]("transformer") ~
    get[String]("pole") ~ get[String]("tree") ~ get[String]("extent") ~ get[Option[String]]("do_not_analyze") ~
    get[Option[String]]("momentary_outage") map {
      case code ~ light ~ wire ~ transformer ~ pole ~ tree ~ extent ~ doNotAnalyze ~ momentaryOutage =>
        TroubleType(code, light, wire, transformer, pole, tree, extent, doNotAnalyze, momentaryOutage)
    }
  }

  def getAllTroubleTypeTypes(implicit conn: Connection, codes: Seq[String]): List[TroubleType] = {       
    val query = s"select code, light, wire, transformer, pole, tree, extent, do_not_analyze, momentary_outage from astorm2.trouble_type_code_t where code in " + codes.mkString("('", "','", "')")
    SQL(query).as(troubleType *)
  }
  
  def getAllActiveTroubleTypes(implicit conn: Connection): List[TroubleType] = {       
    val query = s"select code, light, wire, transformer, pole, tree, extent, do_not_analyze, momentary_outage from astorm2.trouble_type_code_t where code in (select trouble_type_code from siena_web_services.problem_code)"
    SQL(query).as(troubleType *)
  }
}


    

