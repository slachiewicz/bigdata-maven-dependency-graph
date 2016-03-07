package nl.ordina.jtech.mavendependencygraph.spark

import nl.ordina.jtech.maven.analyzer.aether.ArtifactResolver
import nl.ordina.jtech.mavendependencygraph.model.DependencyGraph
import org.sonatype.aether.util.artifact.DefaultArtifact
import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.streaming.{StreamingContext, Seconds}

import scalaj.http.Http

object App {

  def main(args: Array[String]): Unit = {
    val (host, port, url) = parseInputArgs(args)
    val conf = new SparkConf().setMaster("local").setAppName("maven-streaming")
    val ssc = new StreamingContext(conf, Seconds(30))

    val dstream = ssc.socketTextStream(host, port)
    dstream.map(record => MavenEntry(record))
      .map(resolveSubGraph)
      .foreachRDD(graphRDD => sendGraphToNeo(graphRDD, url))

    ssc.start()
    ssc.awaitTermination()
    ssc.stop()
  }

  def resolveSubGraph(mavenEntry: MavenEntry): DependencyGraph = {
    val resolver : ArtifactResolver = new ArtifactResolver()
    val artifactCoordinate = mavenEntry.groupId + ":" + mavenEntry.artifactId + ":" + mavenEntry.version
    resolver.resolveToDependencyGraph(new DefaultArtifact(artifactCoordinate))
  }

  def sendGraphToNeo(graphs: RDD[DependencyGraph], url: String): Unit = {
    graphs.foreach(graph => {
      Http(url).postData(graph.toJson).asString
    })
  }

  def parseInputArgs(args: Array[String]): (String, Int, String) = {
    if (args.length != 3) {
      println(
        """
          |Please provide the following arguments:
          | args0: host
          | args1: port
          | args2: neo endpoint url
        """.stripMargin)
    }
    val host = args(0)
    val port = args(1).toInt
    val url = args(2)
    (host, port, url)
  }
}