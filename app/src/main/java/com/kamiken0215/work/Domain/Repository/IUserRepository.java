package com.kamiken0215.work.Domain.Repository;

import com.kamiken0215.work.Domain.Entities.User;

import java.util.List;

public interface IUserRepository {
    List<User> findAllUser();
    void fetchUserBy(final String email, final String password, final RepositoryCallback<User> callback);
    User loginByMailAndPass(String Id, String password);
    void deleteAll();
    void executeDeleteLocalUsers(final LocalDatabaseCallback<Long> callback);
    void executeInsertLocalUser(final User user, final LocalDatabaseCallback<Long> callback);
    void executeFindLocalUser(final String userId, final LocalDatabaseCallback<User> callback);
}
