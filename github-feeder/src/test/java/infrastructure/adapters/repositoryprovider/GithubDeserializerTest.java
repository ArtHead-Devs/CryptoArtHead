package infrastructure.adapters.repositoryprovider;

import com.arthead.githubfeeder.infrastructure.adapters.repositoryprovider.GithubDeserializer;
import com.arthead.githubfeeder.domain.GithubResponse;
import com.arthead.githubfeeder.domain.Information;
import com.arthead.githubfeeder.domain.Repository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class GithubDeserializerTest {

    private String json;

    @Before
    public void setUp() {
        json = "{\n" +
                "  \"name\": \"example-repo\",\n" +
                "  \"owner\": {\n" +
                "    \"login\": \"johndoe\"\n" +
                "  },\n" +
                "  \"description\": \"This is a sample repository\",\n" +
                "  \"created_at\": \"2021-07-01T10:15:30Z\",\n" +
                "  \"updated_at\": \"2021-07-10T12:00:00Z\",\n" +
                "  \"pushed_at\": \"2021-07-15T14:45:00Z\",\n" +
                "  \"forks_count\": 42,\n" +
                "  \"open_issues_count\": 5,\n" +
                "  \"stargazers_count\": 150,\n" +
                "  \"subscribers_count\": 10\n" +
                "}";
    }

    @Test
    public void deserializeTest() {
        GithubDeserializer deserializer = new GithubDeserializer();
        GithubResponse githubResponse = deserializer.deserialize(json);

        Repository repo = githubResponse.getRepository();
        Assert.assertEquals("example-repo", repo.getName());
        Assert.assertEquals("johndoe", repo.getOwner());
        Assert.assertEquals("This is a sample repository", repo.getDescription());

        Assert.assertEquals("2021-07-01T10:15:30Z", repo.getCreateDate());
        Assert.assertEquals("2021-07-10T12:00:00Z", repo.getUpdateDate());
        Assert.assertEquals("2021-07-15T14:45:00Z", repo.getPushDate());

        Information info = githubResponse.getInformation();
        Assert.assertEquals("example-repo", info.getName());
        Assert.assertEquals(150, info.getStars());
        Assert.assertEquals(42, info.getForks());
        Assert.assertEquals(5, info.getIssuesAndPullRequest());
        Assert.assertEquals(10, info.getWatchers());

        Assert.assertEquals("Github", info.getSs());
        Assert.assertNotNull(info.getTs());
        Assert.assertNotNull(repo.getTs());
        Assert.assertEquals("Github", repo.getSs());
    }
}
