package utils

import java.io.File

import scala.sys.process._

class ExecCommand {
  var isSuccess: Boolean = false
  val err = new StringBuilder
  val out = new StringBuilder
  val log = ProcessLogger(out append _ append "\n", err append _ append "\n")

  def exec(command: String) = {
    val exitCode = command ! log
    if (exitCode == 0) isSuccess = true
  }

  def exec(command1: String, command2: String) = {
    val exitCode = (command1 #&& command2) ! log
    if (exitCode == 0) isSuccess = true
  }

  def exec(command1: String, command2: String, command3:String) = {
    val exitCode = (command1 #&& command2 #&& command3) ! log
    if (exitCode == 0) isSuccess = true
  }

  def exec(command1: String, command2: String, command3:String, command4: String) = {
    val exitCode = (command1 #&& command2 #&& command3 #&& command4) ! log
    if (exitCode == 0) isSuccess = true
  }

  def exec(command1: String, command2: String, command3:String, command4: String, command5: String) = {
    val exitCode = (command1 #&& command2 #&& command3 #&& command4 #&& command5) ! log
    if (exitCode == 0) isSuccess = true
  }

  def exec(command1: String, command2: String, command3:String, command4: String, command5: String, command6: String) = {
    val exitCode = (command1 #&& command2 #&& command3 #&& command4 #&& command5 #&& command6) ! log
    if (exitCode == 0) isSuccess = true
  }

  def exec(command1: String, command2: String, command3:String, command4: String, command5: String, command6: String, command7:String) = {
    val exitCode = (command1 #&& command2 #&& command3 #&& command4 #&& command5 #&& command6 #&& command7) ! log
    if (exitCode == 0) isSuccess = true
  }

  def exec(command : Array[String]) = {
    var processBuilder : ProcessBuilder =  null
    for(c <- command){
        processBuilder = processBuilder #&& c
    }
    val exitCode = processBuilder ! log
    if(exitCode == 0) isSuccess = true
  }

  def execo(command: String, outFile: File) = {
    val exitCode = (command #> outFile) ! log
    if (exitCode == 0) isSuccess = true
  }

  def exect(command : Array[String],tmpDir:String) = {
    val exitArray = command.map{x=>
      val exitCode = Process(x,new File(tmpDir)) ! log
      println(getInfo(x,exitCode))
      exitCode
    }
//    if(exitArray.sum == 0) isSuccess = true
    val invalid = exitArray.count(!_.equals(0))
    if( invalid == 0) isSuccess = true
  }

  def getInfo(command:String,exitCode:Int) : String = {
    val commands = command.split(" ").take(3)
    val proname = if(commands.head == "python" || commands.head == "perl" || commands.head == "sh"){
      commands.drop(1).head.split("/").last
    }else if(commands.head == "java"){
      commands.drop(2).head.split("/").last
    }else{
      commands.head.split("/").last
    }
    val info = if(exitCode ==0){
      s"[info] Programe ${proname} run success!"
    }else{
      s"[error] Programe ${proname} run falied!"
    }

    info
  }

  def getErrStr = {
    err.toString()
  }

  def getOutStr = {
    out.toString()
  }


}
