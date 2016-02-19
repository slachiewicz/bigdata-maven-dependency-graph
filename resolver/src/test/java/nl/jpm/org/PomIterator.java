package nl.jpm.org;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import nl.ordina.jtech.maven.analyzer.aether.ResolveArtifact;
import nl.ordina.jtech.mavendependencygraph.model.ArtifactVertex;

import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.repository.ArtifactRepositoryPolicy;
import org.apache.maven.artifact.repository.MavenArtifactRepository;
import org.apache.maven.artifact.repository.layout.DefaultRepositoryLayout;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.sonatype.aether.artifact.Artifact;
import org.sonatype.aether.collection.DependencyCollectionException;
import org.sonatype.aether.resolution.DependencyResolutionException;
import org.sonatype.aether.util.artifact.DefaultArtifact;
import org.sonatype.aether.util.artifact.JavaScopes;

import com.jcabi.aether.Aether;

public class PomIterator
{
	public static void main(String[] args) throws Exception
	{

		//  "D:\BDWorkspace\dependency-analyzer\resolver\pom.xml";
//		String classpath = getClasspathFromMavenProject(new File("D:\\worspace_scheiding_2\\ESGRA_INGGIT_1.0.26a_scheiding_02\\forcerest\\forcerest-service\\pom.xml"), new File("D:\\m2-repo"));
		String classpath = getClasspathFromMavenProject(new File("D:\\BDWorkspace\\dependency-analyzer\\testData\\pom.xml"), new File("D:\\m2-repo"));

//		System.out.println("classpath = " + classpath);
	}

	public static String getClasspathFromMavenProject(File projectPom, File localRepoFolder) throws DependencyResolutionException, IOException, XmlPullParserException
	{
		MavenProject proj = loadProject(projectPom);

		//////////////////////////////////
		// Voor credentials see: http://www.programcreek.com/java-api-examples/index.php?api=org.gradle.api.artifacts.repositories.MavenArtifactRepository
		//
		// publishToMavenRepository.getRepository().getCredentials().setUsername( credentials.getUserName() );
		// publishToMavenRepository.getRepository().getCredentials().setPassword( credentials.getPassword() );
		
		proj.setRemoteArtifactRepositories(Arrays.asList((ArtifactRepository) new MavenArtifactRepository("maven-central", "http://repo1.maven.org/maven2/",
		        new DefaultRepositoryLayout(), new ArtifactRepositoryPolicy(), new ArtifactRepositoryPolicy())));

		String classpath = "";
		Aether aether = new Aether(proj, localRepoFolder);

		List<org.apache.maven.model.Dependency> dependencies = proj.getDependencies();
		Iterator<org.apache.maven.model.Dependency> it = dependencies.iterator();

		int numberOfDependencies = 0;
		int numberOfArtifacts = 0;
		
		while (it.hasNext())
		{
			numberOfDependencies+=1;
	        System.out.println( "/------------------------------------------------------------\\" );
			org.apache.maven.model.Dependency depend = it.next();
			System.out.println(" dependency nr. " + numberOfDependencies + " --> '" + depend.getGroupId().toString() + "'");

			final Collection<Artifact> deps = aether.resolve(
			        new DefaultArtifact(depend.getGroupId(), depend.getArtifactId(), depend.getClassifier(), depend.getType(), depend.getVersion()), JavaScopes.TEST);

			Iterator<Artifact> artIt = deps.iterator();
			int artifactNumber = 0;
			while (artIt.hasNext())
			{
				numberOfArtifacts+=1;
				artifactNumber+=1;
				
				Artifact art = artIt.next();
//				System.out.println("     artifact " + artifactNumber + " --> '" + art.getArtifactId().toString() + "', '" + art.getVersion().toString() + "'");
				String artifactCoordinate = art.getGroupId().toString() + ":" + art.getArtifactId().toString() + ":" + art.getVersion().toString();
//				System.out.println("              --> '" + artifactCoordinate);
				
				// Converteer artifact-model 
				ArtifactVertex convertedArtifact = ModelConverter.convertArtifact(art);
//				System.out.println(" ---> " + convertedArtifact.toString());
				
				
				try
                {
					// formaat moet zijn: "org.apache.maven:maven-aether-provider:3.1.0"
//	                ResolveArtifact.juanTest2(artifactCoordinate);
					
					// resolving artifact
//					if(false)
						ResolveArtifact.original2(artifactCoordinate);
                }
                catch (DependencyCollectionException e)
                {
	                // TODO Auto-generated catch block
	                e.printStackTrace();
                }

				classpath = classpath + " " + art.getFile().getAbsolutePath();
			}
	        System.out.println( "\\------------------------------------------------------------/" );

			System.out.println("" + numberOfDependencies  + "  " +  numberOfArtifacts + "" );
	        
		}

		return classpath;
	}

	public static MavenProject loadProject(File pomFile) throws IOException, XmlPullParserException
	{
		MavenProject ret = null;
		MavenXpp3Reader mavenReader = new MavenXpp3Reader();

		if (pomFile != null && pomFile.exists())
		{
			FileReader reader = null;

			try
			{
				reader = new FileReader(pomFile);
				Model model = mavenReader.read(reader);
				model.setPomFile(pomFile);

				ret = new MavenProject(model);
			}
			finally
			{
				reader.close();
			}
		}

		return ret;
	}
}