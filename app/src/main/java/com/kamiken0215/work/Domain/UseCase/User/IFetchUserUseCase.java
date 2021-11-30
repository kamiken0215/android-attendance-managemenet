package com.kamiken0215.work.Domain.UseCase.User;

import com.kamiken0215.work.Domain.Entities.User;

import java.util.List;

public interface IFetchUserUseCase {
    void fetchByNameAndPassword(String userName, String password);
    void fetchById(int userId);
    void onFinishFetch(List<User> userData);
    void onFailure(Throwable t);
}
