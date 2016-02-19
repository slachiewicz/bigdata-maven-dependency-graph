/*
 * Copyright (C) 2012 Pieter van der Meer (pieter(at)elucidator.nl)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package nl.ordina.jtech.maven.analyzer.aether;

import java.util.Collection;
import java.util.List;

import org.sonatype.aether.RepositorySystem;
import org.sonatype.aether.RepositorySystemSession;
import org.sonatype.aether.artifact.Artifact;
import org.sonatype.aether.collection.CollectRequest;
import org.sonatype.aether.collection.CollectResult;
import org.sonatype.aether.collection.DependencyCollectionException;
import org.sonatype.aether.graph.Dependency;
import org.sonatype.aether.graph.DependencyFilter;
import org.sonatype.aether.graph.DependencyNode;
import org.sonatype.aether.repository.RemoteRepository;
import org.sonatype.aether.resolution.ArtifactResult;
import org.sonatype.aether.resolution.DependencyRequest;
import org.sonatype.aether.resolution.DependencyResolutionException;
import org.sonatype.aether.resolution.DependencyResult;
import org.sonatype.aether.util.artifact.DefaultArtifact;
import org.sonatype.aether.util.artifact.JavaScopes;
import org.sonatype.aether.util.filter.DependencyFilterUtils;

import nl.ordina.jtech.maven.analyzer.aether.Booter;
import nl.ordina.jtech.maven.analyzer.aether.ConsoleDependencyGraphDumper;
import nl.ordina.jtech.maven.analyzer.aether.FlatDependencyGraphDumper;
import nl.ordina.jtech.maven.analyzer.aether.TransitiveDependencyGraphDumper;

//import org.sonatype.aether.artifact.DefaultArtifact;

/**
 * Resolves a single artifact.
 */
public class ResolveArtifact {

    public static void main(String[] args) throws Exception 
    {
    	original2("org.apache.maven:maven-aether-provider:3.1.0");
//    	juanTest();
//    	juanTest2("org.apache.maven:maven-aether-provider:3.1.0");
    }
    
    public static void juanTest2(String artifactName) throws DependencyCollectionException
    {
//        System.out.println( "------------------------------------------------------------" );
//        System.out.println( ResolveArtifact.class.getSimpleName() );

        RepositorySystem system = Booter.newRepositorySystem();

        RepositorySystemSession session = Booter.newRepositorySystemSession( system );

        Artifact artifact = new DefaultArtifact( artifactName );

        CollectRequest collectRequest = new CollectRequest();
//        collectRequest.setRoot( new Dependency( artifact, "" ) );
        collectRequest.setRoot( new Dependency( artifact, artifactName ) );
        
        // het volgende werkt niet
//        collectRequest.setRepositories( Booter.newRepositories( system, session ) );
        RemoteRepository repo = Booter.newCentralRepository();
        collectRequest.addRepository(repo);
        
        CollectResult collectResult = system.collectDependencies( session, collectRequest );

        collectResult.getRoot().accept( new ConsoleDependencyGraphDumper() );
        
        System.out.println("    /--------------------------------------------------------\\" );
        System.out.println("    Nr: " + collectResult.getRoot().getChildren().size());
        System.out.println("    \\--------------------------------------------------------/" );
    }
    
    
    public static void juanTest1() throws DependencyCollectionException, DependencyResolutionException
    {
        System.out.println("---JPM---------------------------------------------------------");
        System.out.println(ResolveArtifact.class.getSimpleName());

        RepositorySystem system = Booter.newRepositorySystem();

        RepositorySystemSession session = Booter.newRepositorySystemSession(system);

        RemoteRepository repo = Booter.newCentralRepository();


//        DependencyFilter classpathFlter = DependencyFilterUtils.classpathFilter(JavaScopes.COMPILE);

        //DefaultArtifact artifact = new DefaultArtifact("org.apache.maven.shared:maven-dependency-analyzer:1.2");
//        DefaultArtifact artifact = new DefaultArtifact("nl.pieni.maven.dependency-analyzer:maven-dependency-analyzer:0.8");
        
        // D:\m2-repo\com\sun\jersey\jersey-servlet\1.13
//        <groupId>:<artifactId>[:<extension>[:<classifier>]]:<version>, 
        
//        <groupId>commons-fileupload</groupId>
//        <artifactId>commons-fileupload</artifactId>
//        <version>1.3.1</version>
//        DefaultArtifact artifact = new DefaultArtifact("com.sun.jersey.jersey-servlet:jersey-servlet:1.13");

//        DefaultArtifact artifact = new DefaultArtifact("org.apache.maven.plugins:maven-compiler-plugin:2.3");
        DefaultArtifact artifact = new DefaultArtifact("org.apache.maven.plugins:maven-compiler-plugin:2.3");

        CollectRequest collectRequest = new CollectRequest();
        collectRequest.setRoot(new Dependency(artifact, JavaScopes.COMPILE));
        collectRequest.addRepository(repo);

        CollectResult collectResult = system.collectDependencies(session, collectRequest);

        System.out.println("Graph");
        collectResult.getRoot().accept(new ConsoleDependencyGraphDumper());
        System.out.println("Transitive");
        TransitiveDependencyGraphDumper transitiveDependencyGraphDumper = new TransitiveDependencyGraphDumper();
        collectResult.getRoot().accept(transitiveDependencyGraphDumper);
        System.out.println("Flat");
        collectResult.getRoot().accept(new FlatDependencyGraphDumper());

        DependencyRequest dependencyRequest = new DependencyRequest();
//        dependencyRequest.setRoot(new Dependency(artifact, JavaScopes.COMPILE));
//        dependencyRequest.addRepository(repo);
        
        DependencyResult resolveResult = system.resolveDependencies(session, dependencyRequest);
        List<ArtifactResult> artifactResults = resolveResult.getArtifactResults();
        int size = artifactResults.size();
        System.out.println("size: " + size);
        
        List<DependencyNode> children = collectResult.getRoot().getChildren();
        Collection<Artifact> aliases = collectResult.getRoot().getAliases();
        Dependency dependency = collectResult.getRoot().getDependency();
        
        System.out.println("number of children: " + children.size());
        System.out.println("number of aliases : " + aliases.size());
        System.out.println("dependency: " + dependency.getArtifact().getClassifier());

    }
    
    public static void original_00() throws DependencyCollectionException, DependencyResolutionException
    {
        System.out.println("------------------------------------------------------------");
        System.out.println(ResolveArtifact.class.getSimpleName());

        RepositorySystem system = Booter.newRepositorySystem();

        RepositorySystemSession session = Booter.newRepositorySystemSession(system);

        RemoteRepository repo = Booter.newCentralRepository();


//        DependencyFilter classpathFlter = DependencyFilterUtils.classpathFilter(JavaScopes.COMPILE);
        DependencyFilter classpathFlter = DependencyFilterUtils.classpathFilter(JavaScopes.TEST);

        //DefaultArtifact artifact = new DefaultArtifact("org.apache.maven.shared:maven-dependency-analyzer:1.2");
//        DefaultArtifact artifact = new DefaultArtifact("nl.pieni.maven.dependency-analyzer:maven-dependency-analyzer:0.8");
        DefaultArtifact artifact = new DefaultArtifact("org.apache.maven.plugins:maven-compiler-plugin:2.3");

        CollectRequest collectRequest = new CollectRequest();
        collectRequest.setRoot(new Dependency(artifact, JavaScopes.COMPILE));
        collectRequest.addRepository(repo);

        CollectResult collectResult = system.collectDependencies(session, collectRequest);

        System.out.println("Graph");
        collectResult.getRoot().accept(new ConsoleDependencyGraphDumper());
        System.out.println("Transitive");
        TransitiveDependencyGraphDumper transitiveDependencyGraphDumper = new TransitiveDependencyGraphDumper();
        collectResult.getRoot().accept(transitiveDependencyGraphDumper);
        System.out.println("Flat");
        collectResult.getRoot().accept(new FlatDependencyGraphDumper());
	    
    }
    
    
    public static void original2(String artifactCoordinate) throws DependencyCollectionException, DependencyResolutionException
    {
        System.out.println("------------------------------------------------------------");
        System.out.println(ResolveArtifact.class.getSimpleName());

        RepositorySystem system = Booter.newRepositorySystem();

        RepositorySystemSession session = Booter.newRepositorySystemSession(system);

        RemoteRepository repo = Booter.newCentralRepository();


//        DependencyFilter classpathFlter = DependencyFilterUtils.classpathFilter(JavaScopes.COMPILE);

        //DefaultArtifact artifact = new DefaultArtifact("org.apache.maven.shared:maven-dependency-analyzer:1.2");
//        DefaultArtifact artifact = new DefaultArtifact("nl.pieni.maven.dependency-analyzer:maven-dependency-analyzer:0.8");
//        DefaultArtifact artifact = new DefaultArtifact("org.apache.maven.plugins:maven-compiler-plugin:2.3");
        DefaultArtifact artifact = new DefaultArtifact(artifactCoordinate);

        CollectRequest collectRequest = new CollectRequest();
        collectRequest.setRoot(new Dependency(artifact, JavaScopes.COMPILE));
        collectRequest.addRepository(repo);

        CollectResult collectResult = system.collectDependencies(session, collectRequest);

        System.out.println("Graph");
        collectResult.getRoot().accept(new ConsoleDependencyGraphDumper());
        System.out.println("Transitive");
        TransitiveDependencyGraphDumper transitiveDependencyGraphDumper = new TransitiveDependencyGraphDumper();
        collectResult.getRoot().accept(transitiveDependencyGraphDumper);
        System.out.println("Flat");
        collectResult.getRoot().accept(new FlatDependencyGraphDumper());
	    
    }
    

}


