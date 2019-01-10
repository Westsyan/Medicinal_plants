package controllers

import play.api.mvc.{Action, Controller}
import utils.Utils

class PlantsController extends Controller{



  def gansiBefore = Action{
    Utils.data = Seq()
    Ok(views.html.listofplants.gansi())
  }

  def catharanthusBefore = Action{
    Utils.data = Seq()
    Ok(views.html.listofplants.catharanthus())
  }
}
