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

object Application extends Controller {

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

  def index = Action {
    //val ds = DB.getDataSource()
    //val list = tasks()
    //list.foreach(t => println("task: id=" + t.id + ", label = " + t.label))
    //val clist = configs()
    //clist.foreach(c => println("config: item = " + c.item + ", default_value = " + c.defaultValue + ", value = " + c.value + ", description = " + c.description + ", multiples allowed = " + c.multiplesAllowed))
    Ok(views.html.index("Your new application is ready."))
  }

}