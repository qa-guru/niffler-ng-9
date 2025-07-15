package guru.qa.niffler.api;

import guru.qa.niffler.model.spend.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.spend.SpendJson;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface SpendApi {

  @POST("internal/spends/add")
  Call<SpendJson> addSpending(@Body SpendJson spending);

  @PATCH("internal/spends/edit")
  Call<SpendJson> editSpending(@Body SpendJson spending);

  @GET("internal/spends/{id}")
  Call<SpendJson> getSpendingById(@Path("id") String spendingId);

  @GET("internal/spends/all")
  Call<List<SpendJson>> getSpendingList(@Query("filterCurrency")CurrencyValues currency);

  @DELETE("internal/spends/remove")
  Call<Void> deleteSpending(@Query("ids") List<String> ids);

  @POST("internal/categories/add")
  Call<CategoryJson> addCategory(@Body CategoryJson category);

  @PATCH("internal/categories/update")
  Call<CategoryJson> editCategory(@Body CategoryJson category);

  @GET("internal/categories/all")
  Call<List<CategoryJson>> getCategoriesList();
}
