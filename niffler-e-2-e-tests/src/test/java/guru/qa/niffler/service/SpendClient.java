package guru.qa.niffler.service;

import guru.qa.niffler.model.SpendJson;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public interface SpendClient {
  @Nonnull
  SpendJson createSpend(SpendJson spend);
}
