package com.kamiken0215.work.Presentation.Login;

import com.kamiken0215.work.Domain.UseCase.ILoginUseCase;


public class LoginPresenter implements ILoginPresenter {

    private ILoginView loginView;
    private ILoginUseCase loginUseCase;
    //demo
    private String userId = "1";

    public LoginPresenter(ILoginView loginView, ILoginUseCase loginUseCase) {
        this.loginView = loginView;
        this.loginUseCase = loginUseCase;
    }

    @Override
    public void onLogin(String email, String password) {

        //ILoginUseCase loginUseCase = new LoginUseCase(this);
        int loginCode = loginUseCase.isValidLoginInfo(email,password);

        //to UseCase
        if(loginCode == 0) {
            loginView.onLoginError("You must enter USER ID");
        } else if(loginCode == 1) {
            loginView.onLoginError("You must enter PASSWORD");
        } else if(loginCode == 2) {
            loginView.onLoginError("USER ID length must be greater than 4");
        } else if(loginCode == 3) {
            loginView.onLoginError("PASSWORD length must be greater than 4");
        } else {
            //try login
            loginView.showLoading();
            //receive

            loginUseCase.onLogin(email, password);
        }
    }


    @Override
    public void onChangePasswordVisibility(Boolean isOn) {
        if(isOn){
            loginView.showPassword();
        }else{
            loginView.hidePassword();
        }
    }

    @Override
    public void forgotPassword() {
        loginView.resetPassword();
    }

    @Override
    public void showLoading() {
        loginView.showLoading();
    }

    @Override
    public void hideLoading() {
        loginView.hideLoading();
    }

    public void successLogin() {
        loginView.hideLoading();
        //loginView.onLoginSuccess("SUCCESS");
    }

    public void failureLogin() {
        loginView.hideLoading();
        loginView.onLoginError("ERROR...");
    }
}
