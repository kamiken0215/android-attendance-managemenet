package com.kamiken0215.work.Domain.UseCase;

import com.kamiken0215.work.Data.Repository.AttendanceDataRepository;
import com.kamiken0215.work.Domain.Entities.AttendanceDataModel;
import com.kamiken0215.work.Presentation.Home.IHomePresenter;

import java.util.List;

public class HomeUseCase implements IHomeUseCase {

    private IHomePresenter homePresenter;


    public HomeUseCase(IHomePresenter homePresenter) {
        this.homePresenter = homePresenter;
    }

    @Override
    public void initProcess(int userId, String date) {
        homePresenter.showLoading();

        AttendanceDataRepository attendanceDataRepository = new AttendanceDataRepository(this);
        attendanceDataRepository.fetchAttendanceData(userId,date);
    }

    @Override
    public void registerAttendanceTime(int userId,
                                       String date,
                                       String startTime,
                                       String finishTime,
                                       int workStatus,
                                       int attendanceFlag,
                                       int straddlingFlag){
        homePresenter.showLoading();

    }

    @Override
    public void onFinished(List<AttendanceDataModel> attendanceData) {
        //出退勤とステータスを切り出してお返し
        homePresenter.hideLoading();
        System.out.println(attendanceData);
        if(attendanceData.size() > 0){
            String startTime = attendanceData.get(0).getStartTime();
            String finishTime = attendanceData.get(0).getFinishTime();
            if(finishTime == null) {
                //presence start time
                //disable start button
                //show hide view
                //set finish time
                //has only start time
                homePresenter.resetStartTime(startTime.substring(11,16));
                homePresenter.disableStartButton();
                homePresenter.showOverlayStartButton();
                homePresenter.resetFinishTime("--:--");
                homePresenter.enableFinishButton();
                homePresenter.hideOverlayFinishButton();
            }else{
                homePresenter.resetStartTime(startTime.substring(11,16));
                homePresenter.disableStartButton();
                homePresenter.showOverlayStartButton();
                homePresenter.resetFinishTime(finishTime.substring(11,16));
                homePresenter.showOverlayFinishButton();
            }
        }else {
            //none time today
            homePresenter.resetStartTime("--:--");
            homePresenter.enableStartButton();
            homePresenter.hideOverlayStartButton();
            homePresenter.resetFinishTime("--:--");
            homePresenter.showOverlayFinishButton();
        }

        //1 only start
        //2 both of start and finish
        //3 none start and finish
        //1
    }

    @Override
    public void onFailure(Throwable t) {
        homePresenter.hideLoading();
        homePresenter.showErrorMsg("SERVER ERROR!");
    }
}
