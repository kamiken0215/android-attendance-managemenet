package com.kamiken0215.work.Presentation.EditTime;

public interface IEditTimeView {
    void onEditSuccess(String message);
    void onEditError(String message);
    void showLoading();
    void hideLoading();
    void resetStartTime(String startTime);
    void resetFinishTime(String finishTime);
    void resetWorkingStatus(String workingStatus);
    void enableChangeButton();
    void disableChangeButton();
}
