package infrastructure.adapters.repositoryprovider;

import com.arthead.githubfeeder.infrastructure.adapters.repositoryprovider.GithubConnection;
import com.arthead.githubfeeder.infrastructure.adapters.repositoryprovider.GithubFetcher;
import org.jsoup.Connection;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;

public class GithubFetcherTest {
    private GithubConnection connection;
    private final String owner = "octocat";
    private final String repo = "Hello-World";

    @Before
    public void setUp() {
        String apiKey = "API_KEY";
        String urlBase = "https://api.github.com/repos/";
        connection = new GithubConnection(apiKey, urlBase);
    }

    @Test
    public void fetcherTest() throws IOException {
        Connection httpConnection = connection.createConnection(owner, repo);
        GithubFetcher fetcher = new GithubFetcher();
        String json = fetcher.fetcher(httpConnection);
        Assert.assertTrue(json.contains("Hello-World"));
        Assert.assertTrue(json.contains("octocat"));
        Assert.assertTrue(json.contains("stargazers_count"));
    }
}
