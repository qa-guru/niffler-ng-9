package guru.qa.niffler.api;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.model.spend.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.spend.SpendJson;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SpendApiClient {

  private static final Config CFG = Config.getInstance();

  private static final OkHttpClient client = new OkHttpClient.Builder()
          .addInterceptor(new HttpLoggingInterceptor()
                  .setLevel(HttpLoggingInterceptor.Level.BODY))
          .build();

  private static final Retrofit retrofit = new Retrofit.Builder()
      .baseUrl(CFG.spendUrl())
      .client(client)
      .addConverterFactory(JacksonConverterFactory.create())
      .build();

  private final SpendApi spendApi = retrofit.create(SpendApi.class);

  public SpendJson addSpend(SpendJson spendJson) {
    final Response<SpendJson> response;
    try{
      response = spendApi.addSpending(spendJson)
              .execute();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    assertEquals(201, response.code());
    return response.body();
  }

  public SpendJson editSpend(SpendJson spendJson){
    final Response<SpendJson> response;
    try{
      response = spendApi.editSpending(spendJson)
              .execute();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    assertEquals(200, response.code());
    return response.body();
  }

  public SpendJson getSpendingById(String userId){
    final Response<SpendJson> response;
    try{
      response = spendApi.getSpendingById(userId)
              .execute();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    assertEquals(200, response.code());
    return response.body();
  }

  public List<SpendJson> getSpendingsList(CurrencyValues currency){
    final Response<List<SpendJson>> response;
    try{
      response = spendApi.getSpendingList(currency).execute();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    assertEquals(200, response.code());
    return response.body();
  }

  public Integer deleteSpendingById(List<String> ids){
    final Response<Void> response;
    try{
      response = spendApi.deleteSpending(ids)
              .execute();
    } catch (IOException e){
      throw new AssertionError(e);
    }
    assertEquals(200, response.code());
    return response.code();
  }

  public CategoryJson addCategory(CategoryJson categoryJson) {
    final Response<CategoryJson> response;
    try{
      response = spendApi.addCategory(categoryJson)
              .execute();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    assertEquals(200, response.code());
    return response.body();
  }

  public CategoryJson editCategory(CategoryJson categoryJson){
    final Response<CategoryJson> response;
    try{
      response = spendApi.editCategory(categoryJson)
              .execute();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    assertEquals(200, response.code());
    return response.body();
  }

  public List<CategoryJson> getCategoriesList(){
    final Response<List<CategoryJson>> response;
    try{
      response = spendApi.getCategoriesList().execute();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    assertEquals(200, response.code());
    return response.body();
  }
}
