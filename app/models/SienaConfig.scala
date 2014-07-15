package models;

import anorm._
import anorm.SqlParser._
import java.math.BigDecimal
import org.joda.time.DateTime
import play.api.db.DB
import play.api.Play.current
import global._
import java.sql.Connection

object SienaConfig {

  def apply(item: String): SienaConfig = {
    new SienaConfig(item, None, None, None, None)
  }

  def apply(item: String, defaultValue: Option[String], value: Option[String], description: Option[String], multiplesAllowed: Option[BigDecimal]): SienaConfig = {
    new SienaConfig(item, defaultValue, value, description, multiplesAllowed)
  }

  val config = {
    get[String]("item") ~
      get[Option[String]]("default_value") ~
      get[Option[String]]("value") ~
      get[Option[String]]("description") ~
      get[Option[BigDecimal]]("multiples_allowed") map {
        case item ~ default_value ~ value ~ description ~ multiples_allowed => SienaConfig.apply(item,
          default_value, value, description, multiples_allowed)
      }
  }

  def configs2(implicit conn: Connection) : List[SienaConfig] = {
    SQL("select item, default_value, value, description, multiples_allowed from siena.siena_config").as(config *)
  }
}

class SienaConfig(
  var item: String,
  var defaultValue: Option[String],
  var value: Option[String],
  var description: Option[String],
  var multiplesAllowed: Option[BigDecimal]) {
  
  override def toString(): String = {
    "item: " + item + ", defaultValue: " + defaultValue + ", value: " + value + ", description: " + description + ", multiplesAllowed: " + multiplesAllowed
  }
}
    

