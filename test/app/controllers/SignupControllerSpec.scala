package app.controllers

import com.google.inject.Inject
import controllers.{SignupController, routes}
import play.api.mvc.Result
import play.api.test.{FakeRequest, Helpers}
import play.api.test.Helpers._

import scala.concurrent.Future
import org.scalatestplus.play.{OneAppPerSuite, PlaySpec}
import services.VerifySignupDataService
import org.mockito.Mockito._
import org.scalatest.mock.MockitoSugar
import play.api.{Configuration, test}
import play.api.cache.CacheApi
import play.api.mvc.AnyContent
import play.api.test.Helpers.contentAsString

import scala.collection.Map
import scala.reflect.macros.whitebox

/**
  * Created by knoldus on 9/3/17.
  */
class SignupControllerSpec extends PlaySpec with OneAppPerSuite with MockitoSugar{

  "signup controller " should {

    "redirect successfully with correct data" in {

      val anyContent: AnyContent = null

      // mock service, cache and configuration
      val verifySignupDataService = mock[VerifySignupDataService]
        when(verifySignupDataService.verifyData(anyContent)) thenReturn Some(textData, fileData)
      val cache = mock[CacheApi]
      val config = mock[Configuration]

      //controller
      val controller = new SignupController(verifySignupDataService, cache, config)

      val request = FakeRequest(Helpers.POST, "/signup")
        .withHeaders("X-Requested-With" -> "1")
        .withFormUrlEncodedBody(
          "name" -> "Ashish Tomer",
          "username" -> "Ashish1269",
          "age" -> "19",
          "email" -> "ashish1269@gmail.com",
          "mobile" -> "9876543210",
          "gender" -> "Male",
          "password" -> "1234567890",
          "confirm" -> "1234567890",
          "file" -> ""
        )

      val result: Future[Result] = controller.handleSignupForm(request)

      /*val nextUrl = Helpers.redirectLocation(result) match {
        case Some(s: String) => s
        case _ => ""
      }*/

      redirectLocation(result) must contain(routes.ProfileController.showProfile.url)

    }
  }

  val dataMap: Map[String, String] = Map[String,String](
    "name" -> "Ashish Tomer",
    "username" -> "Ashish1269",
    "age" -> "19",
    "email" -> "ashish1269@gmail.com",
    "mobile" -> "9876543210",
    "gender" -> "Male",
    "password" -> "1234567890"
  )

  val errorMap: Map[String, String] = Map[String, String]() //No error in textData

  val textData  = Map[String, Option[Map[String, String]]](
    "error" -> Some(errorMap),
    "data" -> Some(dataMap)
  )

  val fileData = Map[String, String](
    "image" -> "file:///home/knoldus/Templates/anImage.jpg",
    "imageError" -> "" //No error in image upload
  )

}
