package nl.ordina.jtech.mavendependencygraph.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Class: DependencyGraph
 */
public class DependencyGraph implements GSonConverter {

    private Set<ArtifactVertex> vertices = new HashSet<>();
    private List<ArtifactEdge> edges = new ArrayList<>();

    public void addDependency(final ArtifactVertex from, final ArtifactVertex dest, final RelationType type) {
        edges.add(new ArtifactEdge(from, dest, type));
        vertices.add(from);
        vertices.add(dest);
    }
}
