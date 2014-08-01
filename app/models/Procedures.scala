package models

import play.api.Play.current
import play.api.data._
import play.api.data.Forms._
import play.api.data.format.Formats._
import models._
import java.sql.Connection

object Procedures {

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

  /*
   * convenience : laods data into Form and returns the form
   */
  def loadForm(c: Seq[SienaConfig]) = {
    def getValue(c: Seq[SienaConfig], name: String) =
      c.filter(c => c.item.equals(name)).lift(0).map(_.getValueString()).getOrElse("---")

    def getValueInt(c: Seq[SienaConfig], name: String): Option[Int] = {
      c.filter(c => c.item.equals(name)).lift(0).map(_.getValue().map(v => v.toInt)).getOrElse(None)
    }

    val oaVersion = getValue(c, "siena_amr.oa_version")
    val unsolicitedEvents = getValue(c, "siena.trouble.amr.unsolicited_event")
    val outageResponse = getValue(c, "siena.trouble.amr.outage_response")
    val manualResponse = getValue(c, "siena.trouble.amr.manual_response")
    val repingOnOutage = getValue(c, "siena_amr.reping_on_outage_count")
    val repingWindow = getValue(c, "siena_amr.reping_window")
    val repingDelay = getValue(c, "siena_amr.reping_delay")
    val validateDups = getValue(c, "siena_amr.validate_dups")
    val validateEventDate = getValue(c, "siena_amr.validate_event_date")
    val disableTagLevel = getValue(c, "siena_amr.disable.tag_level")
    val suspendedLoadCIS = getValue(c, "siena_amr.suspended_meters.load_from_cis")
    val outageEventEnable = getValue(c, "siena_amr.outage_event.enable")
    val restoreEventEnable = getValue(c, "siena_amr.restore_event.enable")
    AmrConfig.amrConfigForm1.fill(AmrConfig1(oaVersion, null, unsolicitedEvents, outageResponse, manualResponse, validateDups, validateEventDate, disableTagLevel, suspendedLoadCIS, outageEventEnable, restoreEventEnable, repingOnOutage, repingDelay, repingWindow))
  }

  /*
   * Saves each piece of form data
   * 
   * TODO: Create an DAO
   */
  def saveForm(connection: java.sql.Connection, formData: AmrConfig1) = {
    def update(item: String, value: String) = {
      val s = connection.prepareStatement("{call updateConfig(?, ?)}")
      s.setString(1, item)
      s.setString(2, value)
      s.execute()
    }

    update("siena_amr.oa_version", formData.oaVersion)
    update("siena.trouble.amr.unsolicited_event", formData.unsolicitedEvents)
    update("siena.troube.amr.outage_response", formData.outageResponse)
    update("siena.trouble.amr.manual_response", formData.manualResponse)
    update("siena_amr.reping_on_outage_count", formData.repingOnOutage)
    update("siena_amr.reping_delay", formData.repingDelay)
    update("siena_amr.validate_dups", formData.validateDups)
    update("siena_amr.validate_event_date", formData.validateEventDate)
    update("siena_amr.suspended_meters.load_from_cis", formData.suspendedLoadCIS)
    update("siena_amr.outage_event.enable", formData.outageEventEnable)
    update("siena_amr.restore_event.enable", formData.restoreEventEnable)
  }

}