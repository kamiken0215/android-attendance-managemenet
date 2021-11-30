package com.kamiken0215.work.Data.Repository;

import android.app.Application;
import android.util.Log;

import com.kamiken0215.work.Data.Api.IRequestUserDataApi;
import com.kamiken0215.work.Data.Api.RetrofitSingleton;
import com.kamiken0215.work.Data.Dao.IUserDao;
import com.kamiken0215.work.Data.DateResource.UserDatabase;
import com.kamiken0215.work.Domain.Entities.User;
import com.kamiken0215.work.Domain.Repository.IUserRepository;
import com.kamiken0215.work.Domain.Repository.LocalDatabaseCallback;
import com.kamiken0215.work.Domain.Repository.RepositoryCallback;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;

public class UserRepository implements IUserRepository{

    private RetrofitSingleton retrofitSingleton;
    private IUserDao userDao;
    private List<User> allUser;

    public UserRepository(Application application, RetrofitSingleton retrofitSingleton) {
        this.retrofitSingleton = retrofitSingleton;
        UserDatabase userDatabase = UserDatabase.getInstance(application);
        userDao = userDatabase.userDao();
        //init();
    }

    @Override
    public void fetchUserBy(final String email, final String password, final RepositoryCallback<User> callback) {
        final User loginUser = new User(email,password);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                IRequestUserDataApi service = retrofitSingleton.getRetrofit().create(IRequestUserDataApi.class);
                Call<User> call = service.login(loginUser);
                call.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, retrofit2.Response<User> response) {
                        if(response.isSuccessful()) {
                            //update or insert to local database
//                            if (allUser.size() > 0) {
//                                executeUpdateUser(response.body());
//                            } else {
//                                executeInsertUser(response.body());
//                            }
                            callback.onComplete(response,null);
                        } else {
                            callback.onComplete(response,null);
                        }
                    }
                    //Couldn't access
                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Log.d("api/user_repository","cant get data");
                        System.out.println(t);
                        callback.onComplete(null,t);
                    }
                });
            }
        });
    }


    @Override
    public List<User> findAllUser() {
        return userDao.findAll();
    }

    @Override
    public void deleteAll() {
        executeDeleteAllUser();
    }

    @Override
    public void executeDeleteLocalUsers(final LocalDatabaseCallback<Long> callback) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                userDao.deleteAll();
                callback.onComplete((long) 1);
            }
        });

    }


    @Override
    public User loginByMailAndPass(String Id, String password) {
        return null;
    }


    @Override
    public void executeFindLocalUser(final String userId, final LocalDatabaseCallback<User> callback) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                callback.onComplete(userDao.findById(userId));
            }
        });
    }

    public void executeInsertLocalUser(final User user, final LocalDatabaseCallback<Long> callback) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                callback.onComplete(userDao.insert(user));
            }
        });
    }

    private void executeUpdateUser(final User user) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                userDao.update(user);
            }
        });
    }

    private void executeDeleteAllUser() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                userDao.deleteAll();
            }
        });
    }

}
