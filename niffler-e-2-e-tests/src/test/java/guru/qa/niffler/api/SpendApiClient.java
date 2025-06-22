package guru.qa.niffler.api;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;
import java.util.List;

public class SpendApiClient {

  private static final Config CFG = Config.getInstance();

  private static final Retrofit retrofit = new Retrofit.Builder()
      .baseUrl(CFG.spendUrl())
      .addConverterFactory(JacksonConverterFactory.create())
      .build();

  private final SpendApi spendApi = retrofit.create(SpendApi.class);

  public SpendJson addSpend(SpendJson spend) {
    try {
      Response<SpendJson> response = spendApi.addSpend(spend).execute();
      if (!response.isSuccessful()) {
        throw new ApiException("Failed to create spend. Code: " + response.code());
      }
      return response.body();
    } catch (IOException e) {
      throw new ApiException("Network error while creating spend", e);
    }
  }

  public SpendJson editSpend(SpendJson spend) {
    try {
      Response<SpendJson> response = spendApi.editSpend(spend).execute();
      if (response.code() != 200) {
        throw new ApiException("Failed to edit spend. Code: " + response.code());
      }
      return response.body();
    } catch (IOException e) {
      throw new ApiException("Network error while editing spend", e);
    }
  }

  public SpendJson getSpendById(String id) {
    try {
      Response<SpendJson> response = spendApi.getSpendById(id).execute();
      if (response.code() != 200) {
        throw new ApiException("Spend not found. Code: " + response.code());
      }
      return response.body();
    } catch (IOException e) {
      throw new ApiException("Network error while fetching spend", e);
    }
  }

  public List<SpendJson> getAllSpends(List<String> ids, CurrencyValues currencyFilter) {
    try {
      Response<List<SpendJson>> response = spendApi.getAllSpends(ids, currencyFilter).execute();
      if (response.code() != 200) {
        throw new ApiException("Failed to get spends. Code: " + response.code());
      }
      return response.body();
    } catch (IOException e) {
      throw new ApiException("Network error while fetching spends", e);
    }
  }

  public void deleteSpend(String id) {
    try {
      Response<Void> response = spendApi.removeSpend(id).execute();
      if (response.code() != 200) {
        throw new ApiException("Failed to delete spend. Code: " + response.code());
      }
    } catch (IOException e) {
      throw new ApiException("Network error while deleting spend", e);
    }
  }

  public CategoryJson createCategory(CategoryJson category) {
    try {
      Response<CategoryJson> response = spendApi.addCategory(category).execute();
      if (response.code() != 200) {
        throw new ApiException("Failed to create category. Code: " + response.code());
      }
      return response.body();
    } catch (IOException e) {
      throw new ApiException("Network error while creating category", e);
    }
  }

  public CategoryJson updateCategory(CategoryJson category) {
    try {
      Response<CategoryJson> response = spendApi.updateCategory(category).execute();
      if (response.code() != 200) {
        throw new ApiException("Failed to update category. Code: " + response.code());
      }
      return response.body();
    } catch (IOException e) {
      throw new ApiException("Network error while updating category", e);
    }
  }

  public List<CategoryJson> getAllCategories() {
    try {
      Response<List<CategoryJson>> response = spendApi.getAllCategories().execute();
      if (response.code() != 200) {
        throw new ApiException("Failed to get categories. Code: " + response.code());
      }
      return response.body();
    } catch (IOException e) {
      throw new ApiException("Network error while fetching categories", e);
    }
  }

  public static class ApiException extends RuntimeException {
    public ApiException(String message) {
      super(message);
    }

    public ApiException(String message, Throwable cause) {
      super(message, cause);
    }
  }
}
