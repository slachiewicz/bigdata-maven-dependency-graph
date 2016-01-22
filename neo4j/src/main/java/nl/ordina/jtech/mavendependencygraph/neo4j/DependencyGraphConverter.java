package nl.ordina.jtech.mavendependencygraph.neo4j;

import nl.ordina.jtech.mavendependencygraph.model.DependencyGraph;

/**
 * Class: DependencyGraphConverter
 */
public class DependencyGraphConverter {


    /*
merge (n: MavenEntry {
    groupId:"zaa.bb",
    artifactId: "bar",
    version: "1.0.1"})
create unique (n) -[:DEPENDS_ON]->( y: MavenEntry {groupId:"xza.bb",
    artifactId: "bar",
    version: "1.0.1"})
return n, y
     */
}
