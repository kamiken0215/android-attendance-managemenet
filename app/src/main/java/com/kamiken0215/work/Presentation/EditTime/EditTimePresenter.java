package com.kamiken0215.work.Presentation.EditTime;

import com.kamiken0215.work.Domain.UseCase.EditTimeUseCase;

public class EditTimePresenter implements IEditTimePresenter {

    private IEditTimeView editTimeView;

    public EditTimePresenter(IEditTimeView editTimeView) {
        this.editTimeView = editTimeView;
    }


    @Override
    public void updateAttendanceData(int userId,
                                     String date,
                                     String startTime,
                                     String finishTime,
                                     int workingStatus) {

        EditTimeUseCase editTimeUseCase = new EditTimeUseCase(this);
        int resultCode = editTimeUseCase.invalidateAttendanceData(startTime,finishTime,workingStatus);

        //0:start time or finish time is null
        //1:finish time earlier than start time
        //2:although working status is holiday,start time or finish time is not null

        if(resultCode == 0){
            editTimeView.onEditError("TIME IS EMPTY!");
        }else if(resultCode == 1){
            editTimeView.onEditError("FINISH IS EARLIER THAN START!");
        }else {
            editTimeUseCase.updateAttendanceData(userId,date,startTime,finishTime,workingStatus,0,0);
        }

    }


    @Override
    public void showLoading() {
        editTimeView.showLoading();
    }

    @Override
    public void hideLoading() {
        editTimeView.hideLoading();
    }

    @Override
    public void onUpdateSuccess(String message) {
        editTimeView.onEditSuccess(message);
    }

    @Override
    public void onUpdateFailure(String message) {
        editTimeView.onEditError(message);
    }

    @Override
    public void showError(String message) {
    }
//
//    @Override
//    public void onEditWorkItem(String date, String time, Boolean isInMode){
//
//        Work work = new Work();
//
//        work.setWorkingTime(date,time,isInMode);
//
//
//    }

}
