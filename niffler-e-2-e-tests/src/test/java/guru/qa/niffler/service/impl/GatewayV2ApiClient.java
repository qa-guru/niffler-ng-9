package guru.qa.niffler.service.impl;

import guru.qa.niffler.api.GatewayV2Api;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.DataFilterValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.model.page.RestPage;
import guru.qa.niffler.service.RestClient;
import io.qameta.allure.Step;
import org.springframework.data.domain.Page;
import retrofit2.Response;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ParametersAreNonnullByDefault
public class GatewayV2ApiClient extends RestClient {

  private static final Config CFG = Config.getInstance();
  private final GatewayV2Api gatewayApi;

  public GatewayV2ApiClient() {
    super(CFG.gatewayUrl());
    this.gatewayApi = create(GatewayV2Api.class);
  }

  @Step("Send REST GET('/api/v2/friends/all') request to niffler-gateway")
  public @Nonnull Page<UserJson> allFriends(String bearerToken,
                                            int page,
                                            int size,
                                            @Nullable List<String> sort,
                                            @Nullable String searchQuery) {
    return executeForBody(
        gatewayApi.allFriends(bearerToken, page, size, sort, searchQuery),
        200
    );
  }

  @Step("Send REST GET('/api/v2/users/all') request to niffler-gateway")
  public @Nonnull Page<UserJson> allUsers(String bearerToken,
                                          int page,
                                          int size,
                                          @Nullable List<String> sort,
                                          @Nullable String searchQuery) {
    return executeForBody(
        gatewayApi.allUsers(bearerToken, page, size, sort, searchQuery),
        200
    );
  }

  @Step("Send REST GET('/api/v2/spends/all') request to niffler-gateway")
  public @Nonnull RestPage<SpendJson> allSpendsPageable(String bearerToken,
                                                        @Nullable CurrencyValues currency,
                                                        @Nullable DataFilterValues filterPeriod,
                                                        @Nullable Integer page,
                                                        @Nullable Integer size,
                                                        @Nullable List<String> sort,
                                                        @Nullable String searchQuery) {
    return executeForBody(
        gatewayApi.allSpends(bearerToken, currency, filterPeriod, page, size, sort, searchQuery),
        200
    );
  }
}
