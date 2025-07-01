package guru.qa.niffler.api;

import com.fasterxml.jackson.databind.JsonNode;
import guru.qa.niffler.config.Config;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GhApiClient{
    private static final String GH_TOKEN_ENV = "GITHUB_TOKEN";

    private static final Config CFG = Config.getInstance();

    private static final OkHttpClient client = new OkHttpClient.Builder()
            .addInterceptor(new HttpLoggingInterceptor()
                    .setLevel(HttpLoggingInterceptor.Level.HEADERS))
            .build();

    protected static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(CFG.ghUrl())
            .client(client)
            .addConverterFactory(JacksonConverterFactory.create())
            .build();

    private final GhApi ghApi = retrofit.create(GhApi.class);

    public String issueState(String issueNumber){
        final Response<JsonNode> response;
        try{
            response = ghApi.issue(
                    "Bearer " + System.getenv(GH_TOKEN_ENV),
                    issueNumber
            ).execute();
        } catch (IOException e) {
            throw new AssertionError((e));
        }
        assertEquals(200, response.code());
        return response.body().get("state").asText();
    }
}
