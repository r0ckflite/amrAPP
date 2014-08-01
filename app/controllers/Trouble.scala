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

object Trouble extends Controller {

  // Main trouble report page
  def trouble = IsAuthenticated { username =>
    implicit request => {

      def data = JDBC.withConnection(username) { implicit connection =>
        val config = models.SienaConfig.getConfigItems(connection, List("siena.trouble.caller_type"))
        val callerTypes = models.CallerType.getAllCallertypes(connection)
        (config, callerTypes)
      }
      val (config, callerTypes) = data
      Ok(views.html.trouble(TroubleConfig.loadForm(config)))
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

  def getAllActiveTroubleTypes = IsAuthenticated { username =>
    println("DEBUG : getTroubleTypes")
    implicit request => {
      val code = request.body.asFormUrlEncoded
      println("DEBUG : code = " + code)
      val list = JDBC.withConnection(username) { implicit connection =>
        models.TroubleType.getAllActiveTroubleTypes(connection)
      }
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

  //   def getEvents = IsAuthenticated { username =>
  //    implicit request => {
  //      println("getEvents is running")
  //      val ef = EventNotification.eventFilterForm.bindFromRequest.fold(
  //        formWithErrors => {
  //          // binding failure, retrieved form with errors:
  //          println("form get failed!, form is : " + formWithErrors.errors)
  //          Ok
  //          // TODO : Have an error page or error areas within the form
  //        }, formData => {
  //          // save form data back to db!
  //          JDBC.withConnection(username) { implicit connection =>
  //            val (source, eventType) = (formData._1, formData._2)
  //            var filterMap= scala.collection.mutable.Map[String,String]()
  //            if( source != null && source.size > 0)
  //              filterMap += ("source" -> source)
  //            if( eventType != null && eventType.size > 0 )
  //              filterMap += ("eventType" -> eventType)
  //
  //            val events = EventNotification.getAllEventNotifications(connection, 50, if( filterMap.size > 0 ) Some(filterMap.toMap[String,String]) else None)
  //            val json = EventNotification.eventsToJson(events)
  //            Ok(json)
  //          }
  //          Redirect(routes.Trouble.trouble())
  //        })
  //    }
  //      Ok
  //  }

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

  implicit val ttw: Writes[TroubleType] = (
    (JsPath \ "code").write[String] and
    (JsPath \ "light").write[String] and
    (JsPath \ "wire").write[String] and
    (JsPath \ "transformer").write[String] and
    (JsPath \ "pole").write[String] and
    (JsPath \ "tree").write[String] and
    (JsPath \ "extent").write[String] and
    (JsPath \ "doNotAnalyze").write[Option[String]] and
    (JsPath \ "momentaryOutage").write[Option[String]])(unlift(TroubleType.unapply))

  implicit val ttr: Reads[TroubleType] = (
    (JsPath \ "code").read[String] and
    (JsPath \ "light").read[String] and
    (JsPath \ "wire").read[String] and
    (JsPath \ "transformer").read[String] and
    (JsPath \ "pole").read[String] and
    (JsPath \ "tree").read[String] and
    (JsPath \ "extent").read[String] and
    (JsPath \ "doNotAnalyze").read[Option[String]] and
    (JsPath \ "momentaryOutage").read[Option[String]])(TroubleType.apply _)

  def updateProblemCodes = Action(parse.tolerantFormUrlEncoded) { request =>
    /*
     * TODO : there should be an easier way to parse json array of
     * [{stuff},{stuff}], but I can't find it.
     * .
     */
    val req = request.body get "models"
    val problemCodes = new ListBuffer[ProblemCode]()
    if (req.isDefined) {
      req.get.map({ r =>
        val json: JsValue = Json.parse(r)
        json.as[JsArray].value.foreach({ x =>
          val foo = x.validate[ProblemCode] match {
            case pc: JsSuccess[ProblemCode] => {
              problemCodes += pc.get
              println("DEBUG: updateProblemCodes row : " + pc.get)
            }
            case e: JsError => {
              println("Error parsing form js : " + e)
            }
          }
        })
      })
    }
    // save the problem codes
    username(request).map { login =>
      JDBC.withConnection(login) { implicit connection =>
        ProblemCode.save(connection, problemCodes)
      }
    }

    Redirect(routes.Trouble.trouble())
  }

  // saves the caller type text item
  def submit = IsAuthenticated { username =>
    implicit request =>
      println("the form has been submitted! json looks like : " + request.body.toString)
      val config = TroubleConfig.troubleConfigForm.bindFromRequest.fold(
        formWithErrors => {
          // binding failure, retrieved form with errors:
          println("form get failed!, form is : " + formWithErrors.errors)
          Redirect(routes.Trouble.trouble())
          // TODO : Have an error page or error areas within the form
        }, formData => {
          // save form data back to db!
          JDBC.withConnection(username) { implicit connection =>
            TroubleConfig.saveForm(connection, formData)
            CallerType.addCallerType(connection, formData.callerType)
          }
          Redirect(routes.Trouble.trouble())
        })
      Redirect(routes.Trouble.trouble())
  }
}