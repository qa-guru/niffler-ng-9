package guru.qa.niffler.api;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import org.apache.hc.core5.http.HttpStatus;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SpendApiClient {

    private static final String MESSAGE = "Статус код в ответе не 200";
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
        assertEquals(HttpStatus.SC_CREATED, response.code(), MESSAGE);
        return response.body();
    }

    public SpendJson editSpend(SpendJson spendJson) {
        final Response<SpendJson> response;
        try {
            response = spendApi.editSpend(spendJson).execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(HttpStatus.SC_OK, response.code(), MESSAGE);
        return response.body();
    }

    public SpendJson getSpendById(String id, String username) {
        final Response<SpendJson> response;
        try {
            response = spendApi.getSpendById(id, username).execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(HttpStatus.SC_OK, response.code(), MESSAGE);
        return response.body();
    }

    public SpendJson getSpendAll(String username, CurrencyValues filterCurrency, Date from, Date to) {
        final Response<SpendJson> response;
        try {
            response = spendApi.getSpendAll(username, filterCurrency, from, to).execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(HttpStatus.SC_OK, response.code(), MESSAGE);
        return response.body();
    }

    public SpendJson removeSpend(String username, List<String> ids) {
        final Response<SpendJson> response;
        try {
            response = spendApi.removeSpend(username, ids).execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(HttpStatus.SC_OK, response.code(), MESSAGE);
        return response.body();
    }

    public CategoryJson addCategory(CategoryJson categoryJson) {
        final Response<CategoryJson> response;
        try {
            response = spendApi.addCategory(categoryJson).execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(HttpStatus.SC_OK, response.code(), MESSAGE);
        return response.body();
    }

    public CategoryJson updateCategory(CategoryJson categoryJson) {
        final Response<CategoryJson> response;
        try {
            response = spendApi.updateCategory(categoryJson).execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(HttpStatus.SC_OK, response.code(), MESSAGE);
        return response.body();
    }

    public CategoryJson getAllCategories(String username, boolean isExcludeArchived) {
        final Response<CategoryJson> response;
        try {
            response = spendApi.getAllCategories(username, isExcludeArchived).execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(HttpStatus.SC_OK, response.code(), MESSAGE);
        return response.body();
    }
}
