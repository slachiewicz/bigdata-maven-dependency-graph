package nl.ordina.jtech.mavendependencygraph.model;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Class: ArtifactVertex
 */
public class ArtifactVertex  {
    private final int id;
    private final String groupId;
    private final String artifactId;
    private String version;
    private ArtifactType type;

    public ArtifactVertex(final String groupId, final String artifactId, final ArtifactType type, final String version) {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
        this.type = type;
        this.id = hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArtifactVertex that = (ArtifactVertex) o;
        return Objects.equals(groupId, that.groupId) &&
                Objects.equals(artifactId, that.artifactId) &&
                Objects.equals(version, that.version) &&
                type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupId, artifactId, version, type);
    }
}
