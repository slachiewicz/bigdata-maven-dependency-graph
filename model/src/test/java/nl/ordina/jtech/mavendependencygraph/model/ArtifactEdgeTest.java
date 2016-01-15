package nl.ordina.jtech.mavendependencygraph.model;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Class: ArtifactEdgeTest
 */
public class ArtifactEdgeTest {

    @Test
    public void testToJson() throws Exception {
        ArtifactEdge artifactEdge = new ArtifactEdge(new ArtifactVertex("a", "b", ArtifactType.Jar, "1.0"), new ArtifactVertex("b", "c", ArtifactType.Jar, "2.0"), RelationType.Compile);
        System.out.println("artifactEdge = " + artifactEdge.toJson());
    }


}