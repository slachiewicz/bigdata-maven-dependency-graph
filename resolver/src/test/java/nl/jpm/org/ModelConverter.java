package nl.jpm.org;

import org.sonatype.aether.artifact.Artifact;
import org.sonatype.aether.collection.DependencyCollectionException;
import org.sonatype.aether.resolution.DependencyResolutionException;
import org.sonatype.aether.util.artifact.DefaultArtifact;

import nl.ordina.jtech.maven.analyzer.aether.ResolveArtifact;
import nl.ordina.jtech.mavendependencygraph.model.*;


public class ModelConverter
{

	ArtifactVertex av = null;
	
	
	public static ArtifactVertex convertArtifact(Artifact art)
	{
		ArtifactPackaging packaging = null;
		
		switch(art.getExtension())
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
		
		// packaging kan niet 'null' zijn, ook niet af te leiden
		ArtifactVertex av = new ArtifactVertex(art.getGroupId(), art.getArtifactId(), packaging, art.getVersion(), art.getClassifier());
		return av;
	}
	
	
	public static ArtifactEdge createDependencyInformation()
	{
		
		
		
		return null;
	}
	
	
	
	public static void main(String[] args) throws DependencyCollectionException, DependencyResolutionException
	{
		String artifactCoordinate = "org.apache.maven.plugins:maven-compiler-plugin:2.3";
//		ResolveArtifact.original2(artifactCoordinate);
		
		DefaultArtifact artifact = new DefaultArtifact(artifactCoordinate);
		ArtifactVertex convertedArtifact = convertArtifact(artifact);
		System.out.println("-->" + convertedArtifact.toString());
	}
	
}
