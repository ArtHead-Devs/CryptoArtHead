package controller.consumer;

import com.arthead.controller.consume.GithubConnection;
import org.jsoup.Connection;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class GithubConnectionTest {

    private GithubConnection connection;
    private final String owner = "octocat";
    private final String repo = "Hello-World";

    @Before
    public void setUp() {
        String baseUrl = "https://api.github.com/repos/";
        String token = "API_KEY";
        connection = new GithubConnection(token, baseUrl);
    }

    @Test
    public void createConnectionTest() {
        Connection httpConnection = connection.createConnection(owner, repo);
        Assert.assertNotNull(httpConnection);
        Assert.assertEquals("https://api.github.com/repos/octocat/Hello-World",
                httpConnection.request().url().toString());
    }

    @Test
    public void testConnectionResponse() throws IOException {
        Connection.Response response = connection.createConnection(owner, repo).execute();
        Assert.assertEquals(200, response.statusCode());
    }
}
