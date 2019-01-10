package test

import java.io.File

import org.apache.commons.io.FileUtils

import scala.collection.JavaConverters._

object test03 {


  def main(args: Array[String]): Unit = {
    getGene


  }

  def getGene = {
    val anno = FileUtils.readLines(new File("D:\\药用植物基因组资源数据库\\Catharanthus_roseus\\filter/EVM.final.gene.gff3")).asScala
    val id = FileUtils.readLines(new File("D:\\药用植物基因组资源数据库\\Catharanthus_roseus\\filter/id.txt")).asScala



    val fit = anno.filter(_.contains("\tgene\t")).map{x=>
      val column = x.split("\t")
      (column.last.drop(3).init,x)
    }.toMap

    val last = id.map{x=>
      fit(x)
    }



    FileUtils.writeLines(new File("D:\\药用植物基因组资源数据库\\Catharanthus_roseus\\filter/gene.xls"), last.asJava)

  }


  def dealId = {
    val anno = FileUtils.readLines(new File("D:\\药用植物基因组资源数据库\\Catharanthus_roseus\\filter/Integrated_Function.annotation.xls")).asScala
    val a  = anno.map(x=>
      x.split("\t").head.split('.').head + "\t" + x.split("\t").tail.mkString("\t"))
    FileUtils.writeLines(new File("D:\\药用植物基因组资源数据库\\Catharanthus_roseus\\filter/anno.xls"),a.asJava)
  }

  def getSeq = {

    val cds = FileUtils.readFileToString(new File("D:\\药用植物基因组资源数据库\\Catharanthus_roseus\\filter/cds.fa"))
    val pep = FileUtils.readFileToString(new File("D:\\药用植物基因组资源数据库\\Catharanthus_roseus\\filter/pep.fa"))
    val id = FileUtils.readLines(new File("D:\\药用植物基因组资源数据库\\Catharanthus_roseus\\filter/id.txt")).asScala

    val cdsmap = cds.split(">").tail.map{x=>
      val column = x.split("\n")
      (column.head,column.last)
    }.toMap

    val pepmap = pep.split(">").tail.map{x=>
      val column = x.split("\n")
      (column.head,column.last)
    }.toMap

    val bu = id.map{x=>
     x + "\t" + cdsmap(x) + "\t" + pepmap(x)
    }

    FileUtils.writeLines(new File("D:\\药用植物基因组资源数据库\\Catharanthus_roseus\\filter/seq.xls"),bu.asJava)
  }

  def filterSeq = {
    val file = FileUtils.readFileToString(new File("D:\\药用植物基因组资源数据库\\Catharanthus_roseus\\03.annotaion\\02.gene-prediction/EVM.final.gene.gff3.pep"))

    val filter =  file.split(">").map{x=>
      val column = x.split("\n")
      val head = column.head.split("gene=").last
      (head,column.tail.mkString)
    }

    val fit2 = filter.groupBy(_._1).map{x=>
      ">" + x._1 + "\n"+ x._2.maxBy(_._2.length)._2
    }.mkString("\n")

    FileUtils.writeStringToFile(new File("D:\\药用植物基因组资源数据库\\Catharanthus_roseus\\03.annotaion\\02.gene-prediction/pep.fa"),fit2)
  }
}
