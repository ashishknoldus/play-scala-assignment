package controllers

import play.api.mvc.{Action, Controller}

/**
  * Created by knoldus on 6/3/17.
  */
class HomeController extends Controller {

  def index = Action {
    implicit request =>
    Ok(views.html.index(request))
  }

}
