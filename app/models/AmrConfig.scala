package models

import models._
import play.api.Play.current
import play.api.data._
import play.api.data.Forms._
import play.api.libs.json.Json
import play.api.libs.json.Reads
import play.api.libs.json.Writes
import play.api.libs.json.JsPath
import java.sql.Connection

import play.api.libs.json._
import play.api.libs.functional.syntax._

object AmrConfig {

  /*
   * lookuplMap and inverseMap are used to convert from json safe variable names (no periods) to the
   * siena_config variable names stored in db in siena_config
   */
  val lookupMap = Map(
    "siena_amr_suspended_meters_load_from_cis" -> "siena_amr.suspended_meters.load_from_cis",
    "siena_amr_pmu_enable" -> "siena_amr.pmu.enable",
    "siena_amr_resolve_on_restore" -> "siena_amr.resolve_on_restore",
    "siena_amr_cis.meter_client" -> "siena_amr.cis.meter.client",
    "siena_amr_ping_action" -> "siena_amr.ping_action",
    "siena_amr_resolve_timeout" -> "siena_amr.resolve_timeout",
    "siena_amr_outage_timeout" -> "siena_amr.outage_timeout",
    "siena_trouble_amr_outage_response" -> "siena.trouble.amr.outage_response",
    "siena_trouble_amr_manual_response" -> "siena.trouble.amr.manual_response",
    "siena_trouble_amr_unsolicited_event" -> "siena.trouble.amr.unsolicited_event",
    "siena_amr_restore_ping_field" -> "siena_amr.restore_ping_field",
    "siena_amr_reping_on_outage_count" -> "siena_amr.reping_on_outage_count",
    "siena_amr_reping_window" -> "siena_amr.reping_window",
    "siena_amr_reping_delay" -> "siena_amr.reping_delay",
    "siena_amr_HTTP_Server" -> "siena_amr.HTTP.Server",
    "siena_amr_HTTP_Path" -> "siena_amr.HTTP.Path",
    "siena_amr_oa_version" -> "siena_amr.oa_version",
    "siena_amr_oa_response_url" -> "siena_amr.oa_response_url",
    "od_event_disable_tag_level" -> "od_event.disable.tag_level",
    "oa_od_client" -> "oa_od.client",
    "siena_amr_cause_code_ignore" -> "siena_amr.cause_code.ignore",
    "siena_amr_MultispeakMsgHeader_Version" -> "siena_amr.MultispeakMsgHeader.Version",
    "siena_amr_MultispeakMsgHeader_UserID" -> "siena_amr.MultispeakMsgHeader.UserID",
    "siena_amr_MultispeakMsgHeader_Pwd" -> "siena_amr.MultispeakMsgHeader.Pwd",
    "siena_amr_MultispeakMsgHeader_AppName" -> "siena_amr.MultispeakMsgHeader.AppName",
    "siena_amr_MultispeakMsgHeader_AppVersion" -> "siena_amr.MultispeakMsgHeader.AppVersion",
    "siena_amr_MultispeakMsgHeader_Company" -> "siena_amr.MultispeakMsgHeader.Company",
    "siena_amr_MultispeakMsgHeader_CSUnits" -> "siena_amr.MultispeakMsgHeader.CSUnits",
    "siena_amr_MultispeakMsgHeader_CoordinateSystem" -> "siena_amr.MultispeakMsgHeader.CoordinateSystem",
    "siena_amr_MultispeakMsgHeader_Datum" -> "siena_amr.MultispeakMsgHeader.Datum",
    "siena_amr_tools_wallet_auth" -> "siena_amr.tools.wallet.auth",
    "siena_amr_tools_wallet" -> "siena_amr.tools.wallet");

  lazy val inverseMap = lookupMap map { x => x._2 -> x._1 }

  /*
   * convenience : laods data into Form and returns
   * a json value
   */
  def load(connection: Connection): JsValue = {
    def getValue(c: Seq[SienaConfig], name: String) =
      c.filter(c => c.item.equals(name)).lift(0).map(_.getValueString()).getOrElse("---")

    def getValueInt(c: Seq[SienaConfig], name: String): Option[Int] = {
      c.filter(c => c.item.equals(name)).lift(0).map(_.getValue().map(v => v.toInt)).getOrElse(None)
    }

    // retrieve all the config items specified in the lookup map
    val configItems = models.SienaConfig.getConfigItems(connection, lookupMap.map { y => y._2 }.asInstanceOf[List[String]])
    
    // create json for the config items that have single values
    val mapSingle = SienaConfig.mapConfigToJson(configItems)
    val jsonSingle = Json.toJson(mapSingle)
        
    // create json arrays for config items that have multiple values
    val mapMultiple = SienaConfig.mapMultipleConfigToJson(configItems)
    val jsonMultiple = Json.toJson(mapMultiple map { y => inverseMap.getOrElse(y._1, "None") -> y._2 })

    // merge the two jsons and return
    jsonSingle.as[JsObject] ++ jsonMultiple.as[JsObject]
  }

  /*
   * update a single siena_config item in the database.
   * TODO : does not support multiple config types yet
   */
  def update(connection: Connection, item: String, value: String) = {
    val s = connection.prepareStatement("{call updateConfig(?, ?)}")
    s.setString(1, item)
    s.setString(2, value)
    s.execute()
  }

  /*
   * given a json string, will translate and save it to siena_config
   */
  def save(connection: Connection, json: JsValue) {
    lookupMap map { m =>
      val value = (json \ m._1).asOpt[String]
      if (value.isDefined)
        update(connection, m._2, value.get)

    }
  }
}