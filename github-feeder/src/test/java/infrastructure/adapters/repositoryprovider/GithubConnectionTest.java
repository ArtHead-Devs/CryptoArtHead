package infrastructure.adapters.repositoryprovider;

import com.arthead.githubfeeder.infrastructure.adapters.repositoryprovider.GithubConnection;
import org.jsoup.Connection;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class GithubConnectionTest {
    private GithubConnection connection;
    private final String owner = "octocat";
    private final String repo = "Hello-World";

    @Before
    public void setUp() throws URISyntaxException {
        String baseUrl = "https://api.github.com/repos/";
        Path filePath = Paths.get(getClass().getClassLoader().getResource("githubApiKey.txt").toURI());
        String apiKey = filePath.toString();
        connection = new GithubConnection(apiKey, baseUrl);
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
