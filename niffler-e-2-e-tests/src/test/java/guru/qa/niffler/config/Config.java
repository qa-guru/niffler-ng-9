package guru.qa.niffler.config;

public interface Config {

  static Config getInstance() {
    return LocalConfig.INSTANCE;
  }

  String frontUrl();

  String spendUrl();

  String ghUrl();

  String registerPageUrl();

  String profilePageUrl();

  String friendsPageUrl();

  String allPeoplePageUrl();
}
