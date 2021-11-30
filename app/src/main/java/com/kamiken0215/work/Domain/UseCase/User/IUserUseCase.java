package com.kamiken0215.work.Domain.UseCase.User;

import com.kamiken0215.work.Domain.Entities.User;
import com.kamiken0215.work.Domain.Repository.IUserRepository;
import com.kamiken0215.work.Domain.UseCase.ILoginUseCase;

public interface IUserUseCase{

    //CRUD
    void fetchById(String userName);
    User fetchByEmailAndPassword(ILoginUseCase loginUseCase, IUserRepository userRepository, String email, String password);
    void update(User user);
    void delete(String email, String password, String token);
    //CallBack
    void fetchSuccess(User user);
    void fetchFailure();


}
