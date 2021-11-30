package com.kamiken0215.work.Data;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ILoginApi {
    @POST("{username}/{password}")
    Call<LoginApiModel> getTryLoginUserInfo(@Path("username") String username, @Path("password") String password);
}
