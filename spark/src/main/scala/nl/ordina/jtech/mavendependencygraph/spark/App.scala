package nl.ordina.jtech.mavendependencygraph.spark

import org.apache.spark.SparkConf
import org.apache.spark.streaming.{StreamingContext, Seconds}

object App {

  def main(args: Array[String]): Unit = {
    if(args.length != 2) {
      println(
        """
          |Please provide the following arguments:
          | args0: host
          | args1: port
        """.stripMargin)
    }
    val host = args(0)
    val port = args(1).toInt
    val ssc = new StreamingContext(new SparkConf, Seconds(30))
    val dstream = ssc.socketTextStream(host,port)

    dstream.map({ record => 
      val group, artifact, version = record
      ((group, artifact, version), Array("Hello","World"))
    }).print()
    

    ssc.start()
    ssc.awaitTermination()
    ssc.stop()
  }
}