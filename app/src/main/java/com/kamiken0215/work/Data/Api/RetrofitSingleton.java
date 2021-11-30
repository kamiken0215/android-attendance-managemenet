package com.kamiken0215.work.Data.Api;

import com.kamiken0215.work.Data.DefineApi;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitSingleton {

    private Retrofit mRetrofit;

    private static RetrofitSingleton retrofitInstance = new RetrofitSingleton();

    public static RetrofitSingleton getRetrofitInstance(){
        return retrofitInstance;
    }

    private OkHttpClient getOkHttpClient() {

        OkHttpClient.Builder okHttpBuilder = new OkHttpClient.Builder();
        okHttpBuilder.connectTimeout(5, TimeUnit.SECONDS);
        okHttpBuilder.readTimeout(2, TimeUnit.SECONDS);
        okHttpBuilder.writeTimeout(2, TimeUnit.SECONDS);
        okHttpBuilder.addInterceptor(getInterceptor());

        //logging
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
        okHttpBuilder.addInterceptor(logging);

        return okHttpBuilder.build();
    }

    private Interceptor getInterceptor() {

        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {

                Request request = chain.request().newBuilder()
                        .header("Accept","Application/json")
                        .method(chain.request().method(),chain.request().body())
                        .build();

                return chain.proceed(request);
            }
        };
    }

    private RetrofitSingleton(){

        OkHttpClient client = getOkHttpClient();
        mRetrofit = new Retrofit.Builder()
                .baseUrl(DefineApi.HOME)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
    }

    public Retrofit getRetrofit() {
        return mRetrofit;
    }


//    private  <T> T createService(Class<T> serviceClass){
//
//        OkHttpClient client = getOkHttpClient();
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(DefineApi.HOME)
//                .addConverterFactory(ScalarsConverterFactory.create())
//                .addConverterFactory(GsonConverterFactory.create())
//                .client(client)
//                .build();
//        return retrofit.create(serviceClass);
//    }

//    private void setCreateService(Class serviceClass){
//        this.serviceClass = serviceClass;
//    }


}
