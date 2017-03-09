package app.controllers

import scala.concurrent.Future
import play.api.mvc._
import org.mockito.Mockito._
import controllers.SignupController
import org.scalatest.mock.MockitoSugar
import org.scalatestplus.play.{OneAppPerSuite, PlaySpec}
import play.api.Configuration
import play.api.cache.CacheApi
import play.api.libs.Files.TemporaryFile
import play.api.mvc.MultipartFormData.FilePart
import play.api.mvc.{AnyContent, AnyContentAsMultipartFormData, MultipartFormData}
import play.api.test.FakeRequest
import services.VerifySignupDataService
import play.api.test.Helpers._

import scala.collection.Map
import scala.concurrent.duration.DurationLong

/**
  * Created by knoldus on 9/3/17.
  */
class SignupControllerSpec  extends PlaySpec with OneAppPerSuite with MockitoSugar {

  val cache = mock[CacheApi]
  val configuration = mock[Configuration]
  val verifySignupDataService = mock[VerifySignupDataService]

  val dataMap: Map[String, String] = Map[String,String](
    "name" -> "Ashish Tomer",
    "username" -> "ashish1269",
    "age" -> "45",
    "email" -> "ashish1269@gmail.com",
    "mobile" -> "9876543210",
    "gender" -> "male",
    "password" -> "1234567890"
  )

  val textData  = Map[String, Option[Map[String, String]]](
    "error" -> None, //No error in textData
    "data" -> Some(dataMap)
  )

  val fileData = Map[String, String](
    "image" -> "file:///home/knoldus/Pictures/wallpapers/1321922_STpOMHj.jpg",
    "imageError" -> "" //No error in image upload
  )


  val anyContent: AnyContent =AnyContentAsMultipartFormData(
    MultipartFormData(
      scala.Predef.Map(
        "name" -> Seq("Ashish Tomer"),
        "email" -> Seq("ashish1269@gmail.com"),
        "username" -> Seq("ashish1269"),
        "age" -> Seq("45"),
        "confirm" -> Seq("1234567890"),
        "mobile" -> Seq("9876543210"),
        "gender" -> Seq("male"),
        "password" -> Seq("1234567890")
      ),
      Vector(
        FilePart[TemporaryFile](
          key = "file",
          filename = "1321922_STpOMHj.jpg",
          contentType = Some("image/jpeg"),
          ref = TemporaryFile(new java.io.File("/home/knoldus/Pictures/wallpapers/1321922_STpOMHj.jpg")) //uploaded file will go to /tmp/ directory
        )
      ),
      Vector()
    )
  )



  "Signup Controller" should {

    "move to user profile page with right data" in {

      when (configuration.getString("user-type")) thenReturn Some("admin")

      when (cache.get("ashish1269@gmail.com")) thenReturn None

      doReturn(null).when(cache).set("ashish1269@gmail.com","")
      doReturn(Some(List("vijay@lore.com"))).when(cache).get("listOfUsers")

      val controller = new SignupController(verifySignupDataService, cache, configuration)

      val request = FakeRequest(POST, "/signup")
                      .withMultipartFormDataBody(
                        MultipartFormData(
                          scala.Predef.Map(
                          "name" -> Seq("Ashish Tomer"),
                          "email" -> Seq("ashish1269@gmail.com"),
                          "username" -> Seq("ashish1269"),
                          "age" -> Seq("45"),
                          "confirm" -> Seq("1234567890"),
                          "mobile" -> Seq("9876543210"),
                          "gender" -> Seq("Male"),
                          "password" -> Seq("1234567890")
                        ),
                        Vector(
                          FilePart[TemporaryFile](
                            key = "file",
                            filename = "1321922_STpOMHj.jpg",
                            contentType = Some("image/jpeg"),
                            ref = TemporaryFile(new java.io.File("/home/knoldus/Pictures/wallpapers/1321922_STpOMHj.jpg")) //uploaded file will go to /tmp/ directory
                          )
                        ),
                        Vector()
                        )
                      )

      when (verifySignupDataService.verifyData(request.body)) thenReturn Some((textData, fileData))

      val result: Future[Result] = controller.handleSignupForm(request)

      status(result) mustBe 303 //Redirected
    }

    "show the signup form with GET method" in {

      val request = FakeRequest(GET, "/signup")

      val controller = new SignupController(verifySignupDataService , cache , configuration)

      val result: Future[Result] = controller.showSignupForm(request)

      contentAsString(result) must include("<title>Signup</title>")
    }

  }

}


