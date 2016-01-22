package nl.ordina.jtech.mavendependencygraph.model;

import java.io.Serializable;

public class ArtifactVertex implements Serializable {
    private final int id;
    private final String groupId;
    private final String artifactId;
    private String version;
    private String classifier;
    private ArtifactPackaging type;

    public ArtifactVertex(final String groupId, final String artifactId, final ArtifactPackaging type, final String version, String classifier) {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
        this.type = type;
        this.classifier = classifier;
        this.id = hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ArtifactVertex that = (ArtifactVertex) o;

        if (!groupId.equals(that.groupId)) return false;
        if (!artifactId.equals(that.artifactId)) return false;
        if (!version.equals(that.version)) return false;
        if (classifier != null ? !classifier.equals(that.classifier) : that.classifier != null) return false;
        return type == that.type;

    }

    @Override
    public int hashCode() {
        int result = groupId.hashCode();
        result = 31 * result + artifactId.hashCode();
        result = 31 * result + version.hashCode();
        result = 31 * result + (classifier != null ? classifier.hashCode() : 0);
        result = 31 * result + type.hashCode();
        return result;
    }
}
