package controllers

import play.api._
import play.api.mvc._
import play.api.db._
import play.api.Play.current
import anorm._
import anorm.SqlParser._
import models.Task
import java.math.BigDecimal
import org.joda.time.DateTime
import models.SienaConfig
import play.api.data.Forms._
import views._
import models._

object Application extends Controller {
  
  val loginForm = play.api.data.Form(
    tuple(
      "email" -> text,
      "password" -> text
    ) verifying ("Invalid email or password", result => result match {
      case (email, password) => User.authenticate(email, password).isDefined
    })
  )

  /**
   * Login page.
   */
  def login = Action { implicit request =>
    Ok(html.login(loginForm))
  }

  /**
   * Handle login form submission.
   */
  def authenticate = Action { implicit request =>
    loginForm.bindFromRequest.fold(
      formWithErrors => BadRequest(html.login(formWithErrors)),
      user => Redirect(routes.Application.index).withSession("email" -> user._1)
      
    )
  }

  /**
   * Logout and clean the session.
   */
  def logout = Action {
    Redirect(routes.Application.login).withNewSession.flashing(
      "success" -> "You've been logged out"
    )
  }

  val task = {
    get[BigDecimal]("id") ~
      get[String]("label") map {
        case id ~ label => Task.apply(id, label)
      }
  }

  val config = {
    get[String]("item") ~
      get[Option[String]]("default_value") ~
      get[Option[String]]("value") ~
      get[Option[String]]("description") ~
      get[Option[BigDecimal]]("multiples_allowed") map {
        case item ~ default_value ~ value ~ description ~ multiples_allowed => SienaConfig.apply(item,
          default_value, value, description, multiples_allowed)
      }
  }

  def tasks(): List[Task] = DB.withConnection { implicit c =>
    SQL("select 1 \"ID\", 'hello' \"LABEL\" from dual").as(task *)
  }

  def configs(): List[SienaConfig] = DB.withConnection { implicit conn =>
    SQL("select item, default_value, value, description, multiples_allowed from siena.siena_config").as(config *)
  }
  
  def index = IsAuthenticated { username => _ =>
  User.findByEmail(username).map {
    Ok(
        html.index
  }
  }

  def index = Action {
    //val ds = DB.getDataSource()
    //val list = tasks()
    //list.foreach(t => println("task: id=" + t.id + ", label = " + t.label))
    //val clist = configs()
    //clist.foreach(c => println("config: item = " + c.item + ", default_value = " + c.defaultValue + ", value = " + c.value + ", description = " + c.description + ", multiples allowed = " + c.multiplesAllowed))
    
    Ok(views.html.index("Your new application is ready."))
  }
  
}