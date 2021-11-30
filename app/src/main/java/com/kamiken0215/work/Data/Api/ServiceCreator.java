package com.kamiken0215.work.Data.Api;

import com.kamiken0215.work.Data.DefineApi;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ServiceCreator {
    public <T> T createService(Class<T> serviceClass){

        OkHttpSingleton okHttpSingleton = OkHttpSingleton.getInstance();

        OkHttpClient client = okHttpSingleton.getOkHttpClient();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DefineApi.HOME)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        return retrofit.create(serviceClass);
    }
}
