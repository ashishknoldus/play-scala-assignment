package app.services

import org.scalatest.mock.MockitoSugar
import org.scalatestplus.play.{OneAppPerSuite, PlaySpec}
import play.api.libs.Files.TemporaryFile
import play.api.mvc.MultipartFormData.FilePart
import play.api.mvc.{AnyContent, AnyContentAsMultipartFormData, MultipartFormData}
import services.VerifySignupDataService
import org.mindrot.jbcrypt.BCrypt;
import scala.collection.Map

/**
  * Created by knoldus on 9/3/17.
  */

class VerifySignupDataServiceSpec extends PlaySpec with OneAppPerSuite with MockitoSugar{

  "verify signup data service" should {

    "return correct as output with no error with correct data as input" in {

      val verifySignupDataService = new VerifySignupDataService()

      val data: Option[(Map[String, Option[Map[String, String]]], Map[String, String])] = verifySignupDataService.verifyData(anyContent)

      data mustBe a [Some[(Map[String, Option[Map[String, String]]], Map[String, String])]]

      val (mapofTextData: Map[String, Option[Map[String, String]]], mapOfFileData: Map[String, String]) = data.getOrElse(Map[String,String]())

      val errorInText = mapofTextData("error")

      val dataInText: Map[String, String] = mapofTextData("data").getOrElse(Map[String,String]())

      val name = dataInText("name")
      val username = dataInText("username")
      val age = dataInText("age")
      val email = dataInText("email")
      val mobile = dataInText("mobile")
      val gender = dataInText("gender")
      val password = dataInText("password")

      val errorIs = errorInText match {
        case Some(_) => true
        case None => false
      }

      errorIs mustBe(false) //There should be no error in form

      name mustBe dataMap("name")
      username mustBe dataMap("username")
      email mustBe dataMap("email")
      age mustBe dataMap("age")
      mobile mustBe dataMap("mobile")
      gender mustBe dataMap("gender")
      BCrypt.checkpw(dataMap("password"), password) mustBe true

      val imageError = mapOfFileData("imageError")
      val imagePath = mapOfFileData("image")

      imageError mustBe "" //No error in image upload
      imagePath mustBe "file:///home/knoldus/Templates/1321922_STpOMHj.jpg"

    }

    "return error message as output when wrong text data is entered" in {

      val verifySignupDataService = new VerifySignupDataService()

      val data: Option[(Map[String, Option[Map[String, String]]], Map[String, String])] = verifySignupDataService.verifyData(anyContentWithWrongTextData)

      data mustBe a [Some[(Map[String, Option[Map[String, String]]], Map[String, String])]]

      val (mapofTextData: Map[String, Option[Map[String, String]]], mapOfFileData: Map[String, String]) = data.getOrElse(Map[String,String]())

      val errorInText = mapofTextData("error")

      val dataInText: Map[String, String] = mapofTextData("data").getOrElse(Map[String,String]())

      val name = dataInText("name")
      val username = dataInText("username")
      val age = dataInText("age")
      val email = dataInText("email")
      val mobile = dataInText("mobile")
      val gender = dataInText("gender")
      val password = dataInText("password")

      val errorIs = errorInText match {
        case Some(errors: Map[String,String]) => true
        case None => false
      }

      errorIs mustBe true //There should be no error in form

      name must not be dataMap("name")
      username must not be dataMap("username")
      email must not be dataMap("email")
      age must not be dataMap("age")
      mobile must not be dataMap("mobile")
      gender must not be dataMap("gender")

      val imageError = mapOfFileData("imageError")
      val imagePath = mapOfFileData("image")

      imageError mustBe "" //No error in image upload
      imagePath mustBe "file:///home/knoldus/Templates/1321922_STpOMHj.jpg"

    }

    "return error message as output when wrong/no image data is entered" in {

      val verifySignupDataService = new VerifySignupDataService()

      val data: Option[(Map[String, Option[Map[String, String]]], Map[String, String])] = verifySignupDataService.verifyData(anyContetnWithWrongFileData)

      data mustBe a [Some[(Map[String, Option[Map[String, String]]], Map[String, String])]]

      val (mapofTextData: Map[String, Option[Map[String, String]]], mapOfFileData: Map[String, String]) = data.getOrElse(Map[String,String]())

      val errorInText = mapofTextData("error")

      val dataInText: Map[String, String] = mapofTextData("data").getOrElse(Map[String,String]())

      val name = dataInText("name")
      val username = dataInText("username")
      val age = dataInText("age")
      val email = dataInText("email")
      val mobile = dataInText("mobile")
      val gender = dataInText("gender")
      val password = dataInText("password")

      val errorIs = errorInText match {
        case Some(errors: Map[String,String]) => true
        case None => false
      }

      errorIs mustBe false //There should be no error in form

      name mustBe dataMap("name")
      username mustBe dataMap("username")
      email mustBe dataMap("email")
      age mustBe dataMap("age")
      mobile mustBe dataMap("mobile")
      gender mustBe dataMap("gender")

      val imageError = mapOfFileData("imageError")
      val imagePath = mapOfFileData("image")

      imageError mustBe "Didn't select image file or incorrect format" //No error in image upload
      imagePath mustBe ""

    }


  }

  val dataMap: Map[String, String] = Map[String,String](
    "name" -> "Ashish Tomer",
    "username" -> "ashish1269",
    "age" -> "45",
    "email" -> "ashish1269@gmail.com",
    "mobile" -> "9876543210",
    "gender" -> "male",
    "password" -> "1234567890"
  )

  val errorMap: Map[String, String] = Map[String, String]() //No error in textData

  val textData  = Map[String, Option[Map[String, String]]](
    "error" -> Some(errorMap),
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

  val anyContentWithWrongTextData: AnyContent =AnyContentAsMultipartFormData(
    MultipartFormData(
      scala.Predef.Map(
        "name" -> Seq(""), //Wrong name format
        "email" -> Seq("ashish1269#gmail.com"), //Wrong email format
        "username" -> Seq("ashish+=1269"), //Wrong username format
        "age" -> Seq("70"), //Age greater than 60
        "confirm" -> Seq("12345"), //Small password length
        "mobile" -> Seq("987654321023"), //Wrong mob no format
        "gender" -> Seq("maleeee"), //Wrong gender option
        "password" -> Seq("1234567890") //Unmatched error
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

  val anyContetnWithWrongFileData: AnyContent =AnyContentAsMultipartFormData(
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
          filename = "", //No file uploaded
          contentType = Some("application/octet-stream"), //Wrong file type - not image
          ref = TemporaryFile(new java.io.File("/home/knoldus/Pictures/wallpapers/1321922_STpOMHj.jpg")) //uploaded file will go to /tmp/ directory
        )
      ),
      Vector()
    )
  )
}
