package com.kamiken0215.work.Presentation.Login;

public interface ILoginContract {

    interface ILoginPresenter {
        void onLogin(String userId, String password);
        void onChangePasswordVisibility(Boolean isOn);
        //結果うけとってからやることりすと
        void showLoading();
        void hideLoading();
        void forgotPassword();
        void successLogin();
        void failureLogin();
    }

    interface ILoginView {
        void onLoginSuccess(String message);
        void onLoginError(String message);
        void resetPassword();
        void showPassword();
        void hidePassword();
        void showLoading();
        void hideLoading();
    }

}
