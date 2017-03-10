package app.controllers

import controllers.ProfileController
import play.api.cache.CacheApi
import play.api.test.FakeRequest
import play.api.test.Helpers.GET
import org.scalatest.mock.MockitoSugar
import org.scalatestplus.play.{OneAppPerSuite, PlaySpec}
import org.mockito.Mockito._
import play.api.mvc.Result
import play.api.test.Helpers._
import play.api.test.Helpers._
import scala.concurrent.Future

/**
  * Created by knoldus on 10/3/17.
  */
class ProfileControllerSpec extends PlaySpec with OneAppPerSuite with MockitoSugar {

  val cache = mock[CacheApi]

  "Profile Controller" should {

    "display profile page#notFoundPage" in {

      //when (cache.get("ashish1269@gmail.com")) thenReturn Some(userDataOption)

      doReturn(Some(userDataOption)).when(cache).get("ashish1269@gmail.com")
      doReturn(Some(List[String]("ashish1269@gmail.com"))).when(cache).get("listOfUsers")

      val request = FakeRequest(GET, "/showProfile").withSession(
        "connected" -> "ashish1269@gmail.com"
      )

      val profileController = new ProfileController(cache)

      val result: Future[Result] = profileController.showProfile(request)

      contentAsString(result) must include("ashsih1269@gmail.com")

    }
  }

  val userDataOption = Map(
    "name" -> "Ashish",
    "email" -> "ashish1269@gmail.com",
    "username" -> "ashish1269",
    "image" -> "file:///home/knoldus/Templates/1321922_STpOMHj.jpg",
    "suspended" -> "no",
    "age" -> "21",
    "mobile" -> "9876543210",
    "userType" -> "admin",
    "gender" -> "male",
    "password" -> "$2a$10$tcrU5TW8mFhcGOsV4PMSgerGJPd.L1yxupSerfQhXC.krDe6W1iaq"
  )
}
