package models

import play.api.Play.current
import play.api.data._
import play.api.data.Forms._
import play.api.data.format.Formats._
import models._
import java.sql.Connection

object Procedures {

  /*
   * returns true if stored proc indicates od events are currently not being
   * processed in database
   */
  def isODEventDisabled(connection: Connection): Boolean = {
    try {
      val s = connection.prepareCall("begin ? := sys.diutil.bool_to_int(siena_web_services.od_tools.isODEventDisabled()); end;")
      s.registerOutParameter(1, java.sql.Types.NUMERIC)
      s.executeUpdate()
      val rval = s.getInt(1)
      if (rval == 1) return true else return false
    } catch {
      case e: Exception => println("DB ERROR : isODEventDisabled failed with code " + e)
    }
    return true
  }

  /*
   * generic function to search for existence of a job based on a substring
   */
  def isJobFound(connection: Connection, what: String): (Boolean, Integer, java.util.Date, String) = {
    val query = s"select (next_date-last_date)*(24*60) delay, next_date, broken from dba_jobs where lower(what) like '%$what%'"
    try {
      val s = connection.prepareCall(query)
      val rs = s.executeQuery()
      if (rs.next()) {
        return (true, rs.getInt(1), rs.getDate(2), rs.getString(3))
      }
    } catch {
      case e: Exception => println("DB ERROR : isLightsCodeSet failed with code " + e)
    }
    return (false, 0, new java.util.Date(System.currentTimeMillis()), "y")
  }

  /*
   * returns 1 if problem_codes linked to trouble_type are causing
   * trouble reports to be analyzed, ie; creating outages
   */
  def isOutagesAnalyzed(connection: Connection): Integer = {
    val q = """select count(*) the_count
    		from trouble_type_code
    		where code in
    			(select trouble_type_code 
    			from siena_web_services.problem_code
    			where lower(event_type) in ('outage', 'inferred'))
    		and lower(do_not_analyze) = 'X'"""
    try {
      val s = connection.prepareCall(q);
      val rs = s.executeQuery()
      if (rs.next()) {
        return rs.getInt(1)
      }
    } catch {
      case e: Exception => println("DB ERROR : isLightsCodeSet failed with code " + e)
    }
    return 0
  }

  def isLightsCodeSet(connection: Connection): Integer = {
    try {
      val q = "select count(*) from astorm2.lights where lights in ('AMI_ON', 'AMI_OFF')"
      val s = connection.prepareCall(q);
      val rs = s.executeQuery()
      if (rs.next()) {
        return rs.getInt(1)
      }
    } catch {
      case e: Exception => println("DB ERROR : isLightsCodeSet failed with code " + e)
    }
    return 0
  }

  def isProblemCodeTablePopulated(connection: Connection): (Integer, Integer) = {
    val q = "select count(*) from siena_web_services.problem_code where lower(event_type) in ('outage', 'inferred')"
    var restore = 0
    var outage = 0

    try {
      val s = connection.prepareCall(q)
      val rs = s.executeQuery()
      if (rs.next()) {
        outage = rs.getInt(1)
        rs.close()
      }
      s.close()

      val q2 = "select count(*) from siena_web_services.problem_code where lower(event_type) in ('restoration')"

      val s2 = connection.prepareCall(q)
      val rs2 = s2.executeQuery()
      if (rs2.next()) {
        restore = rs2.getInt(1)
        rs2.close()
      }
      s2.close()
      return (restore, outage)
    } catch {
      case e: Exception => println("DB ERROR : isLightsCodeSet failed with code " + e)
    }

    return (0, 0)
  }
}