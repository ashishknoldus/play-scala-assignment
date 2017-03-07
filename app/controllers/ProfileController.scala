package controllers

import models.UserData
import play.api.mvc.{Action, Controller}

/**
  * Created by knoldus on 7/3/17.
  */
class ProfileController extends Controller{

  def showProfile = Action { request =>

    request.session.get("connected").map { user =>

      val userData = UserData.getUser(user)

      if(userData.nonEmpty) {
        Ok(views.html.userprofile(userData("name"))(userData))
      }
      else {
        Redirect(routes.HomeController.index())
      }
    }.getOrElse {
      Redirect(routes.SignupController.showSignupForm())
    }
  }
}
