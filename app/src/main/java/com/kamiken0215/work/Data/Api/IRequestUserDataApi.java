package com.kamiken0215.work.Data.Api;

import com.kamiken0215.work.Domain.Entities.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface IRequestUserDataApi {
    @POST("users/user/{userName}/{password}")
    Call<List<User>> getUser(@Path("userName") String userName, @Path("password") String password);

    @POST("/login")
    Call<User> login(@Body User user);
}
