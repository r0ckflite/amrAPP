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

  // index page
  def amr = IsAuthenticated { username =>
    implicit request => {
      println("username = " + username)

      val json = JDBC.withConnection(username) { implicit connection =>
        AmrConfig.load(connection)
      }
      println("***** DEBUG : retrieved " + json)

      Ok(views.html.amr())
    }
  }

  // returns json containing all configuration information on AMR
  def getNarrativeData = IsAuthenticated { username =>
    implicit request => {
      println("username = " + username)

      val configJson = JDBC.withConnection(username) { implicit connection =>
        AmrConfig.load(connection)
      }

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
      val meterPing = Json.toJson(Map[String, String]("meter_ping" -> found.toString, "meter_ping_increment" -> increment.toString))

      // Is the system sending pings out every minute?
      val (found2, increment2) = JDBC.withConnection(username) { implicit connection =>
        val r = Procedures.isJobFound(connection, "meterping.resolveamicalls")
        (r._1, r._2)
      }
      val resolveCalls = Json.toJson(Map[String, String]("resolve_calls" -> found2.toString, "resolve_calls_increment" -> increment2.toString))

      // Do we have problem codes set up?
      val (restore, outage) = JDBC.withConnection(username) { implicit connection =>
        val r = Procedures.isProblemCodeTablePopulated(connection)
        (r._1, r._2)
      }
      val problemCalls = Json.toJson(Map[String, String]("resolve_pc" -> restore.toString, "outage_pc" -> outage.toString))

      // are any event records going to cause creation of outage?
      val oc = JDBC.withConnection(username) { implicit connection =>
        Procedures.isOutagesAnalyzed(connection)
      }
      val outageCount = Json.toJson(Map[String, String]("outage_analyze" -> oc.toString))

      // are any event records going to cause creation of outage?
      val lc = JDBC.withConnection(username) { implicit connection =>
        Procedures.isLightsCodeSet(connection)
      }
      val lightCodeCount = Json.toJson(Map[String, String]("light_code_count" -> lc.toString))

      // combine all the results into a json string
      val json = configJson.as[JsObject] ++ disableJson.as[JsObject] ++ meterPing.as[JsObject] ++ resolveCalls.as[JsObject] ++ outageCount.as[JsObject] ++ problemCalls.as[JsObject] ++ lightCodeCount.as[JsObject]

      Ok(json)
    }
  }

  /*
   * Saves a single json config item received via a GET
   * { "siena_amr_oa_version" : "30x" }
   */
  def submitItem = IsAuthenticated { username =>
    implicit request => {
      val map = request.queryString.map { case (k, v) => k -> v.mkString }
      val name = map get "name"
      val value = map get "value"

      map.map(x => println("key/value = " + x._1 + ", " + x._2))

      if (name.isDefined && value.isDefined) {
        val configValue = AmrConfig.lookupMap get name.get
        JDBC.withConnection(username) { implicit connection =>
          AmrConfig.update(connection, configValue.get, value.get)
        }
      }
      Ok(views.html.amrNarrative())
    }
  }

  /*
   * returns the narrative page
   */
  def narrative = IsAuthenticated { username =>
    implicit request => {
      Ok(views.html.amrNarrative())
    }
  }

  /*
   * returns caller type json data
   */
  def getCallerType = IsAuthenticated { username =>
    implicit request => {
      val configTypes = JDBC.withConnection(username) { implicit connection =>
        models.CallerType.getAllCallertypes(connection)
      }
      val list = configTypes.map(c => Json.toJson(Map("callerTypeId" -> c.caller_type_key.toString, "callerType" -> c.caller_type.getOrElse("Not Set"))))
      Ok(Json.toJson(list))
    }
  }

  /*
   * returns json data with problem codes
   */
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

  /*
   * returns rows from event_notifications as json
   */
  def getEvents = IsAuthenticated { username =>
    println("DEUBG : getEvents is running")
    implicit request => {
      JDBC.withConnection(username) { implicit connection =>
        val events = EventNotification.getAllEventNotifications(connection, 50, None)
        val json = EventNotification.eventsToJson(events)
        Ok(json)
      }
    }
  }

//  /*
//   * These two helpers de(serialize) problem codes
//   * from the updateProblemCodes method
//   */
//  implicit val pcw: Writes[ProblemCode] = (
//    (JsPath \ "problemCodeKey").write[Option[String]] and
//    (JsPath \ "problemCode").write[String] and
//    (JsPath \ "troubleTypeCode").write[String] and
//    (JsPath \ "source").write[String] and
//    (JsPath \ "eventType").write[String])(unlift(ProblemCode.unapply))
//
//  implicit val pcr: Reads[ProblemCode] = (
//    (JsPath \ "problemCodeKey").read[Option[String]] and
//    (JsPath \ "problemCode").read[String] and
//    (JsPath \ "troubleTypeCode").read[String] and
//    (JsPath \ "source").read[String] and
//    (JsPath \ "eventType").read[String])(ProblemCode.apply _)
}