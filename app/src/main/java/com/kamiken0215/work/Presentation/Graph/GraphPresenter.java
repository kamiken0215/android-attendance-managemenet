package com.kamiken0215.work.Presentation.Graph;


import com.kamiken0215.work.Domain.Entities.AttendanceDataModel;
import com.kamiken0215.work.Domain.UseCase.Attendance.SearchAttendanceUseCase;
import com.kamiken0215.work.Domain.UseCase.CalendarUseCase;

import java.util.List;

//public class GraphPresenter implements IGraphPresenter, IGraphDataPresenter {
public class GraphPresenter implements IGraphPresenter{

    private IGraphView graphView;

    private String date;
    private int userId;

    GraphPresenter(IGraphView graphView,int userId) {
        this.graphView = graphView;
        this.userId = userId;
    }

    @Override
    public void searchAttendanceData(int userId,String date) {
        graphView.showLoading();
        this.date = date;
        SearchAttendanceUseCase searchAttendanceUseCase = new SearchAttendanceUseCase(this,userId,date);
        searchAttendanceUseCase.searchDate();
    }

    @Override
    public void setUpData(List<AttendanceDataModel> attendanceData) {

        //2 加工したデータをgraphviewに渡す
        graphView.hideLoading();
        //1 GraphUseCaseでデータ加工する
        CalendarUseCase calendarUseCase = new CalendarUseCase(this);
        List<Integer[]> yAxis = calendarUseCase.toWeeklyList(attendanceData,date);
        List<String[]> xAxis = calendarUseCase.createAxisData(date);
        graphView.showGraph(xAxis,yAxis);

    }


    @Override
    public void showOverTimesGraph(long[] data) {

    }

    @Override
    public void showLoading() {
        graphView.showLoading();
    }

    @Override
    public void hideLoading() {
        graphView.hideLoading();
    }

    @Override
    public void showError(String message) {
        graphView.showError(message);
    }
}
