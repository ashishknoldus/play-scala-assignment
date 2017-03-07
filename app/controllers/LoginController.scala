package controllers

import models.UserData
import play.api.mvc.{Action, Controller}

/**
  * Created by knoldus on 7/3/17.
  */
class LoginController extends Controller{

  def login = Action {implicit request =>

    val loginBody:Option[Map[String, Seq[String]]]  = request.body.asFormUrlEncoded

    loginBody match {
      case Some(data) => {
        val email = data("email")(0)
        val password = data("password")(0)

        if(UserData.userExists(email, password)) {
          Redirect(routes.ProfileController.showProfile())
            .withSession("connected" -> email)
        } else {
          Ok(views.html.loginwitherror("Login")("Email or password doesn't match"))
        }


      }
      case _ => Redirect(routes.SignupController.showSignupForm())
    }

  }

  def showLogin = Action {
    Ok(views.html.login("Login"))
  }

}
