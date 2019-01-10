package controllers

import java.io.File

import play.api.mvc.{Action, Controller}
import utils.Utils

class HomeController extends Controller{



  def home = Action{
    Ok(views.html.home.index())
  }


  def downloadBefore = Action{implicit request=>
    Ok(views.html.download.download())
  }

  def download(name:String,species:String) = Action{implicit request=>
    val file = new File(Utils.path + "/download/" + species + "/" +name)
    Ok.sendFile(file).withHeaders(
      CACHE_CONTROL -> "max-age=3600",
      CONTENT_DISPOSITION -> ("attachment; filename=" + file.getName),
      CONTENT_TYPE -> "application/x-download"
    )
  }

}
