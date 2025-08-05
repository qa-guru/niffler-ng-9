package guru.qa.niffler.service;

import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.service.impl.SpendDbClient;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public interface SpendClient {
  static SpendClient getInstance() {
    return new SpendDbClient();
  }

  @Nonnull
  SpendJson createSpend(SpendJson spend);
}
