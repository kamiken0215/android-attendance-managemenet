package com.kamiken0215.work.Presentation.Main;

import com.kamiken0215.work.Data.Work;

import java.util.ArrayList;
import java.util.List;

public class MainPresenter implements IMainPresenter {

    private IMainView mainView;
    private int userId;
    private String date;
    private List<String> resultApiList = new ArrayList<>();

    //private RequestPutAttendanceDataModel attendanceData;

    //1 check have a data yesterday
    //2 when 1 is Yes, check the data have both of start and finish time
    //3 when 1 is No, force to update yesterday finish time to 23:59:59
    //4 force to record start time to 00:00:00 and set straddling_flag in 1 today
    //5 put start_time in textview


    public MainPresenter(IMainView mainView,int userId) {
        this.userId = userId;
        this.mainView = mainView;
    }

    @Override
    public void init(){

        Work w = new Work();
        date = w.getDate();
        resultApiList = w.fetchAttendanceData(userId,date);
        //date = "2020-01-02";

    }

//    public void onFinishFetch(){
//        if(resultApiList.size() > 0){
//            mainView.onFetchAttendanceDataSuccess(resultApiList);
//        }else {
//            mainView.onFetchAttendanceDataError(resultApiList);
//        }
//    }


}
