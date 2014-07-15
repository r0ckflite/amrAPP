package models

import play.api.db.DB
import play.api.mvc.Result
import play.api.mvc.Request
import play.api.mvc.AnyContent
import play.api.Play.current
import play.api.libs.json.JsValue
import play.api.libs.json.Json
import play.api.libs.json.JsArray
import play.api.libs.json.JsObject
import oracle.jdbc.pool._
import global._
import oracle.jdbc.replay.OracleDataSourceImpl
import play.api.Play.current
import com.typesafe.config.ConfigFactory

object User {
  def apply(name: String, url: String, user: String): User = {
    new User(name = Some(name), url = url, user = user)
  }
}
class User(
  var name: Option[String] = None,
  var url: String,
  var user: String) {
  override def toString(): String = {
    "user: name=" + name + ", url=" + url + ", user=" + user
  }
}

object Users {

  // load users from databases.json file
  lazy val users = loadConnectionInfo

  def authenticate(connName: String, password: String): Option[javax.sql.DataSource] = {
    println(s"authenticating : ${connName} , ${password}")
    // TODO : eventually, do not hardcode pw
    if (authenticatePassword(password) != true)
      return None

    try {
      println("***** getting datasource")
      val dataSource = JDBC.getDataSource(connName)
      println("***** datasource retrieved")
      return Some(dataSource)
    } catch {
      case e: java.sql.SQLException =>
        println("sql exception : " + e)
        None
    }
  }

  def authenticatePassword(password: String): Boolean = {
    try {
      var ap = ConfigFactory.load().getString("app.password")
      if (ap != null && password.equals(ap))
        return true
      false
    } catch {
      case e : com.typesafe.config.ConfigException =>
        println("config item 'app.password' not found")
        false
    }
  }

  // gets oracle pooled datasource
  def getOracleDataSource(user: User, password: String): javax.sql.DataSource = {
    val ds: OracleConnectionPoolDataSource = new OracleConnectionPoolDataSource()
    ds.setURL(user.url)
    ds.setUser(user.user)
    ds.setPassword(password)
    ds.setDataSourceName("default")
    ds
  }

  // parses json file, returns user seq
  def loadConnectionInfo(): Seq[User] = {
    // read the entire file into a string
    val lines = io.Source.fromFile("public/startupData/databases.json").mkString
    // create a sequence of JsValue(s)
    val jsObjectSeq: Seq[JsObject] = Json.parse(lines).as[Seq[JsObject]]
    println(s"read databases.json, got ${jsObjectSeq.size} lines")
    jsObjectSeq.map(row => User.apply(
      (row \ "name").asOpt[String].getOrElse("none"),
      (row \ "url").asOpt[String].getOrElse("none"),
      (row \ "user").asOpt[String].getOrElse("none")))
  }
}

