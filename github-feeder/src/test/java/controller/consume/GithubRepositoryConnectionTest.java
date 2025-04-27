package controller.consume;

import com.arthead.controller.consume.GithubConnection;
import org.jsoup.Connection;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;

public class GithubRepositoryConnectionTest {

    private GithubConnection connection;

    @Before
    public void setUp() {
        String endpoint = "https://api.github.com/repos/octocat/Hello-World";
        String token = "API_KEY";
        Map<String, String> queries = Map.of();
        connection = new GithubConnection(token, endpoint, queries);
    }

    @Test
    public void createConnectionTest(){
        Connection httpConnection = connection.createConnection();
        Assert.assertNotNull(httpConnection);
        Assert.assertEquals("https://api.github.com/repos/octocat/Hello-World",
                httpConnection.request().url().toString());
    }

    @Test
    public void testConnectionResponse() {
        try {
            Connection.Response response = connection.createConnection().execute();
            Assert.assertEquals(200, response.statusCode());
        } catch (IOException e) {
            Assert.fail("No debería lanzar una excepción: " + e.getMessage());
        }
    }
}
