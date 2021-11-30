package com.kamiken0215.work.Data;

import android.util.Log;

import com.kamiken0215.work.Data.Api.IRequestUserDataApi;
import com.kamiken0215.work.Data.Api.RetrofitSingleton;
import com.kamiken0215.work.Domain.Entities.User;
import com.kamiken0215.work.Domain.Repository.IUserRepository;

import retrofit2.Call;
import retrofit2.Callback;

public class LoginUserDao {

    private User loginUser;
    private IUserRepository userRepository;
    private RetrofitSingleton retrofitSingleton;

    public LoginUserDao(IUserRepository userRepository, RetrofitSingleton retrofitSingleton) {
        this.userRepository = userRepository;
        this.retrofitSingleton = retrofitSingleton;
    }

    public void loginUser(String email, String password) {
        loginUser = new User(email,password);
        //Api
        IRequestUserDataApi service = retrofitSingleton.getRetrofit().create(IRequestUserDataApi.class);
        Call<User> call = service.login(loginUser);
        call.enqueue(new Callback<User>() {

            @Override
            public void onResponse(Call<User> call, retrofit2.Response<User> response) {
                if (response.isSuccessful()){
                    loginUser = response.body();
                }
                else {
                    System.out.println(response.code());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d("api/user_repository","cant get data");
                //userUseCase.fetchFailure();
            }
        });
    }
}
