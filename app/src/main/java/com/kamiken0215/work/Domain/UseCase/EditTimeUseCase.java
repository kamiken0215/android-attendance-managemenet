package com.kamiken0215.work.Domain.UseCase;

import com.kamiken0215.work.Domain.UseCase.Attendance.RegisterAttendanceUseCase;
import com.kamiken0215.work.Presentation.EditTime.IEditTimePresenter;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class EditTimeUseCase implements IEditTimeUseCase {

    private IEditTimePresenter editTimePresenter;

    public EditTimeUseCase(IEditTimePresenter editTimePresenter) {
        this.editTimePresenter = editTimePresenter;
    }

    @Override
    public int invalidateAttendanceData(String startTime, String finishTime, int workingStatus) {

        if(startTime.equals("--:--")){
            startTime = "";
        }
        if(finishTime.equals("--:--")){
            finishTime = "";
        }
        //1 出勤時間のみ
        //2 退勤時間のみ
        //3 出勤時間>退勤時間
        if(startTime.isEmpty() || finishTime.isEmpty()){
            return 0;
            //return "TIME IS EMPTY!";
        }else if(isEarlier(startTime,finishTime)){
            return 1;
            //return "FINISH IS EARLIER THAN START!";
        } else {
            return -1;
        }

    }

    @Override
    public void updateAttendanceData(int userId,String date, String startTime, String finishTime, int workStatus, int attendanceFlag, int straddlingFlag) {
        //RegisterUseCaseで使用できる形に変換しなおす
        String formattedStartTime = date + " " + startTime;
        String formattedFinishTime = date + " " + finishTime;

        editTimePresenter.showLoading();
        RegisterAttendanceUseCase registerAttendanceUseCase = new RegisterAttendanceUseCase(this);
        registerAttendanceUseCase.registerDate(userId,date,formattedStartTime,formattedFinishTime,workStatus,workStatus,0);
    }

    @Override
    public void showLoading() {
        editTimePresenter.showLoading();
    }

    @Override
    public void hideLoading() {
        editTimePresenter.hideLoading();
    }

    @Override
    public void onFinish(String message) {
        editTimePresenter.hideLoading();
        editTimePresenter.onUpdateSuccess(message);
    }

    @Override
    public void onFailure(String message) {
        editTimePresenter.hideLoading();
        editTimePresenter.onUpdateFailure(message);
    }

    private boolean isEarlier(String startTime, String finishTime){

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime localStartTime = LocalTime.parse(startTime,dtf);
        LocalTime localFinishTime = LocalTime.parse(finishTime,dtf);
        int result = localStartTime.compareTo(localFinishTime);
        if(result < 0){
            return false;
        }else {
            return true;
        }

    }
}
