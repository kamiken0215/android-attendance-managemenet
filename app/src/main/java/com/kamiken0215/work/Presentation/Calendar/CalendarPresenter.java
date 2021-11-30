package com.kamiken0215.work.Presentation.Calendar;

import com.kamiken0215.work.Data.Work;
import com.kamiken0215.work.Domain.UseCase.Attendance.FetchAttendanceUseCase;

import java.util.ArrayList;
import java.util.List;

public class CalendarPresenter implements ICalendarPresenter {

    private ICalendarView calendarView;


    private int userId;
    private String date;

    private List<String> resultApiList = new ArrayList<>();

    CalendarPresenter(ICalendarView calendarView,int userId) {
        this.calendarView = calendarView;
        this.userId = userId;
    }

    @Override
    public void initFetchAttendanceData(int userId){
        FetchAttendanceUseCase fetchAttendanceUseCase = new FetchAttendanceUseCase(this);
        //resultApiList = w.fetchAttendanceData(userId,date);
        Work w = new Work();
        String date = w.getDate();
        fetchAttendanceUseCase.fetchDate(userId,date);
    }

    @Override
    public void fetchAttendanceData(int userId, String date){
        FetchAttendanceUseCase fetchAttendanceUseCase = new FetchAttendanceUseCase(this);
        //resultApiList = w.fetchAttendanceData(userId,date);
        fetchAttendanceUseCase.fetchDate(userId,date);
    }

    @Override
    public void showLoading() {
        calendarView.showLoading();
    }

    @Override
    public void hideLoading() {
        calendarView.hideLoading();
    }

    @Override
    public void showErrorMsg(String message) {
        calendarView.showErrorMsg(message);
    }

    @Override
    public void resetAttendanceDate(String date, String startTime, String finishTime, String workType, int workStatus) {
        calendarView.resetAttendance(date,startTime,finishTime,workType,workStatus);
    }
}
