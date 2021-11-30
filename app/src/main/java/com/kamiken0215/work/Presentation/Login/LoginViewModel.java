package com.kamiken0215.work.Presentation.Login;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.kamiken0215.work.Domain.Entities.User;
import com.kamiken0215.work.Domain.Repository.IAttendanceRepository;
import com.kamiken0215.work.Domain.Repository.IUserRepository;
import com.kamiken0215.work.Domain.Repository.LocalDatabaseCallback;
import com.kamiken0215.work.Domain.Repository.RepositoryCallback;

import retrofit2.Response;

public class LoginViewModel extends ViewModel {

    private ILoginView loginView;
    private IUserRepository userRepository;
    private IAttendanceRepository attendanceRepository;
    private MutableLiveData<User> userLiveData = new MutableLiveData<>();
    private User user;

    public LoginViewModel(ILoginView loginView, IUserRepository userRepository, IAttendanceRepository attendanceRepository) {
        this.loginView = loginView;
        this.userRepository = userRepository;
        this.attendanceRepository = attendanceRepository;
        //init();
    }

//    //cleardb
//    private void init() {
//        clearUserDatabase();
//    }

    public void login(String email, String password) {

        userRepository.fetchUserBy(email,password,new RepositoryCallback<User>(){

            @Override
            public void onComplete(Response<User> response, Throwable t) {

                if (response != null) {
                    //200 ~ 300
                    if (response.isSuccessful()) {
                        user = response.body();
                        loginView.onLoginSuccess(user,"Success");
                    } else if (response.code() > 400 && response.code() < 500) {
                        loginView.onLoginError(response.code() + "Not Found...");
                    } else {
                        loginView.onLoginError(response.code() + "Server Error...");
                    }
                } else {
                    loginView.onLoginError("Check Your Internet Connection");
                }
            }
        });
    }

    public void save(final User user) {
        userRepository.executeInsertLocalUser(user, new LocalDatabaseCallback<Long>() {
            @Override
            public void onComplete(Long results) {
                if (results > 0) {
                    //fetchUser(user.getUserId());
                    loginView.onSuccessSaveLocal(user.getUserId());
                } else {
                    loginView.onLoginError("DB ERROR");
                }
            }
        });
    }

    public void fetchUser(String userId) {
        userRepository.executeFindLocalUser(userId, new LocalDatabaseCallback<User>() {
            @Override
            public void onComplete(User results) {
                userLiveData.postValue(results);
                //loginView.onSuccessFetchLocal();
            }
        });
    }

    //初期処理としてDBを空にする
    public void cleanDatabase() {
        userRepository.executeDeleteLocalUsers(new LocalDatabaseCallback<Long>() {
            @Override
            public void onComplete(Long results) {
                if (results > 0) {
                    Log.d("Clean Database","Cleaned User");
                    //clearAttendanceDatabase();
                }
            }
        });
        attendanceRepository.executeDeleteLocalAttendances(new LocalDatabaseCallback<Long>() {
            @Override
            public void onComplete(Long results) {
                if (results > 0) {
                    Log.d("Clean Database","Cleaned Attendance");
                }
            }
        });
    }

    public void clearAttendanceDatabase() {
        attendanceRepository.executeDeleteLocalAttendances(new LocalDatabaseCallback<Long>() {
            @Override
            public void onComplete(Long results) {
                if (results > 0) {
                    Log.d("Clean Database","Cleaned Attendance");
                }
            }
        });
    }

    public LiveData<User> getUser() {
        if (userLiveData == null) {
            userLiveData = new MutableLiveData<>();
        }
        return userLiveData;
    }
}
