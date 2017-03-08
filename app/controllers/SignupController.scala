package controllers

import java.io.File

import com.google.inject.Inject
import play.api.mvc.{Action, AnyContent, Controller}
import services.VerifySignupDataService
import play.api.cache._
import play.api.Configuration

/**
  * Created by knoldus on 6/3/17.
  */
class SignupController @Inject()(verifySignupDataService: VerifySignupDataService, cache: CacheApi, configuration: Configuration) extends Controller {
  def showSignupForm = Action {
    Ok(views.html.signup("Signup"))
  }

  def handleSignupForm = Action { implicit request =>

    val body: AnyContent = request.body

    val verificationResult = verifySignupDataService.verifyData(body)

    val userTypeOption = configuration.getString("user-type")

    val userType: String = userTypeOption match {
                    case Some(x) => x
                    case None => ""
                  }

    verificationResult match {
      case Some((textResult, fileResult)) => {
        textResult("error") match {
          case Some(x) => {
            Ok(views.html.signupwitherror("Signup", fileResult ++ textResult("error").get, textResult("data").get))
          }
          case None => {
            fileResult("imageError") match {
              case x: String if x.length > 5 => {
                Ok(views.html.signupwitherror("Signup", fileResult ++ textResult("error").get, textResult("data").get))
              }
              case _ => {

                val file: File = new File(fileResult("image"))
                val filePath = "/home/knoldus/Templates/" + file.getName+".png"
                file.renameTo(new File(filePath))

                cache.get(textResult("data").get("email")) match {
                  case Some(_) => {
                    Ok(views.html.signupwitherror("Signup",
                    Map[String, String]("signupError" -> "Email id already registered"),
                      textResult("data").get))
                  }
                  case None => {
                    cache.set(textResult("data").get("email"),
                    Map[String,String]("image" -> filePath, "userType" -> userType) ++ textResult("data").get)

                    cache.set("listOfUsers",
                      textResult("data").get("email") :: cache.get("listOfUsers").getOrElse(List())
                    )

                    println(cache.get("listOfUsers"))

                    Redirect(routes.ProfileController.showProfile())
                    .withSession("connected" -> textResult("data").get("email"))
                  }
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
