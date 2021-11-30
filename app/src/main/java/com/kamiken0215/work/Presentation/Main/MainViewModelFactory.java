package com.kamiken0215.work.Presentation.Main;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.kamiken0215.work.Data.Api.RetrofitSingleton;
import com.kamiken0215.work.Data.Repository.AttendanceRepository;
import com.kamiken0215.work.Domain.Repository.IAttendanceRepository;

public class MainViewModelFactory implements ViewModelProvider.Factory{

    private IAttendanceRepository attendanceRepository;

//    public MainViewModelFactory(IAttendanceRepository attendanceRepository) {
//        this.attendanceRepository = attendanceRepository;
//    }

    public MainViewModelFactory(Application application) {
        RetrofitSingleton retrofitSingleton = RetrofitSingleton.getRetrofitInstance();
        this.attendanceRepository = new AttendanceRepository(application,retrofitSingleton);
    }

    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new MainViewModel(attendanceRepository);
    }
}
