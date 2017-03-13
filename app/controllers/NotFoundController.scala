package controllers

import play.api.mvc.{Action, Controller}

/**
  * Created by knoldus on 8/3/17.
  */
class NotFoundController extends Controller {
  def notFoundPage(path: String) = Action {implicit request =>
    Ok(views.html.notfoundpage(path)(request))
  }
}
