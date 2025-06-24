package guru.qa.niffler.api;

import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.Date;
import java.util.List;

public interface SpendApi {

    String INTERNAL_SPEND_BASE_PATH = "internal/spends/";
    String INTERNAL_CATEGORIES_BASE_PATH = "internal/categories/";

    @POST(INTERNAL_SPEND_BASE_PATH + "add")
    Call<SpendJson> addSpend(@Body SpendJson spending);

    @PATCH(INTERNAL_SPEND_BASE_PATH + "edit")
    Call<SpendJson> editSpend(@Body SpendJson spending);

    @GET(INTERNAL_SPEND_BASE_PATH + "{id}")
    Call<SpendJson> getSpendById(@Path("id") String id,
                                 @Query("username") String username);


    @GET(INTERNAL_SPEND_BASE_PATH + "all")
    Call<SpendJson> getSpendAll(@Query("username") String username,
                                @Query("filterCurrency") CurrencyValues filterCurrency,
                                @Query("from") Date from,
                                @Query("to") Date to);

    @DELETE(INTERNAL_SPEND_BASE_PATH + "remove")
    Call<SpendJson> removeSpend(@Query("username") String username,
                                @Query("ids") List<String> ids);

    @POST(INTERNAL_CATEGORIES_BASE_PATH + "add")
    Call<CategoryJson> addCategory(@Body CategoryJson category);

    @PATCH(INTERNAL_CATEGORIES_BASE_PATH + "update")
    Call<CategoryJson> updateCategory(@Body CategoryJson category);

    @GET(INTERNAL_CATEGORIES_BASE_PATH + "all")
    Call<CategoryJson> getAllCategories(@Query("username") String username,
                                        @Query("excludeArchived") boolean excludeArchived);

}
