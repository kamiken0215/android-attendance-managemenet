package com.kamiken0215.work.Domain.UseCase.Attendance;

import com.kamiken0215.work.Data.Api.RetrofitSingleton;
import com.kamiken0215.work.Domain.Entities.AttendanceDataModel;

import java.util.List;

public class AttendanceUseCases implements IAttendanceUseCases {
    //controller attendance usecase
    private IFetchAttendanceUseCase fetchAttendanceUseCase;
    private IRegisterAttendanceUseCase registerAttendanceUseCase;
    private ISearchAttendanceUseCase searchAttendanceUseCase;
    //private PutAttendanceUseCase putAttendanceUseCase;

    AttendanceUseCases(IFetchAttendanceUseCase fetchAttendanceUseCase) {
        this.fetchAttendanceUseCase = fetchAttendanceUseCase;
    }
    AttendanceUseCases(IRegisterAttendanceUseCase registerAttendanceUseCase) {
        this.registerAttendanceUseCase = registerAttendanceUseCase;
    }
    AttendanceUseCases(ISearchAttendanceUseCase searchAttendanceUseCase) {
        this.searchAttendanceUseCase = searchAttendanceUseCase;
    }

    @Override
    public void fetchByIdAndDate(int userId, String date){
        RetrofitSingleton retrofitSingleton = RetrofitSingleton.getRetrofitInstance();
        //AttendanceRepository attendanceRepository = new AttendanceRepository(retrofitSingleton,this);
        //attendanceRepository.fetchAttendanceData(userId,date);
    }

    @Override
    public void onFetchFinished(List<AttendanceDataModel> attendanceData) {
        //出退勤とステータスを切り出してお返し
        fetchAttendanceUseCase.onFinished(attendanceData);
    }

    @Override
    public void onFetchFailure(Throwable t) {
        fetchAttendanceUseCase.onFailure(t);
    }

    //All data
    @Override
    public void searchByIdAndDate(int userId, String date) {
        RetrofitSingleton retrofitSingleton = RetrofitSingleton.getRetrofitInstance();
        //AttendanceRepository attendanceRepository = new AttendanceRepository(retrofitSingleton,this);
        //attendanceRepository.searchAttendanceData(userId,date);
    }

    @Override
    public void onSearchFinished(List<AttendanceDataModel> attendanceData) {
        searchAttendanceUseCase.onFinished(attendanceData);
    }

    @Override
    public void onSearchFailure(Throwable t) {
        searchAttendanceUseCase.onFailure(t);
    }

    @Override
    public void updateAttendanceDate(int userId,
                                     String date,
                                     String startTime,
                                     String finishTime,
                                     int workStatus,
                                     int attendanceFlag,
                                     int straddlingFlag) {

        RetrofitSingleton retrofitSingleton = RetrofitSingleton.getRetrofitInstance();
        //AttendanceRepository attendanceRepository = new AttendanceRepository(retrofitSingleton,this);
        //attendanceRepository.putAttendanceData(userId,date,startTime,finishTime,workStatus,attendanceFlag,straddlingFlag);

    }

    @Override
    public void onUpdateFinished(List<AttendanceDataModel> attendanceData) {
        registerAttendanceUseCase.onFinished(attendanceData);
    }

    @Override
    public void onUpdateFailure(Throwable t) {
        registerAttendanceUseCase.onFailure(t);
    }

    @Override
    public void deleteAttendanceDate(int userId, String date) {

    }

    @Override
    public void onDeleteFinished(List<AttendanceDataModel> attendanceData) {

    }

    @Override
    public void onDeleteFailure(Throwable t) {

    }
}
