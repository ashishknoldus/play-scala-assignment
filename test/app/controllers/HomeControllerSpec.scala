package app.controllers

/**
  * Created by knoldus on 9/3/17.
  */

import controllers.HomeController
import org.scalatestplus.play._
import play.api.mvc._
import play.api.test.Helpers._
import play.api.test._

import scala.concurrent.Future

class HomeControllerSpec extends PlaySpec with Results{

    "Home Page#index" should {
      "should be valid" in {
        val controller = new HomeController()
        val result : Future[Result] = controller.index().apply(FakeRequest())
        val bodyText: String = contentAsString(result)
        println(s"Returned string of HomeController: $bodyText")
        bodyText must include("<title>Home</title>")
      }
    }
}
