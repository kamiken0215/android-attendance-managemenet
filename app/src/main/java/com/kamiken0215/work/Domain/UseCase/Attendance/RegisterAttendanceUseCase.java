package com.kamiken0215.work.Domain.UseCase.Attendance;

import com.kamiken0215.work.Domain.Entities.AttendanceDataModel;
import com.kamiken0215.work.Domain.UseCase.IEditTimeUseCase;
import com.kamiken0215.work.Presentation.Home.IHomePresenter;

import java.util.List;

public class RegisterAttendanceUseCase implements IRegisterAttendanceUseCase {

    private IHomePresenter homePresenter;
    private IEditTimeUseCase editTimeUseCase;

    public RegisterAttendanceUseCase(IHomePresenter homePresenter) {
        this.homePresenter = homePresenter;
    }
    public RegisterAttendanceUseCase(IEditTimeUseCase editTimeUseCase) {
        this.editTimeUseCase = editTimeUseCase;
    }

    @Override
    public void registerDate(int userId,
                             String date,
                             String startTime,
                             String finishTime,
                             int workStatus,
                             int attendanceFlag,
                             int straddlingFlag) {
        if(homePresenter != null) {
            homePresenter.showLoading();

        }else if(editTimeUseCase != null){
            editTimeUseCase.showLoading();
        }
        AttendanceUseCases attendanceUseCases = new AttendanceUseCases(this);
        attendanceUseCases.updateAttendanceDate(userId, date, startTime, finishTime, workStatus, attendanceFlag, straddlingFlag);
    }

    @Override
    public void onFinished(List<AttendanceDataModel> attendanceData) {
        //出退勤とステータスを切り出してお返し
        if(homePresenter != null) {
            homePresenter.hideLoading();
            System.out.println(attendanceData);
            if (attendanceData.size() > 0) {
                String startTime = attendanceData.get(0).getStartTime();
                String finishTime = attendanceData.get(0).getFinishTime();
                if (finishTime == null) {
                    //presence start time
                    //disable start button
                    //show hide view
                    //set finish time
                    //has only start time
                    homePresenter.resetStartTime(startTime.substring(11, 16));
                    homePresenter.disableStartButton();
                    homePresenter.showOverlayStartButton();
                    homePresenter.resetFinishTime("--:--");
                    homePresenter.enableFinishButton();
                    homePresenter.hideOverlayFinishButton();
                } else {
                    homePresenter.resetStartTime(startTime.substring(11, 16));
                    homePresenter.disableStartButton();
                    homePresenter.showOverlayStartButton();
                    homePresenter.resetFinishTime(finishTime.substring(11, 16));
                    homePresenter.disableFinishButton("GOOD JOB!");
                    homePresenter.showOverlayFinishButton();
                }
            } else {
                //none time today
                homePresenter.resetStartTime("--:--");
                homePresenter.enableStartButton();
                homePresenter.hideOverlayStartButton();
                homePresenter.resetFinishTime("--:--");
                homePresenter.disableFinishButton("Hi!");
                homePresenter.showOverlayFinishButton();
            }
        }else if(editTimeUseCase != null){
            editTimeUseCase.onFinish("UPDATE SUCCESS");
        }
    }

    @Override
    public void onFailure(Throwable t) {
        if(homePresenter != null) {
            homePresenter.showErrorMsg("SERVER ERROR...");
        }else if(editTimeUseCase != null){
            editTimeUseCase.onFailure("SERVER ERROR...");
        }
    }
}
