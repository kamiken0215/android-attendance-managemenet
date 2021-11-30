package com.kamiken0215.work.Presentation.Login;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.kamiken0215.work.Domain.Repository.IAttendanceRepository;
import com.kamiken0215.work.Domain.Repository.IUserRepository;

public class LoginViewModelFactory implements ViewModelProvider.Factory {

    private ILoginView loginView;
    private IUserRepository userRepository;
    private IAttendanceRepository attendanceRepository;

    public LoginViewModelFactory(ILoginView loginView, IUserRepository userRepository, IAttendanceRepository attendanceRepository) {
        this.loginView = loginView;
        this.userRepository = userRepository;
        this.attendanceRepository = attendanceRepository;
    }

    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new LoginViewModel(loginView,userRepository,attendanceRepository);
    }
}
