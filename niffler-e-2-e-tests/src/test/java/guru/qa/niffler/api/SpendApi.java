package guru.qa.niffler.api;

import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.DeleteSpendingJson;
import guru.qa.niffler.model.SpendJson;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;
import java.util.Map;

public interface SpendApi {

  @POST("internal/spends/add")
  Call<SpendJson> addSpend(@Body SpendJson spending);

  @PATCH("internal/spends/edit")
  Call<SpendJson> editSpend(@Body SpendJson spending);

  @GET("internal/spends/{id}")
  Call<SpendJson> getSpendById(@Path("id") int userId);

  @GET("internal/spends/all")
  Call<List<SpendJson>> getSpendList(@Query("filterCurrency")CurrencyValues currency);

  @DELETE("internal/spends/remove")
  Call<DeleteSpendingJson> deleteSpend(@Query("ids") List<String> ids);

  @POST("internal/categories/add")
  Call<CategoryJson> addCategory(@Body CategoryJson category);

  @PATCH("internal/categories/update")
  Call<CategoryJson> editCategory(@Body CategoryJson category);

  @GET("internal/categories/all")
  Call<List<CategoryJson>> getCategoriesList();
}
