package nl.ordina.jtech.mavendependencygraph.neo4j;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.neo4j.graphdb.GraphDatabaseService;

import javax.jms.*;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Class: GraphUploader
 */
@Path("/dependency")
public class GraphUploader {
    private static final String QUEUE_NAME = "DependencyGraphQueue";
    public static final String BROKER_URL = "vm://localhost";
    private final GraphDatabaseService database;
    private Session session;
    private MessageProducer producer;
    private final GrapQueueConsumer queueConsumer;

    public GraphUploader(@Context GraphDatabaseService database) throws IOException, TimeoutException {
        this.database = database;
        queueConsumer = new GrapQueueConsumer(database);
        initializeQueue();
    }

    private void initializeQueue() throws IOException, TimeoutException {
        try {
            // Create a ConnectionFactory
            ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(BROKER_URL);

            // Create a Connection
            Connection connection = connectionFactory.createConnection();
            connection.start();

            // Create a Session
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            // Create the destination (Topic or Queue)
            Destination destination = session.createQueue(QUEUE_NAME);

            // Create a MessageProducer from the Session to the Topic or Queue
            producer = session.createProducer(destination);
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

            MessageConsumer consumer = session.createConsumer(destination);
            consumer.setMessageListener(queueConsumer);


        }
        catch (Exception e) {
            System.out.println("Caught: " + e);
            e.printStackTrace();
        }
    }

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.TEXT_PLAIN)
    @Path("/graph")
    public Response uploadSubGraph(final String graphJson) throws IOException, JMSException {

        TextMessage message = session.createTextMessage(graphJson);

        // Tell the producer to send the message
        producer.send(message);

        // Do stuff with the database
        return Response.ok().build();
    }
}
