package com.kamiken0215.work.Presentation.Graph;

import java.util.List;

public interface IGraphView {

//    void onSearchAttendanceDataSuccess(List<String> attendanceData);
//    void onSearchAttendanceDataError(List<String> attendanceData);
    //
    void showGraph(List<String[]> xAxis, List<Integer[]> yAxis);
    //ローディング表示
    void showLoading();
    //ローディング非表示
    void hideLoading();
    //エラー
    void showError(String message);

}
