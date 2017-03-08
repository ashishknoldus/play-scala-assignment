package models

import scala.collection.{Map, mutable}

/**
  * Created by knoldus on 6/3/17.
  */

class UserData {

}

object UserData {
  val userDataBuffer: mutable.Map[String, Map[String, String]] = mutable.Map[String, Map[String, String]]()

  def saveUser(userData: Map[String, String]): Int = {
    userDataBuffer(userData("email")) = userData
    userDataBuffer.size //Returning total signed up users
  }

  def userExists(email: String, password: String): Boolean = {
    userDataBuffer.contains(email) &&
      userDataBuffer(email)("password") == password
  }

  def getUser(email: String): Map[String, String] = {
    userDataBuffer.getOrElse(email, Map())
  }
}
