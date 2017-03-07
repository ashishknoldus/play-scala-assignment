package controllers

import java.io.File

import com.google.inject.Inject
import models.UserData
import play.api.mvc.{Action, AnyContent, Controller}
import services.VerifySignupDataService
import play.api.cache._


/**
  * Created by knoldus on 6/3/17.
  */
class SignupController @Inject()(verifySignupDataService: VerifySignupDataService) extends Controller {
  def showSignupForm = Action {
    Ok(views.html.signup("Signup"))
  }

  def handleSignupForm = Action { implicit request =>

    val body: AnyContent = request.body

    val verificationResult = verifySignupDataService.verifyData(body)


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
                val filePath = "/home/knoldus/Templates/" + file.getName
                file.renameTo(new File(filePath))

                UserData.saveUser(textResult("data").get ++ fileResult)
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
