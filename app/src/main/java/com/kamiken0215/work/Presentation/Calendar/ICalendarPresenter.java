package com.kamiken0215.work.Presentation.Calendar;

public interface ICalendarPresenter {

    //当日の出勤データを取得
    void initFetchAttendanceData(int userId);

    //1日分の出勤データを取得
    void fetchAttendanceData(int userId, String date);

    //ローディング表示
    void showLoading();
    //ローディング非表示
    void hideLoading();
    //サーバーエラー
    void showErrorMsg(String message);
    //出勤データ表示の更新
    void resetAttendanceDate(String date,String startTime,String finishTime,String workType, int workStatus);
}
