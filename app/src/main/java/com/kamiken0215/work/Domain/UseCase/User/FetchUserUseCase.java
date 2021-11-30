package com.kamiken0215.work.Domain.UseCase.User;

import com.kamiken0215.work.Domain.Entities.User;
import com.kamiken0215.work.Presentation.Login.ILoginPresenter;

import java.util.List;

public class FetchUserUseCase implements IFetchUserUseCase {

    private ILoginPresenter loginPresenter;

    public FetchUserUseCase(ILoginPresenter loginPresenter) {
        this.loginPresenter = loginPresenter;
    }

    @Override
    public void fetchByNameAndPassword(String userName, String password) {
        loginPresenter.showLoading();
        //UserUseCases userUseCases = new UserUseCases(this);
        //userUseCases.fetchByNameAndPassword(userName,password);
    }

    @Override
    public void fetchById(int userId) {

    }

    //return code
    @Override
    public void onFinishFetch(List<User> userData) {
        loginPresenter.hideLoading();
        if(userData.size() > 0){
            //int userId = userData.get(0).getUserId();
            int userId = 1;
            loginPresenter.successLogin();
        }else{
            loginPresenter.failureLogin();
        }
    }

    @Override
    public void onFailure(Throwable t) {
        loginPresenter.hideLoading();
        loginPresenter.failureLogin();
    }
}
