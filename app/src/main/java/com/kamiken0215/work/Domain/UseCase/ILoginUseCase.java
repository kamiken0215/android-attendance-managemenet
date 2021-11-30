package com.kamiken0215.work.Domain.UseCase;

import com.kamiken0215.work.Domain.Entities.User;

public interface ILoginUseCase {
    //written usecase
    void onLogin(String username, String password);

    int isValidLoginInfo(String userId, String password);

    //CallBack
    void fetchSuccess(User user);
    void fetchFailure();
}
