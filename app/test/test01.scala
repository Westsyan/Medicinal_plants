package test

import java.io.File

import org.apache.commons.io.FileUtils

import scala.collection.JavaConverters._

object test01 {


  def main(args: Array[String]): Unit = {
    val file = FileUtils.readLines(new File("D:\\药用植物基因组资源数据库\\gansi_紫芝\\03.annotation/annotation_SP-KE-CO-Nr-IP-PF.txt")).asScala

    val array = file.map{x=>
      val column = x.split("\t")
      val anno =  Array("-","-","-","-","-","-")
      anno(0) = column.head
      column.tail.foreach{ y=>
        val element = y.split(":")
        val tail = element.tail.mkString(":")
        element.head.trim match{
          case "Swissprot hits" => anno(1) = tail
          case "KEGG metabolic pathways" => anno(2) = tail
          case "COG families" =>  anno(3) = tail
          case "Nr hits" => anno(4) = tail
          case "Pfam domains" => anno(5) = tail
        }
      }
      anno.mkString("\t")
    }

    FileUtils.writeLines(new File("D:\\药用植物基因组资源数据库/anno.xls"),array.asJava)



  }
}
