package controllers

import java.io.File
import java.nio.file.Files

import play.api.mvc.{Action, Controller}
import utils.Utils

class UtilsController extends Controller {

  def getImageByPhotoId(id: Int, types: String) = Action { implicit request =>
    val ifModifiedSinceStr = request.headers.get(IF_MODIFIED_SINCE)

    val path = ""

    val file = new File(path)

    val lastModifiedStr = file.lastModified().toString
    val MimeType = "image/png"
    val byteArray = Files.readAllBytes(file.toPath)
    if (ifModifiedSinceStr.isDefined && ifModifiedSinceStr.get == lastModifiedStr) {
      NotModified
    } else {
      Ok(byteArray).as(MimeType).withHeaders(LAST_MODIFIED -> file.lastModified().toString)
    }
  }

  def downloadExample(example: String) = Action { implicit request =>
    val filename = "\"" + example + "\""
    Ok.sendFile(new File(Utils.path + "/example/" + example)).withHeaders(
      //缓存
      CACHE_CONTROL -> "max-age=3600",
      CONTENT_DISPOSITION -> ("attachment; filename=" + filename),
      CONTENT_TYPE -> "application/x-download"
    )
  }

}
