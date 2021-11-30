package com.kamiken0215.work.Presentation.Graph;

import com.kamiken0215.work.Domain.Entities.AttendanceDataModel;

import java.util.List;

public interface IDataView {

    //出勤データを表示
    void setUpData(List<AttendanceDataModel> attendanceData);
    void showHoursDataView(List<Integer[]> yAxis, List<String[]> xAxis);
    void showOverTimesDataView(long [] attendanceTimes);
    //void showWeeklyGraph(long[] attendanceTimes, int numberOfGraph);
}
