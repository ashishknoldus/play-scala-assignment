package app.controllers

import controllers.{NotFoundController, SignoutController}
import org.scalatest.mock.MockitoSugar
import org.scalatestplus.play.{OneAppPerSuite, PlaySpec}
import play.api.mvc.Result
import play.api.test.FakeRequest
import play.api.test.Helpers._

import scala.concurrent.Future

/**
  * Created by knoldus on 10/3/17.
  */
class NotFoundControllerSpec extends PlaySpec with OneAppPerSuite with MockitoSugar {



  "Not Found Controller" should {

    "display 404 page#showProfile" in {

      val request = FakeRequest(GET, "/signup")
      val controller = new NotFoundController

      val result: Future[Result] = controller.notFoundPage("/Hello")(request)

      contentAsString(result) must include("/Hello") //Redirected
    }
  }

}
