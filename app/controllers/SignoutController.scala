package controllers

import play.api.mvc.{Action, Controller}

/**
  * Created by knoldus on 7/3/17.
  */
class SignoutController extends Controller {
  def logout = Action { request =>
    Redirect(routes.HomeController.index())
      .withNewSession
  }
}
