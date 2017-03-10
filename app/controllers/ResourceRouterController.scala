package controllers

import play.api.mvc.{Action, Controller}
import play.api.routing._

/**
  * Created by knoldus on 10/3/17.
  */
class ResourceRouterController extends Controller {

  def javascriptRoutes = Action { implicit request =>
    Ok(
      JavaScriptReverseRouter("jsRoutes")(
        routes.javascript.HomeController.index,
        routes.javascript.LoginController.login,
        routes.javascript.LoginController.showLogin,
        routes.javascript.NotFoundController.notFoundPage,
        routes.javascript.ProfileController.showProfile,
        routes.javascript.SignoutController.logout,
        routes.javascript.SignupController.showSignupForm,
        routes.javascript.SignupController.handleSignupForm,
        routes.javascript.UserManagementController.suspend,
        routes.javascript.UserManagementController.resume
      )
    ).as("text/javascript")
  }

}
