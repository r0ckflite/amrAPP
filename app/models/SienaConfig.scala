package models;

import anorm._
import anorm.SqlParser._
import java.math.BigDecimal
import org.joda.time.DateTime

object SienaConfig {

  def apply(item: String): SienaConfig = {
    new SienaConfig(item, None, None, None, None)
  }

  def apply(item: String, defaultValue: Option[String], value: Option[String], description: Option[String], multiplesAllowed: Option[BigDecimal]): SienaConfig = {
    new SienaConfig(item, defaultValue, value, description, multiplesAllowed)
  }
}

class SienaConfig(
  var item: String,
  var defaultValue: Option[String],
  var value: Option[String],
  var description: Option[String],
  var multiplesAllowed: Option[BigDecimal])
    

