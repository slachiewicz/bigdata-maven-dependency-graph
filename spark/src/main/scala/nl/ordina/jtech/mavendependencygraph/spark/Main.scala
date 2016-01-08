package nl.ordina.jtech.mavendependencygraph.spark

import org.apache.spark.SparkConf
import org.apache.spark.streaming.{StreamingContext, Seconds}

class Main {

  def main(args: Array[String]): Unit = {
    val host = ???
    val port = ???
    val ssc = new StreamingContext(new SparkConf, Seconds(30))
    val dstream = ssc.socketTextStream(host,port)

    //TODO logic

    ssc.start()
    ssc.awaitTermination()
    ssc.stop()
  }
}