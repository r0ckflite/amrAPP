package models;

import anorm._
import anorm.SqlParser._
import java.math.BigDecimal
import org.joda.time.DateTime

object Task {

  def apply(id: BigDecimal, label: String): Task = {
    new Task(id, label)
  }
}

class Task (
    var id: BigDecimal,
    var label: String)

