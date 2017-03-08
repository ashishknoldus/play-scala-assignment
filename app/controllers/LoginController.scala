package controllers

import com.github.t3hnar.bcrypt._
import com.google.inject.Inject
import play.api.cache.CacheApi
import play.api.mvc.{Action, Controller}


/**
  * Created by knoldus on 7/3/17.
  */
class LoginController @Inject()(cache: CacheApi) extends Controller {

  def login = Action { implicit request =>

    val loginBody: Option[Map[String, Seq[String]]] = request.body.asFormUrlEncoded

    loginBody match {
      case Some(data) => {
        val email = data("email")(0)
        val password = data("password")(0)

        cache.get[Map[String, String]](email) match {
          case Some(map) => {
            if (map("password").bcrypt == password) {
              Redirect(routes.ProfileController.showProfile()).withSession("connected" -> email)
            } else if (map("suspended") == "yes") {
              Ok(views.html.loginwitherror("Login")("You've been suspended by admin. Contact admin."))
            } else {
              Ok(views.html.loginwitherror("Login")("Password doesn't match for that email"))
            }
          }
          case None => Ok(views.html.loginwitherror("Login")("There is no account with that email"))
        }


      }
      case _ => Redirect(routes.SignupController.showSignupForm())
    }

  }

  def showLogin = Action {
    Ok(views.html.login("Login"))
  }

}
