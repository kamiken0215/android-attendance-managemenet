package com.kamiken0215.work.Presentation.EditTime;

public interface IEditTimePresenter {
    //changeボタン押されたら更新処理
    void updateAttendanceData(int userId,
                              String date,
                              String startTime,
                              String finishTime,
                              int workingStatus);
    //プログレスバーを表示する
    void showLoading();
    //プログレスバーを非表示にする
    void hideLoading();
    //更新成功
    void onUpdateSuccess(String message);
    //更新失敗
    void onUpdateFailure(String message);
    //エラー
    void showError(String message);
}
