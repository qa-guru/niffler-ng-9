package guru.qa.niffler.service.impl;

import guru.qa.niffler.api.GatewayV2Api;
import guru.qa.niffler.config.Config;
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

  @Step("Get all friends & income invitations")
  @Nonnull
  public Page<UserJson> allFriends(String bearerToken, int page, int size, String sort, @Nullable String searchQuery) {
    final Response<RestPage<UserJson>> response;
    try {
      response = gatewayApi.allFriends(
          bearerToken,
          page,
          size,
          sort,
          searchQuery
      ).execute();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    assertEquals(200, response.code());
    return Objects.requireNonNull(response.body());
  }
}
