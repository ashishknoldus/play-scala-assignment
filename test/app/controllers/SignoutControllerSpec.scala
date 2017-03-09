package app.controllers

import controllers.SignoutController
import org.scalatest.mock.MockitoSugar
import org.scalatestplus.play.{OneAppPerSuite, PlaySpec}
import play.api.mvc.Result
import play.api.test.FakeRequest
import play.api.test.Helpers._

import scala.concurrent.Future

/**
  * Created by knoldus on 10/3/17.
  */
class SignoutControllerSpec  extends PlaySpec with OneAppPerSuite with MockitoSugar {


  "Signup Controller" should {

    "signout#signup" in {

      val request = FakeRequest(GET, "/signup")
      val controller = new SignoutController

      val result: Future[Result] = controller.logout(request)

      status(result) mustBe 303 //Redirected
    }
  }
}
