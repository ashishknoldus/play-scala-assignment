package controllers

import com.google.inject.Inject
import play.api.cache.CacheApi
import play.api.mvc.{Action, Controller}

import scala.collection.mutable

/**
  * Created by knoldus on 8/3/17.
  */
class UserManagementController @Inject()(cache: CacheApi) extends Controller {

  def suspend(email: String) = Action { implicit request =>

    val userDataOption = cache.get[Map[String, String]](email)

    val userData = userDataOption match {
      case Some(x) => x
      case None => Map[String, String]()
    }

    val userDetails = mutable.Map[String, String]() ++ cache.get(email).getOrElse(Map())

    userDetails("suspended") = "yes"
    cache.set(email, userDetails.toMap)

    Ok(views.html.adminprofile(userData)(cache.get("listOfUsers").getOrElse(List()))(cache))

  }

  def resume(email: String) = Action { implicit request =>
    val userDataOption = cache.get[Map[String, String]](email)

    val userData = userDataOption match {
      case Some(x) => x
      case None => Map[String, String]()
    }

    val userDetails = mutable.Map[String, String]() ++ cache.get(email).getOrElse(Map())

    userDetails("suspended") = "no"
    cache.set(email, userDetails.toMap)

    Ok(views.html.adminprofile(userData)(cache.get("listOfUsers").getOrElse(List()))(cache))
  }
}
