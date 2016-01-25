package nl.ordina.jtech.mavendependencygraph.neo4j;

import org.junit.Test;
import org.neo4j.graphdb.Result;
import org.neo4j.harness.ServerControls;
import org.neo4j.harness.TestServerBuilders;
import org.neo4j.test.server.HTTP;

import static org.junit.Assert.assertEquals;

/**
 * Class: GraphUploaderTest
 */
public class GraphUploaderTest {


    @Test
    public void testMyExtension() throws Exception {
        // Given
        try (ServerControls server = TestServerBuilders.newInProcessBuilder()
                .withExtension("/myExtension", GraphUploader.class)
                .newServer()) {
            // When
            for (int i = 0; i < 100; i++) {
                HTTP.POST(server.httpURI().resolve("/myExtension/dependency/graph").toString(), HTTP.RawPayload.quotedJson("{\"vertices\":[{\"id\":1178203917,\"groupId\":\"javax.activation\",\"artifactId\":\"activation\",\"version\":\"1.1\",\"classifier\":\"\",\"packaging\":\"Jar\"},{\"id\":1197076530,\"groupId\":\"com.sun.mail\",\"artifactId\":\"javax.mail\",\"version\":\"1.5.0\",\"classifier\":\"\",\"packaging\":\"Jar\"},{\"id\":1778276833,\"groupId\":\"javax\",\"artifactId\":\"javaee-api\",\"version\":\"7.0\",\"classifier\":\"\",\"packaging\":\"Jar\"}],\"edges\":[{\"source\":1778276833,\"destination\":1197076530,\"scope\":\"Provided\"},{\"source\":1197076530,\"destination\":1178203917,\"scope\":\"Provided\"}]}"));
            }
            HTTP.Response response = HTTP.GET(server.httpURI().resolve("/myExtension/dependency").toString());
            System.out.println("response = " + response);


            // Then
            assertEquals(200, response.status());


            Thread.sleep(1800); //Wait for the executor

            Result result = server.graph().execute("MATCH (n) return n");

            System.out.println("result = " + result.resultAsString());

            response = HTTP.GET(server.httpURI().resolve("/myExtension/dependency").toString());
            System.out.println("response = " + response);

        }
    }
}