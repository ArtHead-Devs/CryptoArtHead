package controller.consume;

import com.arthead.controller.consume.GithubConnection;
import com.arthead.controller.consume.GithubFetcher;
import org.jsoup.Connection;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;

import static org.junit.Assert.assertTrue;

public class GithubFetcherTest {

    @Test
    public void fetcherTest() throws IOException {
        String token = "API_KEY";
        String endpoint = "https://api.github.com/repos/octocat/Hello-World";
        Map<String, String> queries = Map.of();

        GithubConnection githubConnection = new GithubConnection(token, endpoint, queries);
        Connection httpConnection = githubConnection.createConnection();

        GithubFetcher fetcher = new GithubFetcher();
        String json = fetcher.getResponseJson(httpConnection);

        assertTrue(json.contains("Hello-World"));
        assertTrue(json.contains("octocat"));
    }
}
