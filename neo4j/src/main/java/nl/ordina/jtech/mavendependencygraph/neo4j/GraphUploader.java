package nl.ordina.jtech.mavendependencygraph.neo4j;

import com.google.gson.Gson;
import nl.ordina.jtech.mavendependencygraph.model.DependencyGraph;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Class: GraphUploader
 */
@Path("/dependency")
public class GraphUploader {
    private static final BlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(1000);
    private static final ExecutorService executorService = new ThreadPoolExecutor(1, 1, 1, TimeUnit.HOURS, queue);
    private static final Gson GSON = new Gson();
    private final GraphDatabaseService database;

    public GraphUploader(@Context GraphDatabaseService database) throws IOException, TimeoutException {
        this.database = database;
    }

    @GET
    public String status() {
        return "Queue depth: " + queue.size() + "\n" + executorService.toString();
    }


    @POST
    @Path("/graph")
    public Response uploadSubGraph(final String graphJson) throws IOException, InterruptedException {
        executorService.submit(() -> {
            final DependencyGraph graph = GSON.fromJson(graphJson, DependencyGraph.class);
            try (final Transaction transaction = database.beginTx()) {
                ;
                database.execute(DependencyGraphConverter.inCypher(graph));
                transaction.success();
            }
            return "Ok";
        });

        return Response.ok().entity("Submitted").build();

    }
}
