package com.kamiken0215.work.Presentation.Home;


public interface IHomePresenter {
    //スタート時間の更新
    void resetStartTime(String startTime);
    //終了時間の更新
    void resetFinishTime(String finishTime);
    //スタートボタン押せる
    void enableStartButton();
    //スタートボタン押せなくする
    void disableStartButton();
    //スタートボタンのオーバーレイを表示する
    void showOverlayStartButton();
    //スタートボタンのオーバーレイを非表示にする
    void hideOverlayStartButton();
    //フィニッシュボタン押せる
    void enableFinishButton();
    //フィニッシュボタン押せなくする
    void disableFinishButton(String message);
    //フィニッシュボタンのオーバーレイを表示する
    void showOverlayFinishButton();
    //フィニッシュボタンのオーバーレイを非表示にする
    void hideOverlayFinishButton();
    //ローディング表示
    void showLoading();
    //ローディング非表示
    void hideLoading();
    //UseCaseに初期処理を移譲
    void initFetchAttendanceData();
    //UseCaseに出勤処理を移譲
    void onStartWorking(int workStatus);
    //UseCaseに退勤処理を移譲
    void onFinishWorking(int workStatus);
    //スピナーに表示する配列とindex番号渡す
    void fixWorkStatus(int workStatus);
    //スピナータップ可能
    void disableSpinner(String workType);
    //スピナータップ不可
    void enableSpinner();
    //サーバーエラー
    void showErrorMsg(String message);
}
