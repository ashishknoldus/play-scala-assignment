package controllers

import play.api.mvc.{Action, Controller}

/**
  * Created by knoldus on 13/3/17.
  */
class HomeDataController extends Controller {

  def homeData = Action {
    implicit request =>
      Ok(views.html.homepage(request))
  }

}
