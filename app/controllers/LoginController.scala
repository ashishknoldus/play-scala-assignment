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
            if (password.isBcrypted(map("password") ) ) {
              Redirect(routes.ProfileController.showProfile()).withSession("connected" -> email)
            } else if (map("suspended") == "yes") {
              Ok(views.html.loginwitherror("You've been suspended by admin. Contact admin."))
            } else {
              println(s"---------------Password stored ---- " + map("password"))
              println(s"---------------Password sent ---- " + password.bcrypt)

              Ok(views.html.loginwitherror("Password doesn't match for that email"))
            }
          }
          case None => Ok(views.html.loginwitherror("There is no account with that email"))
        }


      }
      case _ => Redirect(routes.SignupController.showSignupForm())
    }

  }

  def showLogin = Action {
    implicit request =>
    Ok(views.html.login(request))
  }

}
