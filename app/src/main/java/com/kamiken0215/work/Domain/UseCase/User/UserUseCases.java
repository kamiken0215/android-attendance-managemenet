package com.kamiken0215.work.Domain.UseCase.User;

import com.kamiken0215.work.Domain.Entities.User;
import com.kamiken0215.work.Domain.Repository.IUserRepository;
import com.kamiken0215.work.Domain.UseCase.ILoginUseCase;

public class UserUseCases implements IUserUseCase{

    private User user;
    private IUserRepository userRepository;

    public UserUseCases(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }
    //private ILoginUseCase loginUseCase;

    @Override
    public void fetchById(String userName) {
    }

    @Override
    public User fetchByEmailAndPassword(ILoginUseCase loginUseCase, IUserRepository userRepository, String email, String password) {
        //this.loginUseCase = loginUseCase;
        return userRepository.loginByMailAndPass(email,password);

    }

    @Override
    public void update(User user) {

    }

    @Override
    public void delete(String email, String password, String token) {

    }

    @Override
    public void fetchSuccess(User user) {
        this.user = user;
        //loginUseCase.onSuccess();
    }

    @Override
    public void fetchFailure() {
        //loginUseCase.onFailure();
    }
}
