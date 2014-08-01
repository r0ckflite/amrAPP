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

  def mapConfigToJson(items : Seq[SienaConfig]) = {    
    Map(items map { sc => sc.item.replace('.', '_') -> sc.getValueString }: _*)
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

  def getAllConfigItems(implicit conn: Connection): List[SienaConfig] = {
    val query = "select item, default_value, value, description, multiples_allowed from siena.siena_config"
    SQL(query).as(config *)
  }

  def getValue(c: Seq[SienaConfig], name: String, defaultValue: String): String =
    c.filter(c => c.item.equals(name)).lift(0).map(_.getValueString()).getOrElse(defaultValue)

  def getValue(c: Seq[SienaConfig], name: String): String = getValue(c, name, "---")
  
  def getConfigItems(implicit conn: Connection, items: Seq[String]): List[SienaConfig] = {
    val inClause = items.mkString("'", "','", "'") // gives us 'foo','bar','xyz'
    val query = s"select item, default_value, value, description, multiples_allowed from siena.siena_config where item in ($inClause)"
    SQL(query).as(config *)
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

  def getValue(): Option[String] = {
    if (value.isDefined) value else defaultValue
  }

  def getValueString(): String = {
    println("DEBUG : ***** item = " + item + ", value = " + value + ", defaultValue = " + defaultValue)
    if (value.isDefined) value.get else if (defaultValue.isDefined) defaultValue.get else ""
  }
}
    

