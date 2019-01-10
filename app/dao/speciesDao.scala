package dao

import javax.inject.Inject

import models.Tables._
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import scala.concurrent.Future

import scala.concurrent.ExecutionContext.Implicits.global

class speciesDao @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends
  HasDatabaseConfigProvider[JdbcProfile]{

  import profile.api._

  def insertGansi(row : Seq[GansiRow]) : Future[Unit] = {
    db.run(Gansi ++= row).map(_=>())
  }

  def insertRoseus(row : Seq[RoseusRow]) : Future[Unit] = {
    db.run(Roseus ++= row).map(_=>())
  }



  def getAll : Future[Seq[GansiRow]] = {
    db.run(Gansi.result)
  }

  def getAllRoseus : Future[Seq[RoseusRow]] = {
    db.run(Roseus.result)
  }

  def getByFunction(input:String) : Future[Seq[GansiRow]] = {
    val sql = sql"select * from gansi where concat(swissprot,kegg,cog,nr,pfam) like '%#$input%'".as[GansiRow]
    db.run(sql)
  }

  def getRoseusByFunction(input:String) : Future[Seq[RoseusRow]] = {
    val sql = sql"select * from roseus where concat(go,kegg,kog,kog_anno,swissprot,tremal,nr) like '%#$input%'".as[RoseusRow]
    db.run(sql)
  }

  def getByGeneId(id:String,table:String) : Future[Any] = {
    val sql = table match {
      case "gansi" => sql"select * from #$table where  geneid = '#$id'".as[GansiRow]
      case "roseus" => sql"select * from #$table where  geneid = '#$id'".as[RoseusRow]
    }
    db.run(sql)
  }

  def getInfoByIdAndTable(id:String,table:String) : Future[Seq[(String,String,String,Long,Long,Long)]] ={
    val geneids =  id.split(",").map(_.trim).distinct.mkString("','")
    val sql = sql"select geneid,chr,strand,length,start,end from #$table where  geneid in ('#$geneids')".
      as[(String,String,String,Long,Long,Long)]
    db.run(sql)
  }

  def getInfoByRegionAndTable(chr:String,start:Long,end:Long,table:String) : Future[Seq[(String,String,String,Long,Long,Long)]] ={
    val sql = sql"select geneid,chr,strand,length,start,end from #$table where chr = '#$chr' and start >= #$start and end <= #$end".
      as[(String,String,String,Long,Long,Long)]
    db.run(sql)
  }

  def getAllChr(table:String) : Future[Seq[String]] ={
    val sql = sql"select DISTINCT chr from #$table".as[String]
    db.run(sql)
  }

  def getAllGeneId(table:String) : Future[Seq[String]] ={
    val sql = sql"select DISTINCT geneid from #$table".as[String]
    db.run(sql)
  }

}
