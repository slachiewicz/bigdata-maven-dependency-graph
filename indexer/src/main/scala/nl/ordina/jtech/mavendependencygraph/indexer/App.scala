package nl.ordina.jtech.mavendependencygraph.indexer

import java.io.File
import java.util.Set

import scala.collection.Iterable
import scala.collection.JavaConversions.asScalaSet

import org.apache.maven.index.ArtifactInfo
import org.apache.maven.index.MAVEN

case class Config(indexLocation: File = new File("."), host: String = "localhost", port: Int = 0)

object App {

  def main(args: Array[String]): Unit = {

    val parser = new scopt.OptionParser[Config]("indexer") {
      head("indexer", "")
      opt[File]('i', "indexLocation") required () valueName ("<file>") action { (x, c) =>
        c.copy(indexLocation = x)
      } text ("indexLocation is the local path to the maven index")
      opt[String]('h', "host") required () valueName ("<hostname>") action { (x, c) =>
        c.copy(host = x)
      } text ("host is the hostname of the machine that recieves the data")
      opt[Int]('p', "port") required () valueName ("<port>") action { (x, c) =>
        c.copy(port = x)
      } text ("port is the port to send the data to")
    }
    // parser.parse returns Option[C]
    parser.parse(args, Config()) map { config =>
      (new App(config)).run
    } getOrElse {
      // arguments are bad, usage message will have been displayed
    }

  }

}

class App(config: Config) {
  val searcher = new Searcher(config.indexLocation.getCanonicalPath)
  val sender = new Sender(config.host, config.port)

  def run {
    // update the index
    searcher.update
  
    // send the gav's in batches from 'a' to 'z'
    val totalArtifacts: Int = processArtifacts(searcher, sender, 'a' to 'z', 0)
  
    sender.flush
    sender.close
    println(s"Done. %d artifacts found.".format(totalArtifacts))
  }
  
  def processArtifacts(searcher: Searcher, sender: Sender, chars: Iterable[Char], acc: Int): Int = {
    if (chars.isEmpty) return acc

    // search for artifacts
    val results: Set[ArtifactInfo] = searcher.search(MAVEN.ARTIFACT_ID, chars.head + "*")
    println(s"found %d artifacts starting with the letter %s".format(results.size, chars.head))

    // flush buffer and send batch
    sender.flush
    for (ai <- results) sender.send((ai.groupId, ai.artifactId, ai.version))

    return processArtifacts(searcher, sender, chars.tail, acc + results.size)
  }

}
