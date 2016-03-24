package nl.jpm.org;

import nl.ordina.jtech.mavendependencygraph.model.ArtifactPackaging;
import nl.ordina.jtech.mavendependencygraph.model.ArtifactVertex;
import nl.ordina.jtech.mavendependencygraph.model.DependencyGraph;
import nl.ordina.jtech.mavendependencygraph.model.Scope;
import org.sonatype.aether.graph.Dependency;
import org.sonatype.aether.graph.DependencyNode;
import org.sonatype.aether.graph.DependencyVisitor;

import java.io.PrintStream;


//import org.sonatype.aether.impl.internal.GraphEdge;

public class JTechDependencyVisitor implements DependencyVisitor {

    private PrintStream out;

    private String currentIndent = "";
    private int currentIndentation = 0;

    private DependencyGraph localDependencies;

    private ArtifactVertex firstLevelArtifactVertex;

    public DependencyGraph getLocalDependencies() {
        return localDependencies;
    }

    public void setLocalDependencies(DependencyGraph localDependencies) {
        this.localDependencies = localDependencies;
    }

    public JTechDependencyVisitor() {
        this(null);
    }

    public JTechDependencyVisitor(PrintStream out) {
        this.out = (out != null) ? out : System.out;
    }

    public boolean visitEnter(DependencyNode node) {
        currentIndentation += 1;
        out.println(currentIndent + node + "(" + currentIndentation + ")");

        // get the source node and remember it
        if (currentIndentation == 1) {
            firstLevelArtifactVertex = getArtifactVertexFromArtifactCoordinate(node.getDependency());
        }
        // get the nodes on the second level (the direct dependencies), and add these with the first node to the graph
        else if (currentIndentation == 2) {
            ArtifactVertex secondLevelArtifactVerteX = getArtifactVertexFromArtifactCoordinate(node.getDependency());

            System.out.println("   ----> Source artifact vertex and destination artifact vertex being added...");
            System.out.println("   ----> Adding dependency nr. " + (localDependencies.getEdges().size() + 1));
//			ArtifactEdge artifactEdge = new ArtifactEdge(firstLevelArtifactVertex, secondLevelArtifactVerteX, scope);

            Scope scope = deriveScope(node.getDependency().getScope());

            localDependencies.addDependency(firstLevelArtifactVertex, secondLevelArtifactVerteX, scope);
        } else {
            System.out.println("   ----> Artifact vertex NOT being added...");

        }

        if (currentIndent.length() <= 0) {
            currentIndent = "+- ";
        } else {
            currentIndent = "|  " + currentIndent;
        }
        return true;
    }

    private Scope deriveScope(String s) {
        Scope scope = null;

        switch (s) {
            case "compile":
                scope = Scope.Compile;
                break;
            case "provided":
                scope = Scope.Provided;
                break;
            case "import":
                scope = Scope.Import;
                break;
            case "runtime":
                scope = Scope.Runtime;
                break;
            case "system":
                scope = Scope.System;
                break;
            case "test":
                scope = Scope.Test;
                break;
        }

        System.out.println("scope: " + scope);
        return scope;
    }

    // Ugly; have to derive things from the artifact coordinate string
    private ArtifactVertex getArtifactVertexFromArtifactCoordinate(Dependency dependency) {
        String groupId = dependency.getArtifact().getGroupId();
        String artifactId = dependency.getArtifact().getArtifactId();
        String version = dependency.getArtifact().getVersion();
        ArtifactPackaging packaging = getPackaging(dependency.getArtifact().getExtension());
        String classifier = dependency.getArtifact().getClassifier();
        ArtifactVertex artifactVertex = new ArtifactVertex(groupId, artifactId, packaging, version, classifier);

        System.out.println(" from split -> " + artifactVertex.toString());
        return artifactVertex;
    }

    private ArtifactPackaging getPackaging(String pPackaging) {
        ArtifactPackaging packaging = null;

        switch (pPackaging) {
            case "jar":
                packaging = ArtifactPackaging.Jar;
                break;
            case "war":
                packaging = ArtifactPackaging.War;
                break;
            case "pom":
                packaging = ArtifactPackaging.Pom;
                break;
            case "ear":
                packaging = ArtifactPackaging.Ear;
                break;
        }

        return packaging;
    }

    public boolean visitLeave(DependencyNode node) {
        currentIndent = currentIndent.substring(3, currentIndent.length());
        currentIndentation -= 1;
        return true;
    }

}
