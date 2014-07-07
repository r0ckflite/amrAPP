package global

import play.api._
import scala.io._
import java.io.{ InputStream, File }
import play.api.libs.json._

object Global {

  
  def getSource(fname: String): Option[BufferedSource] = {
    try {
      Some(Source.fromFile(s"public/${fname}"))
    } catch {
      case e: java.io.FileNotFoundException =>
        println("file not found : " + e)
        return None
    }
  }

  def test1() {
    val text = getSource("databases.json").get.mkString
    println("text = " + text)
  }
}