package controllers

import Application._
import global._
import models._
import play.api._
import play.api.Play.current
import play.api.libs.functional.syntax._
import play.api.libs.json._
import play.api.mvc._
import play.api.mvc.Action
import play.api.mvc.Controller
import scala.collection.mutable.ListBuffer

object Amr extends Controller {

  def amr = IsAuthenticated { username =>
    implicit request => {
      println("username = " + username)

      val configItems = JDBC.withConnection(username) { implicit connection =>
        models.SienaConfig.getConfigItems(connection, List("siena_amr.suspended_meters.load_from_cis",
          "siena_amr.event.restore_event.enable",
          "siena_amr.event.outage_event.enable",
          "siena_amr.event_duplicate.timeout",
          "siena_amr.pmu.enable",
          "siena_amr.resolve_on_restore",
          "siena_amr.cis.meter.client",
          "siena_amr.ping_action",
          "siena_amr.resolve_timeout",
          "siena_amr.outage_timeout",
          "siena.trouble.amr.outage_response",
          "siena.trouble.amr.manual_response",
          "siena.trouble.amr.unsolicited_event",
          "siena_amr.restore_ping_field",
          "siena_amr.reping_on_outage_count",
          "siena_amr.reping_window",
          "siena_amr.reping_delay",
          "siena_amr.processType.test_mode",
          "siena_amr.HTTP.Server",
          "siena_amr.HTTP.Path",
          "siena_amr.oa_version",
          "siena_amr.oa_response_url",
          "od_event.disable_tag_level"))
      }
      println("***** DEBUG : retrieved " + configItems.size + " config items")

      Ok(views.html.amr(AmrConfig.loadForm(configItems)))
    }
  }

  def getNarrativeData = IsAuthenticated { username =>
    implicit request => {
      println("username = " + username)

      val configItems = JDBC.withConnection(username) { implicit connection =>
        models.SienaConfig.getConfigItems(connection, List("siena_amr.suspended_meters.load_from_cis",
          "siena_amr.event.restore_event.enable",
          "siena_amr.event.outage_event.enable",
          "siena_amr.event_duplicate.timeout",
          "siena_amr.pmu.enable",
          "siena_amr.resolve_on_restore",
          "siena_amr.cis.meter.client",
          "siena_amr.ping_action",
          "siena_amr.resolve_timeout",
          "siena_amr.outage_timeout",
          "siena.trouble.amr.outage_response",
          "siena.trouble.amr.manual_response",
          "siena.trouble.amr.unsolicited_event",
          "siena_amr.restore_ping_field",
          "siena_amr.reping_on_outage_count",
          "siena_amr.reping_window",
          "siena_amr.reping_delay",
          "siena_amr.processType.test_mode",
          "siena_amr.HTTP.Server",
          "siena_amr.HTTP.Path",
          "siena_amr.oa_version",
          "siena_amr.oa_response_url",
          "od_event.disable.tag_level"))
      }
      println("***** DEBUG : retrieved " + configItems.size + " config items")
      val configJson = Json.toJson(SienaConfig.mapConfigToJson(configItems))

      // Is the disable tag currently on the screen?
      val disabled = JDBC.withConnection(username) { implicit connection =>
        Procedures.isODEventDisabled(connection)
      }
      val disableJson = Json.toJson(Map[String, String]("isDisabled" -> disabled.toString))

      // Is the system sending pings out every minute?
      val (found, increment) = JDBC.withConnection(username) { implicit connection =>
        val r = Procedures.isJobFound(connection, "meterping.ping")
        (r._1, r._2)
      }
      val meterPing = Json.toJson(Map[String,String]("meter_ping" -> found.toString, "meter_ping_increment" -> increment.toString))

      // Is the system sending pings out every minute?
      val (found2, increment2) = JDBC.withConnection(username) { implicit connection =>
        val r = Procedures.isJobFound(connection, "meterping.resolveamicalls")
        (r._1, r._2)
      }
      val resolveCalls = Json.toJson(Map[String,String]("resolve_calls" -> found2.toString, "resolve_calls_increment" -> increment2.toString))
      
       // Do we have problem codes set up?
      val (restore, outage) = JDBC.withConnection(username) { implicit connection =>
        val r = Procedures.isProblemCodeTablePopulated(connection)
        (r._1, r._2)
      }
      val problemCalls = Json.toJson(Map[String,String]("resolve_pc" -> restore.toString, "outage_pc" -> outage.toString))
      
      // are any event records going to cause creation of outage?
      val oc = JDBC.withConnection(username) { implicit connection =>
        Procedures.isOutagesAnalyzed(connection)       
      }
      val outageCount = Json.toJson(Map[String,String]("outage_analyze" -> oc.toString))
      
      // are any event records going to cause creation of outage?
      val lc = JDBC.withConnection(username) { implicit connection =>
        Procedures.isLightsCodeSet(connection)       
      }
      val lightCodeCount = Json.toJson(Map[String,String]("light_code_count" -> lc.toString))

      
      // combine all the results into a json string
      val json = configJson.as[JsObject] ++ disableJson.as[JsObject] ++ meterPing.as[JsObject] ++ resolveCalls.as[JsObject] ++ outageCount.as[JsObject] ++ problemCalls.as[JsObject] ++ lightCodeCount.as[JsObject]
      
      Ok(json)
    }
  }

  def narrative = IsAuthenticated { username =>
    implicit request => {
      println("username = " + username)

      val configItems = JDBC.withConnection(username) { implicit connection =>
        models.SienaConfig.getConfigItems(connection, List("siena_amr.oa_version",
          "siena.trouble.amr.unsolicited_event",
          "siena.trouble.amr.outage_response",
          "siena.trouble.amr.manual_response",
          "siena_amr.reping_on_outage_count",
          "siena_amr.reping_window",
          "siena_amr.reping_delay",
          "siena_amr.disable.tag_level",
          "siena_amr.suspended_meters.load_from_cis",
          "siena_amr.outage_event.enable",
          "siena_amr.restore_event.enable"))
      }

      println("***** DEBUG : retrieved " + configItems.size + " config items")

      Ok(views.html.amrNarrative())
    }
  }

  // returns json-ified caller type rows
  def getCallerType = IsAuthenticated { username =>
    implicit request => {
      val configTypes = JDBC.withConnection(username) { implicit connection =>
        models.CallerType.getAllCallertypes(connection)
      }
      val list = configTypes.map(c => Json.toJson(Map("callerTypeId" -> c.caller_type_key.toString, "callerType" -> c.caller_type.getOrElse("Not Set"))))
      Ok(Json.toJson(list))
    }
  }

  // returns json-ified problem codes
  def getProblemCodes = IsAuthenticated { username =>
    println("DEBUG : getProblemCodes called");
    implicit request => {
      val problemCodes = JDBC.withConnection(username) { implicit connection =>
        models.ProblemCode.getAllProblemCodes(connection)
      }
      val events = JDBC.withConnection(username) { implicit connection =>
        models.EventNotification.getAllEventNotifications(connection, 20, Some(Map("source" -> "ODEvent")))
      }

      val list = problemCodes.map(p => Json.toJson(Map("problemCodeKey" -> p.problem_code_key.getOrElse(""), "problemCode" -> p.problem_code, "troubleTypeCode" -> p.trouble_type_code, "source" -> p.source, "eventType" -> p.event_type)))
      Ok(Json.toJson(list))
    }
  }

  def getEvents = IsAuthenticated { username =>
    println("DEUBG : getEvents is running")
    implicit request => {
      JDBC.withConnection(username) { implicit connection =>
        val events = EventNotification.getAllEventNotifications(connection, 50, None)
        println("DEBUG : TROUBLE : events returned = " + events.size)
        val json = EventNotification.eventsToJson(events)
        println("DEBUG : JSON : " + json.toString())
        Ok(json)
      }
    }
  }

  // saves the caller type text item
  def submit = IsAuthenticated { username =>
    implicit request =>
      //      println("the form has been submitted! json looks like : " + request.body.toString)
      //      val config = TroubleConfig.troubleConfigForm.bindFromRequest.fold(
      //        formWithErrors => {
      //          // binding failure, retrieved form with errors:
      //          println("form get failed!, form is : " + formWithErrors.errors)
      //          Redirect(routes.Trouble.trouble())
      //          // TODO : Have an error page or error areas within the form
      //        }, formData => {
      //          // save form data back to db!
      //          JDBC.withConnection(username) { implicit connection =>
      //            TroubleConfig.saveForm(connection, formData)
      //            CallerType.addCallerType(connection, formData.callerType)
      //          }
      //          Redirect(routes.Trouble.trouble())
      //        })
      Redirect(routes.Trouble.trouble())
  }

  /*
   * These two helpers de(serialize) problem codes
   * from the updateProblemCodes method
   */
  implicit val pcw: Writes[ProblemCode] = (
    (JsPath \ "problemCodeKey").write[Option[String]] and
    (JsPath \ "problemCode").write[String] and
    (JsPath \ "troubleTypeCode").write[String] and
    (JsPath \ "source").write[String] and
    (JsPath \ "eventType").write[String])(unlift(ProblemCode.unapply))

  implicit val pcr: Reads[ProblemCode] = (
    (JsPath \ "problemCodeKey").read[Option[String]] and
    (JsPath \ "problemCode").read[String] and
    (JsPath \ "troubleTypeCode").read[String] and
    (JsPath \ "source").read[String] and
    (JsPath \ "eventType").read[String])(ProblemCode.apply _)

  //  // saves the caller type text item
  //  def submit = IsAuthenticated { username =>
  //    implicit request =>
  //      println("the form has been submitted! json looks like : " + request.body.toString)
  //      val config = AmrConfig.troubleConfigForm.bindFromRequest.fold(
  //        formWithErrors => {
  //          // binding failure, retrieved form with errors:
  //          println("form get failed!, form is : " + formWithErrors.errors)
  //          Redirect(routes.Amr.trouble())
  //          // TODO : Have an error page or error areas within the form
  //        }, formData => {
  //          // save form data back to db!
  //          JDBC.withConnection(username) { implicit connection =>
  //            AmrConfig.saveForm(connection, formData)
  //            CallerType.addCallerType(connection, formData.callerType)
  //          }
  //          Redirect(routes.Amr.trouble())
  //        })
  //      Redirect(routes.Amr.trouble())
  //  }
}