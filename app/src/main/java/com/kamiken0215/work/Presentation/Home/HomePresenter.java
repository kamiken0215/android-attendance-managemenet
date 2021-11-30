package com.kamiken0215.work.Presentation.Home;

import com.kamiken0215.work.Data.Work;
import com.kamiken0215.work.Domain.UseCase.Attendance.FetchAttendanceUseCase;
import com.kamiken0215.work.Domain.UseCase.Attendance.RegisterAttendanceUseCase;

public class HomePresenter implements IHomePresenter {

    private IHomeView homeView;
    private int userId;

    HomePresenter(IHomeView menuView, int userId) {
        this.homeView = menuView;
        this.userId = userId;
    }

    @Override
    public void initFetchAttendanceData(){
        //HomeUseCase initUseCase = new HomeUseCase(this);
        FetchAttendanceUseCase fetchAttendanceUseCase = new FetchAttendanceUseCase(this);

        Work w = new Work();
        String date = w.getDate();
        System.out.println(userId);
        //JsonArrayでうけとる
        //resultApiList = w.fetchAttendanceData(userId,date);
        //initUseCase.initProcess(userId,date);
        fetchAttendanceUseCase.fetchDate(userId,date);
    }


    @Override
    public void resetStartTime(String startTime){
        homeView.resetStartTime(startTime);
    }

    @Override
    public void resetFinishTime(String finishTime){
        homeView.resetFinishTime(finishTime);
    }

    @Override
    public void enableStartButton() {
        homeView.enableStartButton();
    }

    @Override
    public void disableStartButton() {
        homeView.disableStartButton();
    }

    @Override
    public void showOverlayStartButton() {
        homeView.showOverlayStartButton();
    }

    @Override
    public void hideOverlayStartButton() {
        homeView.hideOverlayStartButton();
    }

    @Override
    public void enableFinishButton() {
        homeView.enableFinishButton();
    }

    @Override
    public void disableFinishButton(String message) {
        homeView.disableFinishButton(message);
    }


    @Override
    public void showOverlayFinishButton() {
        homeView.showOverlayFinishButton();
    }

    @Override
    public void hideOverlayFinishButton() {
        homeView.hideOverlayFinishButton();
    }

    @Override
    public void showLoading() {
        homeView.showLoading();
    }

    @Override
    public void hideLoading() {
        homeView.hideLoading();
    }

    @Override
    public void fixWorkStatus(int workStatus) {
        homeView.fixWorkStatus(workStatus);
    }

    @Override
    public void disableSpinner(String workType) {
        homeView.disableSpinner(workType);
    }

    @Override
    public void enableSpinner() {
        homeView.enableSpinner();
    }

    @Override
    public void onStartWorking(int workStatus) {
        RegisterAttendanceUseCase registerAttendanceUseCase = new RegisterAttendanceUseCase(this);
        Work w = new Work();
        String date = w.getDate();
        registerAttendanceUseCase.registerDate(userId,date,null,null,workStatus,1,1);
        //initUseCase.registerAttendanceTime(userId,date,startTime,finishTime,workStatus,);
    }

    @Override
    public void onFinishWorking(int workStatus) {
        RegisterAttendanceUseCase registerAttendanceUseCase = new RegisterAttendanceUseCase(this);
        Work w = new Work();
        String date = w.getDate();
        registerAttendanceUseCase.registerDate(userId,date,null,null,workStatus,1,1);
    }

    @Override
    public void showErrorMsg(String message){
        homeView.showErrorMsg(message);
    }
}
