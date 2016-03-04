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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonatype.aether.RepositorySystem;
import org.sonatype.aether.RepositorySystemSession;
import org.sonatype.aether.artifact.Artifact;
import org.sonatype.aether.collection.CollectRequest;
import org.sonatype.aether.collection.CollectResult;
import org.sonatype.aether.collection.DependencyCollectionException;
import org.sonatype.aether.graph.Dependency;
import org.sonatype.aether.repository.RemoteRepository;
import org.sonatype.aether.util.artifact.DefaultArtifact;
import org.sonatype.aether.util.artifact.JavaScopes;
import org.springframework.stereotype.Component;

import nl.jpm.org.JTechDependencyVisitor;
import nl.ordina.jtech.mavendependencygraph.model.ArtifactEdge;
import nl.ordina.jtech.mavendependencygraph.model.DependencyGraph;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Resolver for artifacts
 */
@Component
public class ArtifactResolver {
    private static final Logger LOGGER = LoggerFactory.getLogger(ArtifactResolver.class);

    private final RepositorySystem system;
    private final RemoteRepository repo;
    private final RepositorySystemSession session;

    public ArtifactResolver() {
        system = Booter.newRepositorySystem();

        session = Booter.newRepositorySystemSession(system);

        repo = Booter.newCentralRepository();
    }

    public List<DependencyResultRecord> resolve(Artifact artifact) throws DependencyCollectionException {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Resolving: " + artifact);
        }

        CollectRequest collectRequest = new CollectRequest();
        collectRequest.setRoot(new Dependency(artifact, JavaScopes.COMPILE));
        collectRequest.addRepository(repo);

        CollectResult collectResult = system.collectDependencies(session, collectRequest);

//        FlatDependencyGraphDumper flat = new FlatDependencyGraphDumper();
//        collectResult.getRoot().accept(flat);
//
        List<DependencyResultRecord> nodes = new ArrayList<DependencyResultRecord>();
//        nodes.addAll(flat.getNodes());

        TransitiveDependencyGraphDumper transitive = new TransitiveDependencyGraphDumper();
        collectResult.getRoot().accept(transitive);
        nodes.addAll(transitive.getNodes());

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Result " + nodes.size() + " depending artifacts.");
        }

        return nodes;
    }
    
    
    public List<ArtifactEdge> resolveToEdges(Artifact artifact) throws DependencyCollectionException {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Resolving: " + artifact);
        }

        CollectRequest collectRequest = new CollectRequest();
        collectRequest.setRoot(new Dependency(artifact, JavaScopes.COMPILE));
        collectRequest.addRepository(repo);

        CollectResult collectResult = system.collectDependencies(session, collectRequest);

//        FlatDependencyGraphDumper flat = new FlatDependencyGraphDumper();
//        collectResult.getRoot().accept(flat);
//
        List<DependencyResultRecord> nodes = new ArrayList<DependencyResultRecord>();
//        nodes.addAll(flat.getNodes());

        TransitiveDependencyGraphDumper transitive = new TransitiveDependencyGraphDumper();
        collectResult.getRoot().accept(transitive);
        nodes.addAll(transitive.getNodes());

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Result " + nodes.size() + " depending artifacts.");
        }

        System.out.println("Flat");
        JTechDependencyVisitor jTechVisitor = new JTechDependencyVisitor();
        DependencyGraph localDependencies = new DependencyGraph();
		jTechVisitor.setLocalDependencies(localDependencies);
		collectResult.getRoot().accept(jTechVisitor);
        
		List<ArtifactEdge> edges = jTechVisitor.getLocalDependencies().getEdges();
		
		// TODO ? Convert back to Artifacts or a derivate from that, or return ArtifactEdges
		for (Iterator iterator = edges.iterator(); iterator.hasNext();)
		{
			ArtifactEdge artifactEdge = (ArtifactEdge) iterator.next();
//			DefaultArtifact source = new DefaultArtifact(groupId, artifactId, extension, version);;
//			DefaultArtifact destination = new DefaultArtifact(groupId, artifactId, extension, version);
//			DependencyResultRecord dependencyResultRecord = new DependencyResultRecord(source, destination, "Compile");
		}
		
		System.out.println("" + edges.size());
		
        return edges;
    } 
    
    
    
    public DependencyGraph resolveToDependencyGraph(Artifact artifact) throws DependencyCollectionException {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Resolving: " + artifact);
        }

        CollectRequest collectRequest = new CollectRequest();
        collectRequest.setRoot(new Dependency(artifact, JavaScopes.COMPILE));
        collectRequest.addRepository(repo);

        CollectResult collectResult = system.collectDependencies(session, collectRequest);

//        FlatDependencyGraphDumper flat = new FlatDependencyGraphDumper();
//        collectResult.getRoot().accept(flat);
//
        List<DependencyResultRecord> nodes = new ArrayList<DependencyResultRecord>();
//        nodes.addAll(flat.getNodes());

        TransitiveDependencyGraphDumper transitive = new TransitiveDependencyGraphDumper();
        collectResult.getRoot().accept(transitive);
        nodes.addAll(transitive.getNodes());

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Result " + nodes.size() + " depending artifacts.");
        }

        System.out.println("Flat");
        JTechDependencyVisitor jTechVisitor = new JTechDependencyVisitor();
        DependencyGraph localDependencies = new DependencyGraph();
		jTechVisitor.setLocalDependencies(localDependencies);
		collectResult.getRoot().accept(jTechVisitor);
        
		List<ArtifactEdge> edges = jTechVisitor.getLocalDependencies().getEdges();
		
		// TODO ? Convert back to Artifacts or a derivate from that, or return ArtifactEdges
		for (Iterator iterator = edges.iterator(); iterator.hasNext();)
		{
			ArtifactEdge artifactEdge = (ArtifactEdge) iterator.next();
//			DefaultArtifact source = new DefaultArtifact(groupId, artifactId, extension, version);;
//			DefaultArtifact destination = new DefaultArtifact(groupId, artifactId, extension, version);
//			DependencyResultRecord dependencyResultRecord = new DependencyResultRecord(source, destination, "Compile");
		}
		
		System.out.println("" + edges.size());
		
        return jTechVisitor.getLocalDependencies();
    }     
    
    
    
    public static void main(String[] args)
	{
    	ArtifactResolver artifactResolver = new ArtifactResolver();
    	try
		{
			String artifactCoordinate = "org.apache.maven.plugins:maven-compiler-plugin:2.3";
			DefaultArtifact artifact = new DefaultArtifact(artifactCoordinate);
			List<ArtifactEdge> resolveToEdges = artifactResolver.resolveToEdges(artifact);
			
			System.out.println("/===========================================\\");
			System.out.println("Num: " + resolveToEdges.size());
		}
		catch (DependencyCollectionException e)
		{
			e.printStackTrace();
		}
	}
    
    
}
