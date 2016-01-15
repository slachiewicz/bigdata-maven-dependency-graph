package nl.ordina.jtech.mavendependencygraph.neo4j;

import nl.ordina.jtech.mavendependencygraph.model.ArtifactType;
import nl.ordina.jtech.mavendependencygraph.model.ArtifactVertex;
import nl.ordina.jtech.mavendependencygraph.model.DependencyGraph;
import nl.ordina.jtech.mavendependencygraph.model.RelationType;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Class: GraphUploaderTest
 */
public class GraphUploaderTest {

    @Test
    public void testSimple() throws Exception {
        GraphUploader graphUploader = new GraphUploader(null);

        DependencyGraph graph = getGraph();
        graphUploader.uploadSubGraph(graph.toJson());

    }

    private DependencyGraph getGraph() {
        DependencyGraph graph = new DependencyGraph();
        ArtifactVertex javaeeApi = new ArtifactVertex("javax", "javaee-api", ArtifactType.Jar, "7.0");//, provided
        ArtifactVertex javaxMail = new ArtifactVertex("com.sun.mail", "javax.mail", ArtifactType.Jar, "1.5.0");//:provided
        ArtifactVertex activation = new ArtifactVertex("javax.activation", "activation", ArtifactType.Jar, "1.1");//:provided

        DependencyGraph dependencyGraph = new DependencyGraph();
        dependencyGraph.addDependency(javaeeApi, javaxMail, RelationType.Provided);
        dependencyGraph.addDependency(javaxMail, activation, RelationType.Compile);

        return dependencyGraph;
    }
}