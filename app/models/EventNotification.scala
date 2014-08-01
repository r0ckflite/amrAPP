package models;

import java.math.BigDecimal
import java.sql.Connection
import java.util.Date

import anorm._
import anorm.SqlParser._
import play.api.Play.current
import play.api.data._
import play.api.data.Forms._
import play.api.data.format.Formats._
import play.api.libs.json.JsPath
import play.api.libs.json.JsValue
import play.api.libs.json.Json
import play.api.libs.json.Reads

import play.api._
import play.api.Play.current
import play.api.libs.functional.syntax._
import play.api.libs.json._
import play.api.mvc._
import play.api.mvc.Action
import play.api.mvc.Controller
import scala.collection.mutable.ListBuffer

class EventNotification(
  var event_notification_id: BigDecimal,
  var phasecd: Option[String],
  var eventtime: Option[Date],
  var eventtype: Option[String],
  var outagedevicetype: Option[String],
  var create_date: Option[Date],
  var meter_no: Option[String],
  var serial_number: Option[String],
  var manufacturer: Option[String],
  var serv_loc: Option[String],
  var account_number: Option[String],
  var area_code: Option[BigDecimal],
  var contact_first_name: Option[String],
  var contact_last_name: Option[String],
  var contact_middle_name: Option[String],
  var contact_callback_phone: Option[BigDecimal],
  var call_record_id: Option[String],
  var customer_id: Option[String],
  var outage_event_id: Option[String],
  var completed_time: Option[Date],
  var callback_status: Option[String],
  var callback_type: Option[String],
  var customer_time_to_call_start: Option[Date],
  var customer_time_call_end: Option[Date],
  var priority: Option[String],
  var comments: Option[String],
  var errorstring: Option[String],
  var percent_confident: Option[BigDecimal],
  var trouble_call_id: Option[Int],
  var object_id: Option[String],
  var rpt_no: Option[Int],
  var outagedeviceid: Option[String],
  var xfmr_id: Option[String],
  var phone: Option[BigDecimal],
  var contact_callback_area: Option[BigDecimal],
  var close_call_time: Option[Date],
  var close_call_status: Option[String],
  var outage_status: Option[String],
  var callback_flag: Option[String],
  var callback_attempt_status: Option[String],
  var callback_attempt_time: Option[Date],
  var problem_code: Option[String],
  var resolved_level: Option[String],
  var company: Option[String],
  var app_name: Option[String],
  var call_type: Option[String],
  var transaction_id: Option[BigDecimal],
  var source: Option[String]) {

  override def toString(): String = {
    s"event_notification_id: $event_notification_id"
  }

}

object EventNotification {

  val eventFilterForm = Form(
    tuple(
      "source" -> text,
      "eventType" -> text))

  val eventNotification = {
    get[BigDecimal]("event_notification_id") ~
      get[Option[String]]("phasecd") ~
      get[Option[Date]]("eventtime") ~
      get[Option[String]]("eventtype") ~
      get[Option[String]]("outagedevicetype") ~
      get[Option[Date]]("create_date") ~
      get[Option[String]]("meter_no") ~
      get[Option[String]]("serial_number") ~
      get[Option[String]]("manufacturer") ~
      get[Option[String]]("serv_loc") ~
      get[Option[String]]("account_number") ~
      get[Option[BigDecimal]]("area_code") ~
      get[Option[String]]("contact_first_name") ~
      get[Option[String]]("contact_last_name") ~
      get[Option[String]]("contact_middle_name") ~
      get[Option[BigDecimal]]("contact_callback_phone") ~
      get[Option[String]]("call_record_id") ~
      get[Option[String]]("customer_id") ~
      get[Option[String]]("outage_event_id") ~
      get[Option[Date]]("completed_time") ~
      get[Option[String]]("callback_status") ~
      get[Option[String]]("callback_type") ~
      get[Option[Date]]("customer_time_to_call_start") ~
      get[Option[Date]]("customer_time_call_end") ~
      get[Option[String]]("priority") ~
      get[Option[String]]("comments") ~
      get[Option[String]]("errorstring") ~
      get[Option[BigDecimal]]("percent_confident") ~
      get[Option[Int]]("trouble_call_id") ~
      get[Option[String]]("object_id") ~
      get[Option[Int]]("rpt_no") ~
      get[Option[String]]("outagedeviceid") ~
      get[Option[String]]("xfmr_id") ~
      get[Option[BigDecimal]]("phone") ~
      get[Option[BigDecimal]]("contact_callback_area") ~
      get[Option[Date]]("close_call_time") ~
      get[Option[String]]("close_call_status") ~
      get[Option[String]]("outage_status") ~
      get[Option[String]]("callback_flag") ~
      get[Option[String]]("callback_attempt_status") ~
      get[Option[Date]]("callback_attempt_time") ~
      get[Option[String]]("problem_code") ~
      get[Option[String]]("resolved_level") ~
      get[Option[String]]("company") ~
      get[Option[String]]("app_name") ~
      get[Option[String]]("call_type") ~
      get[Option[BigDecimal]]("transaction_id") ~
      get[Option[String]]("source") map {
        case event_notification_id ~ phasecd ~ eventtime ~
          eventtype ~
          outagedevicetype ~
          create_date ~
          meter_no ~
          serial_number ~
          manufacturer ~
          serv_loc ~
          account_number ~
          area_code ~
          contact_first_name ~
          contact_last_name ~
          contact_middle_name ~
          contact_callback_phone ~
          call_record_id ~
          customer_id ~
          outage_event_id ~
          completed_time ~
          callback_status ~
          callback_type ~
          customer_time_to_call_start ~
          customer_time_call_end ~
          priority ~
          comments ~
          errorstring ~
          percent_confident ~
          trouble_call_id ~
          object_id ~
          rpt_no ~
          outagedeviceid ~
          xfmr_id ~
          phone ~
          contact_callback_area ~
          close_call_time ~
          close_call_status ~
          outage_status ~
          callback_flag ~
          callback_attempt_status ~
          callback_attempt_time ~
          problem_code ~
          resolved_level ~
          company ~
          app_name ~
          call_type ~
          transaction_id ~
          source => new EventNotification(event_notification_id, phasecd, eventtime,
          eventtype,
          outagedevicetype,
          create_date,
          meter_no,
          serial_number,
          manufacturer,
          serv_loc,
          account_number,
          area_code,
          contact_first_name,
          contact_last_name,
          contact_middle_name,
          contact_callback_phone,
          call_record_id,
          customer_id,
          outage_event_id,
          completed_time,
          callback_status,
          callback_type,
          customer_time_to_call_start,
          customer_time_call_end,
          priority,
          comments,
          errorstring,
          percent_confident,
          trouble_call_id,
          object_id,
          rpt_no,
          outagedeviceid,
          xfmr_id,
          phone,
          contact_callback_area,
          close_call_time,
          close_call_status,
          outage_status,
          callback_flag,
          callback_attempt_status,
          callback_attempt_time,
          problem_code,
          resolved_level,
          company,
          app_name,
          call_type,
          transaction_id,
          source)
      }
  }

  def eventsToJson(events: Seq[EventNotification]): JsValue = {
    val ev = events.map(e => eventToJson(e))
    Json.toJson(ev)
  }

  def eventToJson(event: EventNotification): JsValue = {
    Json.toJson(Map[String, String]("event_notification_id" -> event.event_notification_id.toString,
      "phasecd" -> event.phasecd.getOrElse(""),
      "eventtime" -> event.eventtime.getOrElse("").toString,
      "eventtype" -> event.eventtype.getOrElse(""),
      "outagedevicetype" -> event.outagedevicetype.getOrElse(""),
      "create_date" -> event.create_date.getOrElse("").toString,
      "meter_no" -> event.meter_no.getOrElse(""),
      "serial_number" -> event.serial_number.getOrElse(""),
      "manufacturer" -> event.manufacturer.getOrElse(""),
      "serv_loc" -> event.serv_loc.getOrElse(""),
      "account_number" -> event.account_number.getOrElse(""),
      "area_code" -> event.area_code.getOrElse("").toString,
      "contact_first_name" -> event.contact_first_name.getOrElse(""),
      "contact_last_name" -> event.contact_last_name.getOrElse(""),
      "contact_middle_name" -> event.contact_middle_name.getOrElse(""),
      "contact_callback_phone" -> event.contact_callback_phone.getOrElse("").toString,
      "call_record_id" -> event.call_record_id.getOrElse(""),
      "customer_id" -> event.customer_id.getOrElse(""),
      "outage_event_id" -> event.outage_event_id.getOrElse(""),
      "completed_time" -> event.completed_time.getOrElse("").toString,
      "callback_status" -> event.callback_status.getOrElse(""),
      "callback_type" -> event.callback_type.getOrElse(""),
      "customer_time_to_call_start" -> event.customer_time_to_call_start.getOrElse("").toString,
      "customer_time_call_end" -> event.customer_time_call_end.getOrElse("").toString,
      "priority" -> event.priority.getOrElse(""),
      "comments" -> event.comments.getOrElse(""),
      "errorstring" -> event.errorstring.getOrElse(""),
      "percent_confident" -> event.percent_confident.getOrElse("").toString,
      "trouble_call_id" -> event.trouble_call_id.getOrElse("").toString,
      "object_id" -> event.object_id.getOrElse(""),
      "rpt_no" -> event.rpt_no.getOrElse("").toString,
      "outagedeviceid" -> event.outagedeviceid.getOrElse(""),
      "xfmr_id" -> event.xfmr_id.getOrElse(""),
      "phone" -> event.phone.getOrElse("").toString,
      "contact_callback_area" -> event.contact_callback_area.toString,
      "close_call_time" -> event.close_call_time.getOrElse("").toString,
      "close_call_status" -> event.close_call_status.getOrElse(""),
      "outage_status" -> event.outage_status.getOrElse(""),
      "callback_flag" -> event.callback_flag.getOrElse(""),
      "callback_attempt_status" -> event.callback_attempt_status.getOrElse(""),
      "callback_attempt_time" -> event.callback_attempt_time.getOrElse("").toString,
      "problem_code" -> event.problem_code.getOrElse(""),
      "resolved_level" -> event.resolved_level.getOrElse(""),
      "company" -> event.company.getOrElse(""),
      "app_name" -> event.app_name.getOrElse(""),
      "call_type" -> event.call_type.getOrElse(""),
      "transaction_id" -> event.transaction_id.getOrElse("").toString,
      "source" -> event.source.getOrElse("")))
  }

  def getAllEventNotifications(implicit conn: Connection, maxRows: Integer, filter: Option[Map[String, String]]): List[EventNotification] = {
    var queryString = """select event_notification_id,  phasecd, eventtime,eventtype, outagedevicetype, create_date,
meter_no, serial_number, manufacturer, serv_loc, account_number, area_code,
contact_first_name, contact_last_name, contact_middle_name, contact_callback_phone, 
call_record_id, customer_id, outage_event_id, completed_time, callback_status,
callback_type, customer_time_to_call_start, customer_time_call_end, priority, comments,
errorstring, percent_confident, trouble_call_id, object_id, rpt_no,
outagedeviceid, xfmr_id, phone, contact_callback_area, close_call_time,
close_call_status, outage_status, callback_flag, callback_attempt_status, 
callback_attempt_time, problem_code, resolved_level, company, app_name,
call_type, transaction_id, source from event_notifications"""

    var query: SqlQuery = null
    queryString += s" where rownum < $maxRows "

    /*
     * filter the results
     */
    if (filter.isDefined && filter.get.size > 0) {
      filter.get.foreach(f => {
        queryString += s" and ${f._1} = \'${f._2}\'"
      })

      queryString += " order by event_notification_id desc"

      println("DEBUG : EventNotication create select query = " + queryString)

      query = SQL(queryString)
      val results = query.as(eventNotification *)
      println("DEBUG: getAllEventNotificationss returned " + results.size + " items")

      return results
    } else {
      queryString += " order by event_notification_id desc"
      query = SQL(queryString)
      val results = query.as(eventNotification *)
      println("DEBUG: getAllEventNotificationss returned " + results.size + " items")
      results
    }
  }
}


    

