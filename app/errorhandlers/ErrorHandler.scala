package errorhandlers

import play.api.http.HttpErrorHandler
import play.api.mvc.Results._
import play.api.mvc._

import scala.concurrent._

/**
  * Created by knoldus on 2/3/17.
  */


class ErrorHandler extends HttpErrorHandler {

  def onClientError(request: RequestHeader, statusCode: Int, message: String) = {
    Future.successful(
      statusCode match {
        case 403 => Status(403)("A client error occurred: No access! <br/>" + message)
        case 404 => Status(404)("A client error occurred: Page Not Found! <br/>" + message)
      }

    )
  }

  def onServerError(request: RequestHeader, exception: Throwable) = {
    Future.successful(
      InternalServerError("A server error occurred: " + exception.getMessage)
    )
  }

}
