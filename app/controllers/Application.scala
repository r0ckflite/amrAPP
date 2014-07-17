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

  /**
   * Handle login form submission.
   */
  def authenticate = Action { implicit request =>
    loginForm.bindFromRequest.fold(
      formWithErrors => BadRequest(html.login(formWithErrors)),
      user => Redirect(routes.Application.index).withSession("conn" -> user._1))
  }

  def submit = IsAuthenticated { username => implicit request =>
    println("the form has been submitted!")
    val config = AmrConfig.amrConfigForm.bindFromRequest.fold(
        formWithErrors => {
          // binding failure, retrieved form with errors:
          println("form get failed!, form is : " + formWithErrors.errors)
          BadRequest(views.html.index(formWithErrors, AmrConfig.formElements))
          // TODO : Have an error page or error areas within the form
        }, formData => {
          // save form data back to db!
          JDBC.withConnection(username) { implicit connection =>
            AmrConfig.saveForm(connection, formData)
          }
          Redirect(routes.Application.index())
        }
    )
    Redirect(routes.Application.index())
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
      println("username = " + username)

      // load all config items from db, might want to filterr this query
      val configItems = JDBC.withConnection(username) { implicit connection =>
        models.SienaConfig.configs2(connection)
      }

      Ok(views.html.index(AmrConfig.loadForm(configItems), AmrConfig.formElements))
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
