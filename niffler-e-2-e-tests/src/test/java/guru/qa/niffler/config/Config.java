package guru.qa.niffler.config;

import javax.annotation.Nonnull;

public interface Config {

  @Nonnull
  static Config getInstance() {
    return "docker".equals(System.getProperty("test.env"))
        ? DockerConfig.INSTANCE
        : LocalConfig.INSTANCE;
  }

  default String testDatabaseUsername() {
    return "postgres";
  }

  default String testDatabasePassword() {
    return "secret";
  }

  @Nonnull
  String frontUrl();

  @Nonnull
  String authUrl();

  @Nonnull
  String authJdbcUrl();

  @Nonnull
  String gatewayUrl();

  @Nonnull
  String userdataUrl();

  @Nonnull
  String userdataJdbcUrl();

  @Nonnull
  String spendUrl();

  @Nonnull
  String spendJdbcUrl();

  @Nonnull
  String currencyJdbcUrl();

  @Nonnull
  String currencyGrpcAddress();

  @Nonnull
  String userdataGrpcAddress();

  default int currencyGrpcPort() {
    return 8092;
  }

  default int userdataGrpcPort() {
    return 8088;
  }

  @Nonnull
  default String ghUrl() {
    return "https://api.github.com/";
  }
}
