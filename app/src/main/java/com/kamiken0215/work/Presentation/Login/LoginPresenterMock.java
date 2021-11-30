package com.kamiken0215.work.Presentation.Login;

public class LoginPresenterMock implements ILoginPresenter {

    private ILoginView loginView;

    public LoginPresenterMock(ILoginView loginView) {
        this.loginView = loginView;
    }

    @Override
    public void onLogin(String userId, String password) {
        loginView.showLoading();

        int loginCode = -1;

        //to UseCase
        if(loginCode == 0)
            loginView.onLoginError("You must enter USER ID");
        else if(loginCode == 1)
            loginView.onLoginError("You must enter PASSWORD");
        else if(loginCode == 2)
            loginView.onLoginError("USER ID length must be greater than 4");
        else if(loginCode == 3)
            loginView.onLoginError("PASSWORD length must be greater than 4");
        else
            //try login
            if (loginCode == -1) {
                loginView.hideLoading();
                //loginView.onLoginSuccess("SUCCESS");
            } else {
                loginView.hideLoading();
                loginView.onLoginError("ERROR...");
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

    @Override
    public void successLogin() {

    }

    @Override
    public void failureLogin() {

    }
}
