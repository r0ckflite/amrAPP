package models

import play.api.Play.current
import play.api.data._
import play.api.data.Forms._
import play.api.data.format.Formats._

case class AmrConfig(
  oaVersion: String,
  unsolicitedEvents: String,
  outageResponse: String,
  manualResponse: String,
  validateDups: Boolean,
  validateEventDate: Boolean,
  disableTagLevel: Int,
  suspendedLoadCIS: Boolean,
  outageEventEnable: Boolean,
  restoreEventEnable: Boolean,
  repingOnOutage: Int,
  repingDelay: Int,
  repingWindow: Int)

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
      "validateDups" -> boolean,
      "validateEventDate" -> boolean,
      "disableTagLevel" -> number,
      "suspendedLoadCIS" -> boolean,
      "outageEventEnable" -> boolean,
      "restoreEventEnable" -> boolean,
      "repingOnOutage" -> number,
      "repingDelay" -> number,
      "repingWindow" -> number)(AmrConfig.apply)(AmrConfig.unapply))
}