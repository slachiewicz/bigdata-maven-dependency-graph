package nl.ordina.jtech.mavendependencygraph.neo4j;

import org.junit.Test;
import org.neo4j.test.SuppressOutput;

/**
 * Class: DependencyGraphConverterTest
 */
public class DependencyGraphConverterTest {


    @Test
    public void testConvert() throws Exception {
        String s = DependencyGraphConverter.inCypher(GraphCreator.getGraph());
        System.out.println(s);
    }

    @Test
    public void testMatch() throws Exception {
        GraphCreator.getGraph().getVertices().stream().map(DependencyGraphConverter::matchVertex).forEach(System.out::println);

    }

    @Test
    public void createNode() throws Exception {
        GraphCreator.getGraph().getVertices().stream().map(DependencyGraphConverter::createVertex).forEach(System.out::println);

    }

    @Test
    public void DumpJson() throws Exception {

        System.out.println("GraphCreator.getGraph().toJson() = " + GraphCreator.getGraph().toJson());
    }

    @Test
    public void relations() throws Exception {
        System.out.println(DependencyGraphConverter.relations(GraphCreator.getGraph()));DependencyGraphConverter.relations(GraphCreator.getGraph());

    }
}