package nl.ordina.jtech.mavendependencygraph.model;

import java.io.Serializable;

public class ArtifactEdge implements Serializable {
    private final int source;
    private final int destination;
    private RelationType relationType;

    public ArtifactEdge(final ArtifactVertex source, final ArtifactVertex destination, final RelationType relationType) {
        this.source = source.hashCode();
        this.destination = destination.hashCode();
        this.relationType = relationType;
    }
}
