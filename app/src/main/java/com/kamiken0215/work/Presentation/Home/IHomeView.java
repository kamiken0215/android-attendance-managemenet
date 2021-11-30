package com.kamiken0215.work.Presentation.Home;

public interface IHomeView {
    //UIにかんすることだけね
    //出勤ボタン有効
    void enableStartButton();
    //出勤ボタン無効
    void disableStartButton();
    //出勤オーバーレイ表示
    void showOverlayStartButton();
    //出勤オーバーレイ非表示
    void hideOverlayStartButton();
    //退勤ボタン有効
    void enableFinishButton();
    //退勤ボタン無効
    void disableFinishButton(String message);
    //退勤オーバーレイ表示
    void showOverlayFinishButton();
    //退勤オーバーレイ非表示
    void hideOverlayFinishButton();
    //ローディング表示
    void showLoading();
    //ローディング非表示
    void hideLoading();
    //出勤時間セット
    void resetStartTime(String time);
    //退勤時間セット
    void resetFinishTime(String time);
    //スピナーにworkStatusをセット
    void fixWorkStatus(int workStatus);
    //スピナー表示
    void disableSpinner(String workingType);
    //スピナー非表示
    void enableSpinner();
    //通信エラー
    void showErrorMsg(String message);
//    void initReadTime(String inTime, String outTime);
//    Context getContext();
//    void onFetchAttendanceDataSuccess(List<String> resultInitProcess);
//    void onFetchAttendanceDataError(List<String> resultInitProcess);
}
