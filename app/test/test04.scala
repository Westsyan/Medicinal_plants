package test

object test04 {


  def main(args: Array[String]): Unit = {
    val x = "K15264|0|vvi:100261190|K15264 putative methyltransferase [EC:2.1.1.-] | (RefSeq) probable 28S rRNA (cytosine-C(5))-methyltransferase"
    println(x.toUpperCase)

    x.getBytes.foreach(println)
    println(x.toUpperCase.contains("Probable".toUpperCase()
    ))
  }
}
