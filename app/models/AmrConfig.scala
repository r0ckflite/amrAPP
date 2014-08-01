package models

import play.api.Play.current
import play.api.data._
import play.api.data.Forms._
import play.api.data.format.Formats._
import models._

case class AmrConfig1(
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
  repingWindow: String,
  eventDuplicateCount: String)

case class AmrConfig2(
  pmu_enable: String,
  resolve_on_restore: String,
  cis_meter_client: String,
  ping_action: String,
  resolve_timeout: String,
  outage_timeout: String,
  restore_ping_field: String,
  http_server: String,
  http_path: String,
  oa_response_url: String)

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
  lazy val amrConfigForm1 = Form(
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
      "repingWindow" -> text,
      "eventDuplicateCount" -> text)(AmrConfig1.apply)(AmrConfig1.unapply))

  lazy val amrConfigForm2 = Form(
    mapping(
      "pmu_enable" -> text,
      "resolve_on_restore" -> text,
      "cis_meter_client" -> text,
      "ping_action" -> text,
      "resolve_timeout" -> text,
      "outage_timeout" -> text,
      "restore_ping_field" -> text,
      "http_server" -> text,
      "http_path" -> text,
      "oa_response_url" -> text)(AmrConfig2.apply)(AmrConfig2.unapply))
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
    val eventDuplicateCount = getValue(c, "siena_amr.event_duplicate.timeout")
    val pmu_enable = getValue(c, "siena_amr.pmu.enable")
    val resolve_on_restore = getValue(c, "siena_amr.resolve_on_restore")
    val cis_meter_client = getValue(c, "siena_amr.cis.meter.client")
    val ping_action = getValue(c, "siena_amr.ping_action")
    val resolve_timeout = getValue(c, "siena_amr.resolve_timeout")
    val outage_timeout = getValue(c, "siena_amr.outage_timeout")
    val restore_ping_field = getValue(c, "siena_amr.restore_ping_field")
    val http_server = getValue(c, "siena_amr.HTTP.Server")
    val http_path = getValue(c, "siena_amr.HTTP.Path")
    val oa_response_url = getValue(c, "siena_amr.oa_response_url")
    AmrConfig.amrConfigForm1.fill(AmrConfig1(oaVersion, unsolicitedEvents, outageResponse, manualResponse,
      validateDups, validateEventDate, disableTagLevel, suspendedLoadCIS,
      outageEventEnable, restoreEventEnable, repingOnOutage, repingDelay,
      repingWindow, eventDuplicateCount))

//    AmrConfig.amrConfigForm2.fill(AmrConfig2(pmu_enable, resolve_on_restore,
//      meter_client, ping_action, resolve_timeout, outage_timeout,
//      restore_ping_field, http_server, http_path, oa_response_url))
  }

  /*
   * Saves each piece of form data
   * 
   * TODO: Create an DAO
   */
  def saveForm(connection: java.sql.Connection, formData: AmrConfig1, formData2: AmrConfig2) = {
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
    update("siena_amr.event_duplicate.timeout", formData.eventDuplicateCount)
    update("siena_amr.pmu.enable", formData2.pmu_enable)
    update("siena_amr.resolve_on_restore", formData2.resolve_on_restore)
    update("siena_amr.cis.meter.client", formData2.cis_meter_client)
    update("siena_amr.ping_action", formData2.ping_action)
    update("siena_amr.resolve_timeout", formData2.resolve_timeout)
    update("siena_amr.outage_timeout", formData2.outage_timeout)
    update("siena_amr.restore_ping_field", formData2.restore_ping_field)
    update("siena_amr.HTTP.Server", formData2.http_server)
    update("siena_amr.HTTP.Path", formData2.http_path)
    update("siena_amr.oa_response_url", formData2.oa_response_url)

  }

}