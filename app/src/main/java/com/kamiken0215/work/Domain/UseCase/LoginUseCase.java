package com.kamiken0215.work.Domain.UseCase;

import android.text.TextUtils;

import com.kamiken0215.work.Domain.Entities.User;
import com.kamiken0215.work.Domain.Repository.IUserRepository;
import com.kamiken0215.work.Domain.UseCase.User.UserUseCases;

public class LoginUseCase implements ILoginUseCase{

    //private ILoginPresenter loginPresenter;
    private IUserRepository userRepository;

    public LoginUseCase(IUserRepository userRepository) {
        //this.loginPresenter = loginPresenter;
        this.userRepository = userRepository;
    }

    @Override
    public void onLogin(String email,String password) {
        //get one user record
        UserUseCases userUseCases = new UserUseCases(userRepository);
        userUseCases.fetchByEmailAndPassword(this,userRepository,email,password);
        //userRepository.loginByMailAndPass(email,password);
        //userRepository.getUserModel();
        
        //System.out.println(userModel);
    }

    @Override
    public int isValidLoginInfo(String email, String password) {
        //return result message
        //0. Check username is empty
        //1. Check password is empty
        //2. Check username length > 10
        //3. password length > 8
        if (TextUtils.isEmpty(email))
            return 0;
        else if(email.length() < 4)
            return 2;
        else if(password.length() < 4)
            return 3;
        else
            return -1;
    }

    @Override
    public void fetchSuccess(User user) {
        if(user != null) {
            //loginPresenter.successLogin();
        } else {
            //loginPresenter.failureLogin();
        }
    }

    @Override
    public void fetchFailure() {
        //loginPresenter.failureLogin();
    }

}
