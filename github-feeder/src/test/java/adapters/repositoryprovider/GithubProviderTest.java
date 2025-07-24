package adapters.repositoryprovider;

import com.arthead.githubfeeder.domain.GithubResponse;
import com.arthead.githubfeeder.domain.Information;
import com.arthead.githubfeeder.domain.Repository;
import com.arthead.githubfeeder.infrastructure.adapters.repositoryprovider.GithubConnection;
import com.arthead.githubfeeder.infrastructure.adapters.repositoryprovider.GithubDeserializer;
import com.arthead.githubfeeder.infrastructure.adapters.repositoryprovider.GithubFetcher;
import com.arthead.githubfeeder.infrastructure.adapters.repositoryprovider.GithubProvider;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public class GithubProviderTest {
    private GithubProvider provider;
    private final Map<String, String> repoQuery = Map.of(
            "owner", "octocat",
            "repo", "Hello-World"
    );

    @Before
    public void setUp() throws URISyntaxException {
        Path filePath = Paths.get(getClass().getClassLoader().getResource("githubApiKey.txt").toURI());
        String apiKey = filePath.toString();
        String baseUrl = "https://api.github.com/repos/";
        GithubConnection connection = new GithubConnection(apiKey, baseUrl);
        GithubFetcher fetcher = new GithubFetcher();
        GithubDeserializer deserializer = new GithubDeserializer();
        provider = new GithubProvider(connection, fetcher, deserializer);
    }

    @Test
    public void provide_ShouldReturnValidGithubData() {
        GithubResponse data = provider.provide(repoQuery);

        Assert.assertNotNull(data);
        Repository repo = data.getRepository();
        Assert.assertNotNull(repo);
        Assert.assertEquals("Hello-World", repo.getName());
        Assert.assertEquals("octocat", repo.getOwner());

        Information info = data.getInformation();
        Assert.assertNotNull(info);
        Assert.assertTrue(info.getStars() >= 0);
        Assert.assertTrue(info.getForks() >= 0);
        Assert.assertTrue(info.getIssuesAndPullRequest() >= 0);
        Assert.assertTrue(info.getWatchers() >= 0);
    }
}
