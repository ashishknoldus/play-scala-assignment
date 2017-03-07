package services

import java.util.regex.Pattern
import java.util.regex.Matcher
import play.api.libs.Files.TemporaryFile
import play.api.mvc.MultipartFormData.FilePart
import play.api.mvc.{AnyContent, MultipartFormData}
import play.mvc.BodyParser.MultipartFormData

import scala.collection.{Map, mutable}
import scala.collection.mutable.ListBuffer

/**
  * Created by knoldus on 6/3/17.
  */
class VerifySignupDataService {

  def verifyData(userData: AnyContent): Option[(Map[String, Option[Map[String, String]]], Map[String, String])] = {

    val multipartData = userData.asMultipartFormData

    multipartData match {
      case Some(wholeData) => {
        val textData: Map[String, Seq[String]] = wholeData.asFormUrlEncoded
        val fileData = wholeData.file("file")
        Some(verifyText(textData), verifyFileData(fileData.get))
      }
      case None => {
        None
      }
    }
  }

  private def verifyText(textData: Map[String, Seq[String]]): Map[String, Option[Map[String, String]]] = {

    val name = textData("name")(0).trim
    val email = textData("email")(0).trim
    val username = textData("username")(0).trim
    val age = textData("age")(0).trim
    val mobile = textData("mobile")(0).trim
    val gender = textData("gender")(0).trim
    val password = textData("password")(0).trim
    val confirm = textData("confirm")(0).trim

    val dataMap: mutable.Map[String, String] = mutable.Map[String, String]()

    dataMap("name") = name
    dataMap("gender") = gender
    dataMap("username") = username
    dataMap("mobile") = mobile
    dataMap("age") = age
    dataMap("password") = password
    dataMap("email") = email

    val map: mutable.Map[String, String] = mutable.Map[String, String]()

    val emailPattern: String = "^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})$"
    val pattern: Pattern = Pattern.compile(emailPattern)
    val matcher: Matcher = pattern.matcher(email)

    if(name == "" ) {
      map("nameError") = "Name is not provided"
    }

    if(email =="" || !matcher.matches() ) {
      map("emailError") = "Wrong email format or not provided"
    }

    if(age.length < 1 || age.length > 3 || age.toInt < 18 || age.toInt > 60) {
      map("ageError") = "Age should be in between 18 and 60 years"
    }

    if(username == "") {
      map("usernameError") = "Username is incorrect or not provided"
    }

    if(mobile == "" || mobile.length > 10 || mobile.charAt(0).toInt < 7) {
      map("mobileError") = "Wrong mobile number or not provided"
    }

    if(! List[String]("male", "female", "other").contains(gender.toLowerCase) ) {
      map("genderError") = "Wrong option for a gender"
    }

    if(password.length <6 || password.length >16) {
      map("passwordError") = "Password should be of length 6 to 16"
    }

    if(confirm != password) {
      map("confirmError") = "Password doesn't match"
    }

    if(map.nonEmpty) {
      Map[String, Option[Map[String, String]]](
        "error" -> Some(map),
        "data" -> Some(dataMap)
      )
    } else {
      Map[String, Option[Map[String, String]]](
        "error" -> None,
        "data" -> Some(dataMap)
      )
    }
  }

  private def verifyFileData(fileData: FilePart[TemporaryFile]): Map[String, String] = {
    val map = mutable.Map[String, String]()

    fileData.contentType match {
      case Some(x) if x.substring(0,6) == "image/"=> {
        map("image") = fileData.ref.file.getAbsolutePath
        map("imageError") = ""
      }
      case _ => {
        map("image") = ""
        map("imageError") = "Didn't select image file or incorrect format"
      }
    }
    map
  }

}
