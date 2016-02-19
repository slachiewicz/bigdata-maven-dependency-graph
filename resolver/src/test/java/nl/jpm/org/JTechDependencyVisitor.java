package nl.jpm.org;

import java.io.PrintStream;

import org.sonatype.aether.graph.DependencyNode;
import org.sonatype.aether.graph.DependencyVisitor;

import nl.ordina.jtech.mavendependencygraph.model.ArtifactPackaging;
import nl.ordina.jtech.mavendependencygraph.model.ArtifactVertex;
import nl.ordina.jtech.mavendependencygraph.model.DependencyGraph;


//import org.sonatype.aether.impl.internal.GraphEdge;

public class JTechDependencyVisitor implements DependencyVisitor
{

	private PrintStream out;

	private String currentIndent = "";
	private int currentIndentation = 0;
	
	private DependencyGraph localDependencies;
	
	private ArtifactVertex firstLevelArtifactVertex;
	

	public DependencyGraph getLocalDependencies()
	{
		return localDependencies;
	}

	public void setLocalDependencies(DependencyGraph localDependencies)
	{
		this.localDependencies = localDependencies;
	}

	public JTechDependencyVisitor()
	{
		this(null);
	}

	public JTechDependencyVisitor(PrintStream out)
	{
		this.out = (out != null) ? out : System.out;
	}

	public boolean visitEnter(DependencyNode node)
	{
		currentIndentation += 1;
		out.println(currentIndent + node + "(" + currentIndentation + ")");

		// get the source node and remember it
		if(currentIndentation==1)
		{
			firstLevelArtifactVertex = getArtifactVertexFromArtifactCoordinate(node.getDependency().toString());
		}
		// get the nodes on the second level (the direct dependencies), and add these with the first node to the graph
		else if(currentIndentation==2)
		{
			ArtifactVertex secondLevelArtifactVertes = getArtifactVertexFromArtifactCoordinate(node.getDependency().toString());
		}
		
		if (currentIndent.length() <= 0)
		{
			currentIndent = "+- ";
		}
		else
		{
			currentIndent = "|  " + currentIndent;
		}
//		out.println("" + currentIndentation);
		return true;
	}

	// 
	private ArtifactVertex getArtifactVertexFromArtifactCoordinate(String artifactCoordinate)
	{
		String[] split = artifactCoordinate.split(":");
		
		String groupId              = split[0];
		String artifactId           = split[1];
		String version              = split[3];
		ArtifactPackaging packaging = getPackaging(split[2]);
		String classifier           = "";
		ArtifactVertex artifactVertex = new ArtifactVertex(groupId, artifactId, packaging, version, classifier);
		
//		System.out.println("->" + split.length);
		System.out.println(" from split -> " + artifactVertex.toString());
		return artifactVertex;
	}

	private ArtifactPackaging getPackaging(String pPackaging)
	{
		// TODO Auto-generated method stub
		ArtifactPackaging packaging = null;
		
		switch(pPackaging)
		{
			case "jar": packaging = ArtifactPackaging.Jar;
						break;
			case "war": packaging = ArtifactPackaging.War;
						break;
			case "pom": packaging = ArtifactPackaging.Pom;
						break;
			case "ear": packaging = ArtifactPackaging.Ear;
						break;
		}
		
		
		return packaging;
	}

	public boolean visitLeave(DependencyNode node)
	{
		currentIndent = currentIndent.substring(3, currentIndent.length());
		currentIndentation -= 1;
//		out.println("" + currentIndentation);
		return true;
	}

}
