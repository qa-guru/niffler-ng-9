package guru.qa.niffler.api;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SpendApiClient {

    private static final Config CFG = Config.getInstance();

    private static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(CFG.spendUrl())
            .addConverterFactory(JacksonConverterFactory.create())
            .build();

    private final SpendApi spendApi = retrofit.create(SpendApi.class);

    public SpendJson addSpend(SpendJson spendJson) {
        final Response<SpendJson> response;
        try {
            response = spendApi.addSpend(spendJson).execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
        return response.body();
    }

    public SpendJson getSpend(SpendJson spendJson) {
        final Response<SpendJson> response;
        try {
            response = spendApi.editSpend(spendJson).execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
        return response.body();
    }

    public SpendJson getSpend(String id, String username) {
        final Response<SpendJson> response;
        try {
            response = spendApi.getSpend(id, username).execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
        return response.body();
    }

    public List<SpendJson> getSpends(
            String username,
            CurrencyValues filterCurrency,
            Date from,
            Date to
    ) {
        final Response<List<SpendJson>> response;
        try {
            response = spendApi.getSpends(username, filterCurrency, from, to).execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
        return response.body();
    }

    public void removeSpends(String username, List<String> ids) {
        final Response<Void> response;
        try {
            response = spendApi.removeSpends(username, ids).execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
    }

    public CategoryJson addCategory(CategoryJson category) {
        final Response<CategoryJson> response;
        try {
            response = spendApi.addCategory(category).execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
        return response.body();
    }

    public CategoryJson updateCategory(CategoryJson category) {
        final Response<CategoryJson> response;
        try {
            response = spendApi.updateCategory(category).execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
        return response.body();
    }

    public List<CategoryJson> getCategories(String username, boolean excludeArchived) {
        final Response<List<CategoryJson>> response;
        try {
            response = spendApi.getCategories(username, excludeArchived).execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
        return response.body();
    }
}
