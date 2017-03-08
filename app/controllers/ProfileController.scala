package controllers

import com.google.inject.Inject
import models.UserData
import play.api.cache.CacheApi
import play.api.mvc.{Action, Controller}

/**
  * Created by knoldus on 7/3/17.
  */
class ProfileController @Inject()(cache: CacheApi)  extends Controller{

  def showProfile = Action { request =>

    request.session.get("connected").map { email =>

      val userDataOption = cache.get[Map[String, String]](email)

      val userData = userDataOption match {
                        case Some(x) => x
                        case None => Map[String, String]()
                      }

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
