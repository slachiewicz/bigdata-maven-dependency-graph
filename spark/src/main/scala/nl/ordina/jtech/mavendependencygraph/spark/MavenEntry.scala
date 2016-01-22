package nl.ordina.jtech.mavendependencygraph.spark

case class MavenEntry(groupId: String, artifactId: String, version: String, packaging: String, classifier: String)