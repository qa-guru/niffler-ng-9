package guru.qa.niffler.service;

import guru.qa.niffler.model.UserJson;

public interface UserClient {
    public UserJson createUser(String username, String password);
    public void addIncomeInvitation(UserJson targetUser, int count);
    public void addOutcomeInvitation(UserJson targetUser, int count);
    void addFriends(UserJson targetUser, int count);

}
