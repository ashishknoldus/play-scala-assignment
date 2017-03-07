package controllers

import models.UserData
import play.api.data._
import play.api.data.Forms._
import play.api.mvc.{Action, AnyContent, Controller}
import play.mvc.Http.RequestBody
import services.VerifySignupDataService
import java.io.File

/**
  * Created by knoldus on 6/3/17.
  */
class SignupController extends Controller{
  def showSignupForm = Action{
    Ok(views.html.signup("Signup"))
  }

  def handleSignupForm = Action{ implicit request =>

    val body:AnyContent  = request.body

    val verificationResult = (new VerifySignupDataService).verifyData(body)


    verificationResult match {
      case Some((textResult, fileResult)) => {
        textResult("error") match {
          case Some(x) => {
            println("Text error ------" + textResult("error").get)
            Ok(views.html.signupwitherror("Signup", fileResult ++ textResult("error").get, textResult("data").get))
          }
          case None => {
            println("No Text error")
            fileResult("imageError") match {
              case x: String if x.length > 5 => {
                println("Image upload error")
                Ok(views.html.signupwitherror("Signup", fileResult ++ textResult("error").get, textResult("data").get))
              }
              case _ => {
                println("Noooooo error --- user --- "+ textResult("data").get("email"))

                val file:File = new File(fileResult("image"))

                file.renameTo(new File("/home/knoldus/Templates"+file.getName))

                UserData.saveUser(textResult("data").get)
                Redirect(routes.ProfileController.showProfile())
                  .withSession("connected" -> textResult("data").get("email"))
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
