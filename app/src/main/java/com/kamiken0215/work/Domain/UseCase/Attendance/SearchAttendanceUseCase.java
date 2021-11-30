package com.kamiken0215.work.Domain.UseCase.Attendance;

import com.kamiken0215.work.Domain.Entities.AttendanceDataModel;
import com.kamiken0215.work.Presentation.Graph.IGraphPresenter;

import java.util.List;

public class SearchAttendanceUseCase implements ISearchAttendanceUseCase {

    private IGraphPresenter graphPresenter;
    private int userId;
    private String date;


    public SearchAttendanceUseCase(IGraphPresenter graphPresenter,int userId, String date) {
        this.graphPresenter = graphPresenter;
        this.userId = userId;
        this.date = date;
    }

    @Override
    public void searchDate() {
        AttendanceUseCases attendanceUseCases = new AttendanceUseCases(this);
        attendanceUseCases.searchByIdAndDate(userId,date);
    }

    @Override
    public void onFinished(List<AttendanceDataModel> attendanceData) {

        graphPresenter.setUpData(attendanceData);
    }

    @Override
    public void onFailure(Throwable t) {

    }
}
