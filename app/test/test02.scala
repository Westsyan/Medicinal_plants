package test

import java.io.File

import org.apache.commons.io.FileUtils

import scala.collection.JavaConverters._

object test02 {


  def main(args: Array[String]): Unit = {

    val anno = FileUtils.readLines(new File("D:\\药用植物基因组资源数据库\\gansi_紫芝\\02.prediction/geneModels.gff")).asScala

    val ids =  FileUtils.readLines(new File("D:\\药用植物基因组资源数据库\\gansi_紫芝\\02.prediction/id.txt")).asScala


    val row = ids.map{x=>
      x + "\t" + anno.find(_.contains(x)).get.split("\t").init.mkString("\t")
    }

    FileUtils.writeLines(new File("D:\\药用植物基因组资源数据库\\gansi_紫芝\\02.prediction/last.txt"),row.asJava)





  }
}
