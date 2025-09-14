package guru.qa.niffler.api;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.service.SpendClient;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SpendApiClient implements SpendClient {

  private static final Config CFG = Config.getInstance();

  private static final Retrofit retrofit = new Retrofit.Builder()
          .baseUrl(CFG.spendUrl())
          .addConverterFactory(JacksonConverterFactory.create())
          .build();

  private final SpendApi spendApi = retrofit.create(SpendApi.class);

  @Override
  public SpendJson createSpend(SpendJson spend) {
    final Response<SpendJson> response;
    try {
      response = spendApi.addSpend(spend)
              .execute();
    } catch (IOException e) {
      throw new AssertionError(e);}

      assertEquals(201, response.code());
      return response.body();
  }


  @Override
  public SpendJson update(SpendJson spend) {
    try {
      Response<SpendJson> response = spendApi.editSpend(spend).execute();
      if (!response.isSuccessful()) {
        throw new RuntimeException("Failed to update spend, code: " + response.code());
      }
      return response.body();
    } catch (IOException e) {
      throw new RuntimeException("Network error while updating spend", e);
    }
  }

  @Override
  public Optional<SpendJson> findById(UUID id) {
    try {
      Response<SpendJson> response = spendApi.getSpend(id.toString()).execute();
      if (!response.isSuccessful()) {
        throw new RuntimeException("Failed to get spend, code: " + response.code());
      }
      return Optional.of(response.body());
    } catch (IOException e) {
      throw new RuntimeException("Network error while getting spend", e);
    }
  }

  public List<SpendJson> getAllSpends() {
    try {
      Response<List<SpendJson>> response = spendApi.getAllSpends().execute();
      if (!response.isSuccessful()) {
        throw new RuntimeException("Failed to get all spends, code: " + response.code());
      }
      return response.body();
    } catch (IOException e) {
      throw new RuntimeException("Network error while getting all spends", e);
    }
  }

  @Override
  public void remove(SpendJson spend) {
    try {
      Response<Void> response = spendApi.removeSpend(List.of(spend.id().toString())).execute();
      if (!response.isSuccessful()) {
        throw new RuntimeException("Failed to remove spend, code: " + response.code());
      }
    } catch (IOException e) {
      throw new RuntimeException("Network error while removing spend", e);
    }
  }

  @Override
  public CategoryJson createCategory(CategoryJson category) {
    try {
      Response<CategoryJson> response = spendApi.addCategory(category).execute();
      if (!response.isSuccessful()) {
        throw new RuntimeException("Failed to add category, code: " + response.code());
      }
      return response.body();
    } catch (IOException e) {
      throw new RuntimeException("Network error while adding category", e);
    }
  }

  @Override
  public Optional<CategoryJson> findCategoryById(UUID id) {
    throw new UnsupportedOperationException("Operation not supported in API");
  }

  @Override
  public Optional<CategoryJson> findCategoryByUsernameAndCategoryName(String username, String categoryName) {
    throw new UnsupportedOperationException("Operation not supported in API");
  }

  @Override
  public Optional<SpendJson> findByUsernameAndSpendDescription(String username, String description) {
    throw new UnsupportedOperationException("Operation not supported in API");
  }

  @Override
  public void removeCategory(CategoryJson category) {
    throw new UnsupportedOperationException("Operation not supported in API");

  }

  public CategoryJson updateCategory(CategoryJson category) {
    try {
      Response<CategoryJson> response = spendApi.updateCategory(category).execute();
      if (!response.isSuccessful()) {
        throw new RuntimeException("Failed to update category, code: " + response.code());
      }
      return response.body();
    } catch (IOException e) {
      throw new RuntimeException("Network error while updating category", e);
    }
  }

  public List<CategoryJson> getAllCategories() {
    try {
      Response<List<CategoryJson>> response;
      response = spendApi.getAllCategories().execute();
      if (!response.isSuccessful()) {
        throw new RuntimeException("Failed to get all categories, code: " + response.code());
      }
      return response.body();
    } catch (IOException e) {
      throw new RuntimeException("Network error while getting all categories", e);
    }
  }
}

