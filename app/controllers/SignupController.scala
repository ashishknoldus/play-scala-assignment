package controllers

import com.google.inject.Inject
import play.api.Configuration
import play.api.cache._
import play.api.mvc.{Action, AnyContent, Controller}
import services.VerifySignupDataService

import scala.collection.Map

/**
  * Created by knoldus on 6/3/17.
  */
class SignupController @Inject()(verifySignupDataService: VerifySignupDataService, cache: CacheApi, configuration: Configuration) extends Controller {
  def showSignupForm = Action {
    Ok(views.html.signup())
  }

  def handleSignupForm = Action { implicit request =>

    val body: AnyContent = request.body

    println("Body : " + body)


    val verificationResult = verifySignupDataService.verifyData(body)
    val userTypeOption = configuration.getString("user-type")

    val userType: String = userTypeOption match {
      case Some(x) => x
      case None => ""
    }

    println(verificationResult)

    verificationResult match {
      case Some((textResult: Map[String, Option[Map[String, String]]], fileResult)) => {
        textResult("error") match {
          case Some(_) =>{

            Ok(views.html.signupwitherror("Signup Text Error", fileResult ++ textResult("error").get, textResult("data").get))
          }

          case None => {
            fileResult("imageError") match {
              case x: String if x.length > 5 => {
                Ok(views.html.signupwitherror("Signup Image Error", fileResult ++ textResult("error").getOrElse(Map[String, String]()), textResult("data").getOrElse(Map[String, String]())))
              }
              case _ => {

                cache.get(textResult("data").get("email")) match {
                  case Some(_) => {
                    Ok(views.html.signupwitherror("Signup Id Taken",
                      Map[String, String]("signupError" -> "Email id already registered"),
                      textResult("data").get))
                  }
                  case None => {
                    cache.set(textResult("data").get("email"),
                      Map[String, String]("image" -> fileResult("image"), "userType" -> userType, "suspended" -> "no") ++ textResult("data").get)

                    cache.set("listOfUsers",
                      textResult("data").get("email") :: cache.get("listOfUsers").getOrElse(List())
                    )

                    Redirect(routes.ProfileController.showProfile())
                      .withSession("connected" -> textResult("data").get("email"))
                  } case _ => Ok("Couldn't find a match")
                }
              }
            }
          }
        }
      }
      case None => {
        Redirect("/signup", 303)
      }
    }
  }
}
