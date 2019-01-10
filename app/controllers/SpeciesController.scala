package controllers

import java.io.File
import javax.inject.Inject

import dao._
import org.apache.commons.io.FileUtils
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}
import models.Tables._
import play.api.data.Form
import play.api.data.Forms._
import utils.Utils

import scala.collection.JavaConverters._
import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

case class PageData(limit: Int, offset: Int, order: String, search: Option[String], sort: Option[String])

class SpeciesController @Inject()(speciesdao: speciesDao) extends Controller {


  def insertGansi = Action.async {
    val anno = FileUtils.readLines(new File("D:\\药用植物基因组资源数据库\\gansi_紫芝\\03.annotation/anno.xls")).asScala
    val row = anno.map { x =>
      val c = x.split("\t")
      GansiRow(0, c(0), c(1), c(3).toLong - c(2).toLong, c(2).toLong, c(3).toLong, c(4), c(5), c(6), c(7), c(8), c(9), c(10), c(11))
    }
    speciesdao.insertGansi(row).map { x =>
      Ok(Json.toJson("1"))
    }
  }

  def insertRoseus = Action.async{
    val anno = FileUtils.readLines(new File("D:\\药用植物基因组资源数据库\\Catharanthus_roseus\\filter/anno.xls")).asScala
    val row = anno.map { x =>
      val c = x.split("\t")
      RoseusRow(0, c(0), c(1), c(3).toLong - c(2).toLong, c(2).toLong, c(3).toLong, c(4), c(5), c(6), c(7), c(8), c(9), c(10), c(11),c(12),c(13))
    }
    speciesdao.insertRoseus(row).map { x =>
      Ok(Json.toJson("1"))
    }
  }

  val pageForm = Form(
    mapping(
      "limit" -> number,
      "offset" -> number,
      "order" -> text,
      "search" -> optional(text),
      "sort" -> optional(text)
    )(PageData.apply)(PageData.unapply)
  )

  def getAllGansi = Action {implicit request=>
    val page = pageForm.bindFromRequest.get
    if (Utils.data.isEmpty) {
      Utils.data = Await.result(speciesdao.getAll, Duration.Inf)
    }
    val x = Utils.data
    val orderX = Utils.dealDataByPage(x, page)
    val total = orderX.size
    val tmpX = orderX.slice(page.offset, page.offset + page.limit)
    val array = tmpX.asInstanceOf[Seq[GansiRow]].map{x=>
      val geneid = s"<a href='/medicinalPlants/tools/geneInfo?id=${x.geneid}&&species=gansi' target='_blank'>" + x.geneid + "</a>"
      Json.obj("geneid" -> geneid,"chr"-> x.chr,"strand" -> x.strand,"length" -> x.length,"start" -> x.start,"end"->x.end,
        "swissprot" -> x.swissprot,"kegg" -> x.kegg,"cog"->x.cog,"nr"->x.nr,"pfam" -> x.pfam)
    }
    Ok(Json.obj("rows" -> array, "total" -> total))
  }


  def getAllRoseus = Action {implicit request=>
    val page = pageForm.bindFromRequest.get
    if (Utils.data.isEmpty) {
      Utils.data = Await.result(speciesdao.getAllRoseus, Duration.Inf)
    }
    val x = Utils.data
    val orderX = Utils.dealDataByPage(x, page)
    val total = orderX.size
    val tmpX = orderX.slice(page.offset, page.offset + page.limit)
    val array = tmpX.asInstanceOf[Seq[RoseusRow]].map{x=>
      val geneid = s"<a href='/medicinalPlants/tools/geneInfo?id=${x.geneid}&&species=roseus' target='_blank'>" + x.geneid + "</a>"
      Json.obj("geneid" -> geneid,"chr"-> x.chr,"strand" -> x.strand,"length" -> x.length,"start" -> x.start,"end"->x.end,
        "go" -> x.go,"kegg" -> x.kegg,"kog"->x.kog,"kog_anno"->x.kogAnno,"swissprot" -> x.swissprot,"tremal" -> x.tremal,
      "nr" -> x.nr)
    }
    Ok(Json.obj("rows" -> array, "total" -> total))
  }



}
