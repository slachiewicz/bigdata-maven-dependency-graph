package nl.ordina.jtech.mavendependencygraph.spark

//import nl.ordina.jtech.mavendependencygraph.model.DependencyGraph
import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.streaming.{StreamingContext, Seconds}

object App {

  def main(args: Array[String]): Unit = {
    if (args.length != 2) {
      println(
        """
          |Please provide the following arguments:
          | args0: host
          | args1: port
        """.stripMargin)
    }
    val host = args(0)
    val port = args(1).toInt
    val conf = new SparkConf().setMaster("local[2]").setAppName("maven-streaming")
    val ssc = new StreamingContext(conf, Seconds(30))

    val dstream = ssc.socketTextStream(host, port)
    dstream.map(parseMavenEntry).print()
//           .map(resolveSubGraph)
//           .foreachRDD(sendGraphToNeo _)

    ssc.start()
    ssc.awaitTermination()
    ssc.stop()
  }

  def parseMavenEntry(record: String): MavenEntry = {
    val splits = record.split(";")
    val groupId = splits(0)
    val artifactId = splits(1)
    val version = splits(2)
    val packaging = splits(3)
    val classifier = splits(4)
    MavenEntry(groupId,artifactId,version,packaging,classifier)
  }

//  def resolveSubGraph(mavenEntry: MavenEntry): DependencyGraph = ??? //TODO: Call resolver
//
//  def sendGraphToNeo(graph: RDD[DependencyGraph]): Unit = ??? //TODO: Send to Neo
}