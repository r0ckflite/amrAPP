package models

import play.api.Play.current
import play.api.data._
import play.api.data.Forms._
import play.api.data.format.Formats._
import models._

case class AmrConfig(
  oaVersion: String,
  unsolicitedEvents: String,
  outageResponse: String,
  manualResponse: String,
  validateDups: String,
  validateEventDate: String,
  disableTagLevel: String,
  suspendedLoadCIS: String,
  outageEventEnable: String,
  restoreEventEnable: String,
  repingOnOutage: String,
  repingDelay: String,
  repingWindow: String)

object AmrConfig {
  val s1 = Some(Seq("Outage", "Restoration", "Both"))
  val s2 = Some(Seq("True", "False"))

  case class elements(widgetType: String, name: String, label: String, values: Option[Seq[String]])

  /*
   * Tells the form how to build the widgets (dropdown or text)
   */
  val formElements: Seq[elements] = Seq(
    elements("dropdown", "oaVersion", "OA Version", Some(Seq("3j", "3x", "4"))),
    elements("dropdown", "unsolicitedEvents", "Unsolicited Events", s1),
    elements("dropdown", "outageResponse", "Outage Response", s1),
    elements("dropdown", "manualResponse", "Manual Response", s1),
    elements("dropdown", "validateDups", "Validate Dups", s2),
    elements("dropdown", "validateEventDate", "Validate Event Date", s2),
    elements("text", "disableTagLevel", "Disable Events Tag Level ID", None),
    elements("dropdown", "suspendedLoadCIS", "Suspended Load CIS", s2),
    elements("dropdown", "outageEventEnable", "Outage Event Enabled", s2),
    elements("dropdown", "restoreEventEnable", "Restore Event Enabled", s2),
    elements("dropdown", "repingOnOutage", "Reping On Outage", s2),
    elements("text", "repingDelay", "Reping Delay", None),
    elements("text", "repingWindow", "Reping Window", None))

  /*
   * Mapping for loading the form and reading from the form
   * after submit
   */
  lazy val amrConfigForm = Form(
    mapping(
      "oaVersion" -> text,
      "unsolicitedEvents" -> text,
      "outageResponse" -> text,
      "manualResponse" -> text,
      "validateDups" -> text,
      "validateEventDate" -> text,
      "disableTagLevel" -> text,
      "suspendedLoadCIS" -> text,
      "outageEventEnable" -> text,
      "restoreEventEnable" -> text,
      "repingOnOutage" -> text,
      "repingDelay" -> text,
      "repingWindow" -> text)(AmrConfig.apply)(AmrConfig.unapply))

  /*
   * convenience : laods data into Form and returns the form
   */
  def loadForm(c: Seq[SienaConfig]) = {
    def getValue(c: Seq[SienaConfig], name: String) =
      c.filter(c => c.item.equals(name)).lift(0).map(_.getValueString()).getOrElse("---")

    def getValueInt(c: Seq[SienaConfig], name: String): Option[Int] = {
      c.filter(c => c.item.equals(name)).lift(0).map(_.getValue().map(v => v.toInt)).getOrElse(None)
    }

    val oaVersion = getValue(c, "siena_amr.oa_version")
    val unsolicitedEvents = getValue(c, "siena.trouble.amr.unsolicited_event")
    val outageResponse = getValue(c, "siena.trouble.amr.outage_response")
    val manualResponse = getValue(c, "siena.trouble.amr.manual_response")
    val repingOnOutage = getValue(c, "siena_amr.reping_on_outage_count")
    val repingWindow = getValue(c, "siena_amr.reping_window")
    val repingDelay = getValue(c, "siena_amr.reping_delay")
    val validateDups = getValue(c, "siena_amr.validate_dups")
    val validateEventDate = getValue(c, "siena_amr.validate_event_date")
    val disableTagLevel = getValue(c, "siena_amr.disable.tag_level")
    val suspendedLoadCIS = getValue(c, "siena_amr.suspended_meters.load_from_cis")
    val outageEventEnable = getValue(c, "siena_amr.outage_event.enable")
    val restoreEventEnable = getValue(c, "siena_amr.restore_event.enable")
    AmrConfig.amrConfigForm.fill(AmrConfig(oaVersion, unsolicitedEvents, outageResponse, manualResponse, validateDups, validateEventDate, disableTagLevel, suspendedLoadCIS, outageEventEnable, restoreEventEnable, repingOnOutage, repingDelay, repingWindow))
  }

  /*
   * Saves each piece of form data
   * 
   * TODO: Create an DAO
   */
  def saveForm(connection: java.sql.Connection, formData: AmrConfig) = {
    def update(item: String, value: String) = {
      val s = connection.prepareStatement("{call updateConfig(?, ?)}")
      s.setString(1, item)
      s.setString(2, value)
      s.execute()
    }
    
    update("siena_amr.oa_version", formData.oaVersion)
    update("siena.trouble.amr.unsolicited_event", formData.unsolicitedEvents)
    update("siena.troube.amr.outage_response", formData.outageResponse)
    update("siena.trouble.amr.manual_response", formData.manualResponse)
    update("siena_amr.reping_on_outage_count", formData.repingOnOutage)
    update("siena_amr.reping_delay", formData.repingDelay)
    update("siena_amr.validate_dups", formData.validateDups)
    update("siena_amr.validate_event_date", formData.validateEventDate)
    update("siena_amr.suspended_meters.load_from_cis", formData.suspendedLoadCIS)
    update("siena_amr.outage_event.enable", formData.outageEventEnable)
    update("siena_amr.restore_event.enable", formData.restoreEventEnable)
  }

}