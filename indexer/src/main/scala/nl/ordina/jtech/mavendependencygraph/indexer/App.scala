package nl.ordina.jtech.mavendependencygraph.indexer

import java.util.Set

import scala.collection.Iterable
import scala.collection.JavaConversions.asScalaSet

import org.apache.maven.index.ArtifactInfo
import org.apache.maven.index.MAVEN

object App {
  
  def main(args: Array[String]): Unit = {
    
    val indexLocation: String = args(0)
    val host: String = args(1)
    val port: Int = args(2).toInt
    
    val searcher = new Searcher(indexLocation)
    val sender = new Sender("localhost", 1234)
    
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
      val results: Set[ArtifactInfo] = searcher.search(MAVEN.ARTIFACT_ID, chars.head+"*")
      println(s"found %d artifacts starting with the letter %s".format(results.size, chars.head))

      // flush buffer and send batch
      sender.flush
      for (ai <- results) sender.send((ai.groupId, ai.artifactId, ai.version))
      
      return processArtifacts(searcher, sender, chars.tail, acc + results.size)
  }

}