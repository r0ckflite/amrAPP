package models

case class User(conn: String, name: String, password: String)

object Users {
  def authenticate(conn: String, password: String): Int = {
    println(s"authenticate, always suceeds : ${conn} , ${password}")
    1
  }
}

//object Users extends Table[User]("USER") {
//  lazy val database = Database.forDataSource(DB.getDataSource())
//
//  // -- Parsers
//
//  def email = column[String]("EMAIL", O.PrimaryKey)
//  def name = column[String]("NAME")
//  def password = column[String]("PASSWORD")
//
//  def * = email ~ name ~ password <> (User.apply _, User.unapply _)
//  // -- Queries
//
//  /**
//   * Retrieve a User from email.
//   */
//  def findByEmail(email: String): Option[User] = {
//    database withSession { implicit session =>
//      val q1 = for (u <- Users if u.email === email) yield u
//      q1.list.headOption.asInstanceOf[Option[User]]
//    }
//  }
//
//  /**
//   * Retrieve all users.
//   */
//  def findAll = {
//    for (u <- Users) yield u
//  }
//
//  /**
//   * Authenticate a User.
//   */
//  def authenticate(email: String, password: String): Int = {
//    database withSession { implicit session =>
//      val q1 = for (u <- Users if u.email === email && u.password === password) yield u
//      println("^^^^^^^^" + Query(q1.length).first)
//      Query(q1.length).first
//    }
//  }
//}
