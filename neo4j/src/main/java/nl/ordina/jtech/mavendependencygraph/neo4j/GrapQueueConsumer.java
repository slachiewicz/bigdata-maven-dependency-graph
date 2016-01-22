package nl.ordina.jtech.mavendependencygraph.neo4j;

import com.google.gson.Gson;
import nl.ordina.jtech.mavendependencygraph.model.DependencyGraph;
import org.neo4j.graphdb.GraphDatabaseService;

import javax.jms.*;

/**
 * Class: GrapQueueConsumer
 */
public class GrapQueueConsumer implements MessageListener {

    public static final Gson GSON = new Gson();
    private GraphDatabaseService database;

    /**
     * Constructs a new instance and records its association to the passed-in channel.
     *
     * @param channel the channel to which this consumer is attached
     * @param database
     */
    public GrapQueueConsumer(GraphDatabaseService database) {
        this.database = database;
    }


    @Override
    public void onMessage(Message message) {
        System.out.println("message = " + message);
        TextMessage data = (TextMessage) message;
        DependencyGraph dependencyGraph = null;
        try {
            dependencyGraph = GSON.fromJson(data.getText(), DependencyGraph.class);
        } catch (JMSException e) {
            e.printStackTrace();
        }
        System.out.println("dependencyGraph = " + dependencyGraph.toJson());
    }
}
