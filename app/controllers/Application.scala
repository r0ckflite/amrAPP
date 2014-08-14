package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import models._
import views._
import play.api.libs.json._
import org.h2.engine.Database
import play.api.db.DB
import play.api.Play.current
import global._
import play.api.data._
import play.api.data.Forms._
import play.api.data.format.Formats._
import scala.concurrent.Future

object Application extends Controller with Secured {

  var ds: javax.sql.DataSource = null

  lazy val loginForm = Form(
    tuple(
      "conn" -> text,
      "password" -> text) verifying ("Invalid connection name or password", result => result match {
        case (conn, password) => {
          println("connection=" + conn + " password=" + password);
          val userList = Users.authenticate(conn, password)
          if (userList.isDefined) {
            // set the ds and user is authenticated
            var ds = userList.get
            true
          } else {
            false
          }
        }
        case _ => false
      }))

  def login = Action { implicit request =>
    Ok(html.login(loginForm))
  }

  def getPage1Vars = IsAuthenticated { username =>
    implicit request => {

      val configItems = JDBC.withConnection(username) { implicit connection =>
        models.SienaConfig.getConfigItems(connection, List(
          "oa_od.client",
          "oa_od.event.restore_event.enable",
          "oa_od.event.outage_event.enable"))
      }

      val client = SienaConfig.getValue(configItems, "oa_od.client", "not set")
      val restore = SienaConfig.getValue(configItems, "oa_od.event.restore_event.enable", "0")
      val outage = SienaConfig.getValue(configItems, "oa_od.event.outage_event.enable", "0")

      val json = Json.toJson(Map[String, String](
        "client" -> client,
        "restoreEvent" -> (if (restore.equals("0")) "NOT " else ""),
        "outageEvent" -> (if (outage.equals("0")) "NOT " else "")))


      Ok(json)
    }
  }

  /**
   * Handle login form submission.
   */
  def authenticate = Action { implicit request =>
    loginForm.bindFromRequest.fold(
      formWithErrors => BadRequest(html.login(formWithErrors)),
      user => Redirect(routes.Application.index).withSession("conn" -> user._1))
  }

  def test = Action { implicit request =>
    Ok(html.test1())
  }

  /**
   * Logout and clean the session.
   */
  def logout = Action {
    Redirect(routes.Application.login).withNewSession.flashing(
      "success" -> "You've been logged out")
  }

  def index = IsAuthenticated { username =>
    implicit request => {

      Ok(views.html.index())
    }
  }
}

/**
 * Provide security features
 */
trait Secured {
  self: Controller =>

  /**
   * Retrieve the connected user id.
   */
  def username(request: RequestHeader) = request.session.get("conn")

  /**
   * Redirect to login if the use in not authorized.
   */
  def onUnauthorized(request: RequestHeader) = Results.Redirect(routes.Application.login)

  def IsAuthenticated(f: => String => Request[AnyContent] => Result) =
    Security.Authenticated(username, onUnauthorized) { user =>
      Action(request => f(user)(request))
    }

}
