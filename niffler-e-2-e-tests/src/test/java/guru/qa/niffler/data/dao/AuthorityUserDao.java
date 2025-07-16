package guru.qa.niffler.data.dao;

import guru.qa.niffler.data.entity.auth.AuthorityUserEntity;

public interface AuthorityUserDao {
    AuthorityUserEntity create(AuthorityUserEntity authority);
}
