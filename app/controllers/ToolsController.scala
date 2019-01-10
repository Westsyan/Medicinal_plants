package controllers

import java.io.File
import java.nio.file.Files
import javax.inject.Inject

import dao._
import models.Tables._
import org.apache.commons.io.FileUtils
import play.api.data.Form
import play.api.data.Forms._
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}
import utils.Utils.windowsPath
import utils.{ExecCommand, Utils}

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration


class ToolsController @Inject()(speciesdao: speciesDao) extends Controller {

  def blastBefore = Action {
    Ok(views.html.tools.blast())
  }

  case class blastData(blastType: String, method: String, queryText: String, db: String, evalue: String, wordSize: String, maxTargetSeqs: String)

  val blastForm = Form(
    mapping(
      "blastType" -> text,
      "method" -> text,
      "queryText" -> text,
      "db" -> text,
      "evalue" -> text,
      "wordSize" -> text,
      "maxTargetSeqs" -> text
    )(blastData.apply)(blastData.unapply)
  )

  def blastRun = Action(parse.multipartFormData) { implicit request =>
    val data = blastForm.bindFromRequest.get
    val tmpDir = Files.createTempDirectory("tmpDir").toString
    val seqFile = new File(tmpDir, "seq.fa")
    data.method match {
      case "text" =>
        FileUtils.writeStringToFile(seqFile, data.queryText)
      case "file" =>
        val file = request.body.file("file").get
        file.ref.moveTo(seqFile, replace = true)
    }

    val outXml = new File(tmpDir, "out.xml")
    val outHtml = new File(tmpDir, "out.html")
    val outTable = new File(tmpDir, "table.xls")
    val execCommand = new ExecCommand

    val blastType = data.blastType match {
      case "blastn" => "blastn"
      case "blastp" => "blastp"
      case "blastx" => "blastx"
    }

    val blast2Html = data.blastType match {
      case "blastx" => "blastx2html"
      case _ => "blast2html"
    }

    val database = Utils.path + "/data/" + data.db


    val command1 = Utils.path + "/tools/ncbi-blast-2.6.0+/bin/%s%s -query ".format(blastType, Utils.suffix) + seqFile.getAbsolutePath + " -subject " +
      database + " -outfmt 5 -evalue " + data.evalue + " -max_target_seqs " + data.maxTargetSeqs +
      " -word_size " + data.wordSize + " -out " + outXml.getAbsolutePath
    val command2 = "python " + Utils.path + s"/tools/blast2html/$blast2Html.py -i " + outXml.getAbsolutePath + " -o " + outHtml.getAbsolutePath + " --template %s/tools/blast2html/%s.jinja".format(Utils.path, blastType)
    val btt = Utils.path + "/tools/Blast2table -format 10 -xml " + outXml.getAbsolutePath + " -header -top > " + outTable.getAbsoluteFile
    val bttf = new File(tmpDir, "blastToTable.sh")


    FileUtils.writeStringToFile(bttf, btt)
    val command3 = "sh " + bttf.getAbsoluteFile

    execCommand.exect(Array(command1, command2,command3), tmpDir)
    if (execCommand.isSuccess) {
      val html = FileUtils.readFileToString(outHtml)
      val excel = FileUtils.readFileToString(outTable)
     // val excel = ""
 //     Utils.deleteDirectory(tmpDir)
      Ok(Json.obj("html" -> tmpDir.replaceAll("\\\\","/"), "excel" -> excel))
    } else {
      Utils.deleteDirectory(tmpDir)
      println(execCommand.getErrStr)
      Ok(Json.obj("valid" -> "false", "message" -> execCommand.getErrStr))
    }
  }

  def blastResultBefore(path:String) = Action{
    Ok(views.html.tools.blastResult(path))
  }

  def blastResult(path:String) = Action{
    val html = FileUtils.readFileToString(new File(path + "/out.html"))
    Utils.deleteDirectory(path)
    Ok(Json.obj("html" -> (html + "\n" + Utils.scriptHtml)))
  }


  def geneInfo(id: String,species:String) = Action.async { implicit request =>
    speciesdao.getByGeneId(id,species).map { x =>
      Ok(
        species match{
          case "gansi" => views.html.detailInfo.gansiInfo(x.asInstanceOf[Seq[GansiRow]].head)
          case "roseus" => views.html.detailInfo.roseusInfo(x.asInstanceOf[Seq[RoseusRow]].head)
        }
      )
    }
  }

  def searchBefore = Action{implicit request=>
    Ok(views.html.tools.search())
  }

  def searchFunctionBefore = Action { implicit request =>
    Ok(views.html.tools.searchFunction())
  }

  case class functionData(species: String, text: String)

  val functionForm = Form(
    mapping(
      "species" -> text,
      "text" -> text
    )(functionData.apply)(functionData.unapply)
  )

  def getGansiFunction(row: Seq[GansiRow], input: String): Seq[(String, String)] = {
    row.map {
      case x if x.swissprot.toUpperCase.contains(input) => (x.geneid, "Swissprot hits: <br>" + x.swissprot)
      case x if x.kegg.toUpperCase.contains(input) => (x.geneid, "KEGG metabolic pathways: <br>" + x.kegg)
      case x if x.cog.toUpperCase.contains(input) => (x.geneid, "COG families: <br>" + x.cog)
      case x if x.nr.toUpperCase.contains(input) => (x.geneid, "Nr hits: <br>" + x.nr)
      case x if x.pfam.toUpperCase.contains(input) => (x.geneid, "Pfam domains: <br>" + x.pfam)
    }
  }

  def getRoseusFunction(row: Seq[RoseusRow], input: String): Seq[(String, String)] = {
    row.map {
        case x if x.go.toUpperCase.contains(input) => (x.geneid, "GO_annotation: <br>" + x.go)
        case x if x.kegg.toUpperCase.contains(input) => (x.geneid, "KEGG_annotation: <br>" + x.kegg)
        case x if x.kog.toUpperCase.contains(input) => (x.geneid, "KOG_class: <br>" + x.kog)
        case x if x.kogAnno.toUpperCase.contains(input) => (x.geneid, "KOG_class_annotation: <br>" + x.kogAnno)
        case x if x.swissprot.toUpperCase.contains(input) => (x.geneid, "Swissprot_annotation: <br>" + x.swissprot)
        case x if x.tremal.toUpperCase.contains(input) => (x.geneid, "TrEMBL_annotation: <br>" + x.tremal)
        case x if x.nr.toUpperCase.contains(input) => (x.geneid, "nr_annotation: <br>" + x.nr)
    }
  }



  def searchFunction = Action { implicit request =>
    val data = functionForm.bindFromRequest.get
    val text = data.text
    val result = data.species match {
      case "gansi" => getGansiFunction(Await.result(speciesdao.getByFunction(text), Duration.Inf),text.toUpperCase)
      case "roseus" => getRoseusFunction(Await.result(speciesdao.getRoseusByFunction(text), Duration.Inf),text.toUpperCase)
    }

    val row = result.map { x =>
      val geneid = s"<a href='/medicinalPlants/tools/geneInfo?id=${x._1}&&species=${data.species}' target='_blank'>" + x._1 + "</a>"
      val index = x._2.toUpperCase.indexOf(text.toUpperCase)
      val html = x._2.take(index) + "<span style='color:red;'>" + x._2.slice(index,index+text.length) + "</span>" + x._2.drop(index + text.length)
      Json.obj("geneid" -> geneid, "function" -> html)
    }
    Ok(Json.toJson(row))
  }

  case class geneData(species:String,geneId:String)

  val geneForm = Form(
    mapping(
      "species" -> text,
      "geneId" -> text
    )(geneData.apply)(geneData.unapply)
  )

  def searchById = Action.async{implicit request=>
    val data = geneForm.bindFromRequest.get
    speciesdao.getInfoByIdAndTable(data.geneId,data.species).map{result=>
      val row = result.map{x=>
        val geneid = s"<a href='/medicinalPlants/tools/geneInfo?id=${x._1}&&species=${data.species}' target='_blank'>" + x._1 + "</a>"
        Json.obj("geneid" -> geneid,"species" -> getSpeciesName(data.species),"chr"-> x._2,"strand" -> x._3,"length" -> x._4,"start" -> x._5,"end"->x._6 )
      }
      Ok(Json.toJson(row))
    }
  }

  case class regionData(species:String,chr:String,start:String,end:String)

  val regionForm = Form(
    mapping(
      "species"->text,
      "chr"->text,
      "start"->text,
      "end"->text
    )(regionData.apply)(regionData.unapply)
  )

  def searchByRegion = Action.async{implicit request=>
    val data = regionForm.bindFromRequest.get
    speciesdao.getInfoByRegionAndTable(data.chr,data.start.toLong,data.end.toLong,data.species).map{result=>
      val row = result.map{x=>
        val geneid = s"<a href='/medicinalPlants/tools/geneInfo?id=${x._1}&&species=${data.species}' target='_blank'>" + x._1 + "</a>"
        Json.obj("geneid" -> geneid,"species" -> getSpeciesName(data.species),"chr"-> x._2,"strand" -> x._3,"length" -> x._4,"start" -> x._5,"end"->x._6 )
      }
      Ok(Json.toJson(row))
    }
  }


  def getSpeciesName(species:String) = {
    species match {
      case "gansi" => "Camptotheca acuminata"
      case "roseus" => "Catharanthus roseus"
    }
  }

  case class speciesData(species:String)

  val speciesForm = Form(
    mapping(
      "species" -> text
    )(speciesData.apply)(speciesData.unapply)
  )

  def getAllChr = Action.async{implicit request=>
    val data = speciesForm.bindFromRequest.get
    speciesdao.getAllChr(data.species).map{x=>
      Ok(Json.toJson(x))
    }
  }

  def getAllGeneId = Action.async{implicit request=>
    val data = speciesForm.bindFromRequest.get
    speciesdao.getAllGeneId(data.species).map{x=>
      Ok(Json.toJson(x))
    }
  }

  def seqFetchBefore = Action{implicit request=>
    Ok(views.html.tools.seqFetch())
  }

  case class regData(species:String,region: String)

  val regForm = Form(
    mapping(
      "species" -> text,
      "region" -> text
    )(regData.apply)(regData.unapply)
  )

  def seqRegion = Action { implicit request =>
    val data = regForm.bindFromRequest.get
    val tmpDir = Files.createTempDirectory("tmpDir").toString
    val outFile = new File(tmpDir, "data.txt")
    val execCommand = new ExecCommand
    val command = if(new File(windowsPath).exists()){
      Utils.path + "/tools/samtools-0.1.19/samtools.exe faidx "+ Utils.path + "/data/" + data.species+"/gene.fasta " + data.region
    }else{
      "samtools faidx " + Utils.path + "data.fa " + data.region
    }
    execCommand.execo(command, outFile)
    if (execCommand.isSuccess) {
      val dataStr = FileUtils.readFileToString(outFile)
      Utils.deleteDirectory(tmpDir)
      Ok(Json.obj("valid" -> "true", "data" -> dataStr))
    } else {
      Utils.deleteDirectory(tmpDir)
      Ok(Json.obj("valid" -> "false", "message" -> execCommand.getErrStr))
    }
  }


}
