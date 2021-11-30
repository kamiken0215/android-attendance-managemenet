package com.kamiken0215.work.Domain.UseCase.Attendance;

import com.kamiken0215.work.Domain.Entities.AttendanceDataModel;
import com.kamiken0215.work.Presentation.Calendar.ICalendarPresenter;
import com.kamiken0215.work.Presentation.Home.IHomePresenter;

import java.util.List;

public class FetchAttendanceUseCase implements IFetchAttendanceUseCase {

    private IHomePresenter homePresenter;
    private ICalendarPresenter calendarPresenter;
    private String date;

    public FetchAttendanceUseCase(IHomePresenter homePresenter) {
        this.homePresenter = homePresenter;
    }

    public FetchAttendanceUseCase(ICalendarPresenter calendarPresenter){
        this.calendarPresenter = calendarPresenter;
    }


    @Override
    public void fetchDate(int userId, String date){
        AttendanceUseCases attendanceUseCases = new AttendanceUseCases(this);
        if(homePresenter != null) {
            homePresenter.showLoading();
        }else if(calendarPresenter != null){
            calendarPresenter.showLoading();
        }
        attendanceUseCases.fetchByIdAndDate(userId, date);
    }


    @Override
    public void onFinished(List<AttendanceDataModel> attendanceData) {
        //出退勤とステータスを切り出してお返し
        //きてるinstanceで処理わけ
        //workStatusもここで処理
        if (homePresenter != null) {
            homePresenter.hideLoading();
            System.out.println(attendanceData);
            if (attendanceData.size() > 0) {
                String startTime = attendanceData.get(0).getStartTime();
                String finishTime = attendanceData.get(0).getFinishTime();
                int workStatus = attendanceData.get(0).getWorkStatus();
                String workType = toWorkType(workStatus);
                if (finishTime == null) {
                    //presence start time
                    //disable start button
                    //show hide view
                    //set finish time
                    //has only start time
                    homePresenter.resetStartTime(startTime.substring(11, 16));
                    homePresenter.disableStartButton();
                    homePresenter.showOverlayStartButton();
                    homePresenter.fixWorkStatus(workStatus);
                    homePresenter.disableSpinner(workType);
                    homePresenter.resetFinishTime("--:--");
                    homePresenter.enableFinishButton();
                    homePresenter.hideOverlayFinishButton();
                    //cant tap spinner
                    //set value of spinner

                } else {
                    homePresenter.resetStartTime(startTime.substring(11, 16));
                    homePresenter.disableStartButton();
                    homePresenter.showOverlayStartButton();
                    homePresenter.resetFinishTime(finishTime.substring(11, 16));
                    homePresenter.disableFinishButton("GOOD JOB!");
                    homePresenter.showOverlayFinishButton();
                    homePresenter.fixWorkStatus(workStatus);
                    homePresenter.disableSpinner(workType);
                }
            } else {
                //none time today
                homePresenter.resetStartTime("--:--");
                homePresenter.enableStartButton();
                homePresenter.hideOverlayStartButton();
                homePresenter.resetFinishTime("--:--");
                homePresenter.disableFinishButton("Hi!");
                homePresenter.showOverlayFinishButton();
                homePresenter.enableSpinner();
            }
        }else if (calendarPresenter != null){
            calendarPresenter.hideLoading();
            if (attendanceData.size() > 0) {
                String date = attendanceData.get(0).getDate().substring(5,10).replace('-','/');
                String startTime = attendanceData.get(0).getStartTime();
                String finishTime = attendanceData.get(0).getFinishTime();
                int workStatus = attendanceData.get(0).getWorkStatus();
                String workType = toWorkType(workStatus);
                if (finishTime == null) {
                    startTime = startTime.substring(11,16);
                    finishTime = "--:--";
                    calendarPresenter.resetAttendanceDate(date,startTime,finishTime,workType,workStatus);
                } else {
                    startTime = startTime.substring(11,16);
                    finishTime = finishTime.substring(11,16);
                    calendarPresenter.resetAttendanceDate(date,startTime,finishTime,workType,workStatus);
                }
            } else {
                //none time today
                calendarPresenter.resetAttendanceDate(date,"--:--","--:--","--",1);
            }
        }
    }

    @Override
    public void onFailure(Throwable t) {
        String err = "SERVER ERROR...";
        if (homePresenter != null){
            homePresenter.showErrorMsg(err);
        }else if(calendarPresenter != null){
            calendarPresenter.showErrorMsg(err);
        }
    }

    private String toWorkType(int workStatus){
        String[] workStatusArray = {"出勤","休出","有給","欠勤","シフト出","シフト休","フレックス"};
        return workStatusArray[workStatus - 1];
    }


}
