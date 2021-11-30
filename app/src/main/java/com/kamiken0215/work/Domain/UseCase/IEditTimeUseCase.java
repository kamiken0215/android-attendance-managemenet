package com.kamiken0215.work.Domain.UseCase;

public interface IEditTimeUseCase {

    //出勤時間と退勤時間と出勤ステータスが揃っていなければ登録できない
    int invalidateAttendanceData(String startTime, String finishTime, int workingStatus);
    //出勤データを更新する
    void updateAttendanceData(int userId,String date, String startTime, String finishTime, int workStatus, int attendanceFlag, int straddlingFlag);
    //ローディング表示
    void showLoading();
    //ローディング非表示
    void hideLoading();
    //更新終了
    void onFinish(String message);
    //サーバーエラー
    void onFailure(String message);
}
