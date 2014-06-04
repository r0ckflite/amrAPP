package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._

import models._
import views._

object Application extends Controller with Secured {

  lazy val loginForm = Form(
    tuple(
      "conn" -> text,
      "password" -> text) verifying ("Invalid connection name or password", result => result match {
        case (conn, password) => {
          println("connection=" + conn + " password=" + password);
          val userList = Users.authenticate(conn, password)
          userList == 1
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

  /**
   * Logout and clean the session.
   */
  def logout = Action {
    Redirect(routes.Application.login).withNewSession.flashing(
      "success" -> "You've been logged out")
  }

  def index = IsAuthenticated { username =>
    implicit request => Ok(views.html.index())
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
  //def onUnauthorized(request: RequestHeader): Result
  def onUnauthorized(request: RequestHeader) = Results.Redirect(routes.Application.login)

  def IsAuthenticated(f: => String => Request[AnyContent] => Result) =
    Security.Authenticated(username, onUnauthorized) { user =>
      Action(request => f(user)(request))
    }
}
