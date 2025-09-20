package guru.qa.niffler.api;

import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.model.page.RestPage;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public interface GatewayV2Api {

  @GET("api/v2/friends/all")
  Call<RestPage<UserJson>> allFriends(@Header("Authorization") String bearerToken,
                                      @Query("page") int page,
                                      @Query("size") int size,
                                      @Query("sort") String sort,
                                      @Query("searchQuery") @Nullable String searchQuery);
}
