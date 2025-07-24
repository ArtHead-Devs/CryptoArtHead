package adapters.repositoryprovider;

import com.arthead.githubfeeder.infrastructure.adapters.repositoryprovider.GithubConnection;
import com.arthead.githubfeeder.infrastructure.adapters.repositoryprovider.GithubFetcher;
import org.jsoup.Connection;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class GithubFetcherTest {
    private GithubConnection connection;

    @Before
    public void setUp() throws URISyntaxException {
        Path filePath = Paths.get(getClass().getClassLoader().getResource("githubApiKey.txt").toURI());
        String apiKey = filePath.toString();
        String urlBase = "https://api.github.com/repos/";
        connection = new GithubConnection(apiKey, urlBase);
    }

    @Test
    public void fetcherTest() throws IOException {
        String owner = "octocat";
        String repo = "Hello-World";
        Connection httpConnection = connection.createConnection(owner, repo);
        GithubFetcher fetcher = new GithubFetcher();
        String json = fetcher.fetcher(httpConnection);
        Assert.assertTrue(json.contains("Hello-World"));
        Assert.assertTrue(json.contains("octocat"));
        Assert.assertTrue(json.contains("stargazers_count"));
    }
}
