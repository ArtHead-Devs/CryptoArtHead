package controller.consume;

import com.arthead.controller.consume.GithubDeserializer;
import com.arthead.model.Information;
import com.arthead.model.Owner;
import com.arthead.model.Repository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class GithubDeserializerTest {

    private String json;
    private final SimpleDateFormat iso8601Format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

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
    public void deserializeTest() throws ParseException {
        GithubDeserializer deserializer = new GithubDeserializer();
        Repository repo = deserializer.deserialize(json);

        Assert.assertEquals("example-repo", repo.getName());
        Assert.assertEquals("This is a sample repository", repo.getDescription());

        Assert.assertEquals(iso8601Format.parse("2021-07-01T10:15:30Z"), repo.getCreateDate());
        Assert.assertEquals(iso8601Format.parse("2021-07-10T12:00:00Z"), repo.getUpdateDate());
        Assert.assertEquals(iso8601Format.parse("2021-07-15T14:45:00Z"), repo.getPushDate());

        Owner owner = repo.getOwner();
        Assert.assertNotNull(owner);
        Assert.assertEquals("johndoe", owner.getLogin());

        Information info = repo.getInformation();
        Assert.assertEquals(42, info.getForks());
        Assert.assertEquals(5, info.getIssues());
        Assert.assertEquals(150, info.getStars());
        Assert.assertEquals(10, info.getWatchers());
    }
}
