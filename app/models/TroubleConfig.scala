package models

import play.api.Play.current
import play.api.data._
import play.api.data.Forms._
import play.api.data.format.Formats._
import models._
import java.sql.Connection

case class TroubleConfig(
  callerType: String)

object TroubleConfig {
  val s1 = Some(Seq("Outage", "Restoration", "Both"))
  val s2 = Some(Seq("True", "False"))

  case class elements(widgetType: String, name: String, label: String, values: Option[Seq[String]])

  /*
   * Mapping for loading the form and reading from the form
   * after submit
   */
  lazy val troubleConfigForm = Form(
    mapping(
      "callerType" -> text)(TroubleConfig.apply)(TroubleConfig.unapply))

  /*
   * convenience : laods data into Form and returns the form
   */
  def loadForm(c: Seq[SienaConfig]) = {
    def getValue(c: Seq[SienaConfig], name: String) =
      c.filter(c => c.item.equals(name)).lift(0).map(_.getValueString()).getOrElse("---")

    def getValueInt(c: Seq[SienaConfig], name: String): Option[Int] = {
      c.filter(c => c.item.equals(name)).lift(0).map(_.getValue().map(v => v.toInt)).getOrElse(None)
    }

    val callerType = getValue(c, "siena.trouble.caller_type")

    TroubleConfig.troubleConfigForm.fill(TroubleConfig(callerType))
  }
  
  

  /*
   * Saves each piece of form data
   * 
   * TODO: Create an DAO
   */
  def saveForm(connection: java.sql.Connection, formData: TroubleConfig) = {
    def update(item: String, value: String) = {
      val s = connection.prepareStatement("{call updateConfig(?, ?)}")
      s.setString(1, item)
      s.setString(2, value)
      s.execute()
    }
    
    update("siena.trouble.caller_type", formData.callerType)
  }
}