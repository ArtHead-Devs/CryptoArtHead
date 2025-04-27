package controller.consume;

import com.arthead.controller.consume.GithubConnection;
import com.arthead.controller.consume.GithubDeserializer;
import com.arthead.controller.consume.GithubFetcher;
import com.arthead.controller.consume.GithubProvider;
import com.arthead.model.Repository;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Map;

public class GithubProviderTest {

    @Test
    public void testProvideReturnsValidRepository() {
        String token = "API_KEY";

        String endpoint = "https://api.github.com/repos/octocat/Hello-World";
        Map<String, String> queries = Map.of();

        GithubConnection connection = new GithubConnection(token, endpoint, queries);
        GithubFetcher fetcher = new GithubFetcher();
        GithubDeserializer deserializer = new GithubDeserializer();

        GithubProvider provider = new GithubProvider(connection, fetcher, deserializer);
        List<Repository> result = provider.provide();

        Assert.assertEquals(1, result.size());
        Repository repo = result.getFirst();
        Assert.assertEquals("Hello-World", repo.getName());
        Assert.assertEquals("octocat", repo.getOwner().getLogin());
    }
}
