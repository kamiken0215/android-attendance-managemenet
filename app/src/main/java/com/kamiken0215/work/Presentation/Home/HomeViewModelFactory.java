package com.kamiken0215.work.Presentation.Home;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.kamiken0215.work.Domain.Repository.IAttendanceRepository;

public class HomeViewModelFactory implements ViewModelProvider.Factory{
    private IHomeView homeView;
    private IAttendanceRepository attendanceRepository;

    public HomeViewModelFactory(IHomeView homeView, IAttendanceRepository attendanceRepository) {
        this.homeView = homeView;
        this.attendanceRepository = attendanceRepository;
    }

    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new HomeViewModel(attendanceRepository);
    }
}
