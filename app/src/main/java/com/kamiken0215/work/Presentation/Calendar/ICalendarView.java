package com.kamiken0215.work.Presentation.Calendar;

public interface ICalendarView {
//    void onFetchAttendanceDataSuccess(List<String> attendanceData);
//    void onFetchAttendanceDataError(List<String> attendanceData);
    //出退勤ステータスの表示
    void resetAttendance(String date,String startTime,String finishTime,String workType, int workStatus);
    //ローディング表示
    void showLoading();
    //ローディング非表示
    void hideLoading();
    //通信エラー
    void showErrorMsg(String message);
}
