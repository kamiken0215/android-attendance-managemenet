package com.kamiken0215.work.Presentation.Login;

public interface ILoginPresenter {
    void onLogin(String userId, String password);
    void onChangePasswordVisibility(Boolean isOn);
    //結果うけとってからやることりすと
    void showLoading();
    void hideLoading();
    void forgotPassword();
    void successLogin();
    void failureLogin();
}
