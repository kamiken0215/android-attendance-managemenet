package com.kamiken0215.work.Presentation.Graph;

import com.kamiken0215.work.Domain.Entities.AttendanceDataModel;

import java.util.List;

public interface IGraphPresenter {

    //データをとってくる
    void searchAttendanceData(int userId, String date);
    //出勤時間のグラフ用データをセット
    void setUpData(List<AttendanceDataModel> attendanceData);
    //残業時間のグラフを表示する
    void showOverTimesGraph(long[] data);
    //プログレスバーを表示する
    void showLoading();
    //プログレスバーを非表示にする
    void hideLoading();
    //エラー
    void showError(String message);
}
