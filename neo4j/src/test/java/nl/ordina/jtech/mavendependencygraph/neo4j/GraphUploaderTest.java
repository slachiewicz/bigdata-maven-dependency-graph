package nl.ordina.jtech.mavendependencygraph.neo4j;

import nl.ordina.jtech.mavendependencygraph.model.DependencyGraph;
import org.junit.Test;

/**
 * Class: GraphUploaderTest
 */
public class GraphUploaderTest {

    @Test
    public void testSimple() throws Exception {
        GraphUploader graphUploader = new GraphUploader(null);

        DependencyGraph graph = GraphCreator.getGraph();
        graphUploader.uploadSubGraph(graph.toJson());

    }
}