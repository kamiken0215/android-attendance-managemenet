package com.kamiken0215.work.Presentation.Login;

import com.kamiken0215.work.Domain.Entities.User;

public interface ILoginView {
    void onLoginSuccess(User user,String name);
    void onLoginError(String message);
    void onSuccessSaveLocal(String userId);
    void onFailureSaveLocal();
    void onSuccessFetchLocal();
    void onFailureFetchLocal();
    void onStarted();
    void resetPassword();
    void showPassword();
    void hidePassword();
    void showLoading();
    void hideLoading();
}
