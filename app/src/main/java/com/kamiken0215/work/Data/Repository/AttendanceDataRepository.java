package com.kamiken0215.work.Data.Repository;

import com.kamiken0215.work.Data.Api.ServiceCreator;
import com.kamiken0215.work.Domain.Entities.AttendanceDataModel;
import com.kamiken0215.work.Data.Api.IRequestAttendanceDataApi;
import com.kamiken0215.work.Domain.Repository.IAttendanceDataRepository;
import com.kamiken0215.work.Domain.UseCase.ICalendarUseCase;
import com.kamiken0215.work.Domain.UseCase.IGraphWeeklyUseCase;
import com.kamiken0215.work.Domain.UseCase.IHomeUseCase;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class AttendanceDataRepository implements IAttendanceDataRepository {

    private IGraphWeeklyUseCase graphWeeklyUseCase;
    private IHomeUseCase homeUseCase;
    private ICalendarUseCase calendarUseCase;



    public AttendanceDataRepository(IGraphWeeklyUseCase graphWeeklyUseCase) {
        this.graphWeeklyUseCase = graphWeeklyUseCase;
    }
    public AttendanceDataRepository(IHomeUseCase homeUseCase){
        this.homeUseCase = homeUseCase;
    }

    public AttendanceDataRepository(ICalendarUseCase calendarUseCase){
        this.calendarUseCase = calendarUseCase;
    }


    @Override
    public void fetchAttendanceData(int userId, String date) {
        //Api
        ServiceCreator serviceCreator = new ServiceCreator();
        IRequestAttendanceDataApi service = serviceCreator.createService(IRequestAttendanceDataApi.class);
        Call<List<AttendanceDataModel>> call = service
                .getOneAttendanceData(userId,date);
        call.enqueue(new Callback<List<AttendanceDataModel>>() {

            @Override
            public void onResponse(Call<List<AttendanceDataModel>> call, retrofit2.Response<List<AttendanceDataModel>> response) {
                if (response.isSuccessful()){
                    homeUseCase.onFinished(response.body());
                }
                else {
                    System.out.println(response.code());
                }
            }

            @Override
            public void onFailure(Call<List<AttendanceDataModel>> call, Throwable t) {
                homeUseCase.onFailure(t);
            }
        });
    }

    @Override
    public void searchAttendanceData(int userId, String date){
        //Api
        ServiceCreator serviceCreator = new ServiceCreator();
        IRequestAttendanceDataApi service = serviceCreator.createService(IRequestAttendanceDataApi.class);
        Call<List<AttendanceDataModel>> call = service
                .getOneAttendanceData(userId,date);
        call.enqueue(new Callback<List<AttendanceDataModel>>() {

            @Override
            public void onResponse(Call<List<AttendanceDataModel>> call, retrofit2.Response<List<AttendanceDataModel>> response) {
                if (response.isSuccessful()){
                    homeUseCase.onFinished(response.body());
                }
                else {
                    System.out.println(response.code());
                }
            }

            @Override
            public void onFailure(Call<List<AttendanceDataModel>> call, Throwable t) {
                homeUseCase.onFailure(t);
            }
        });
    }

//    @Override
//    public void fetchAttendanceData(int userId, String date) {
//        //Api
//        ServiceCreator serviceCreator = new ServiceCreator();
//        IRequestAttendanceDataApi service = serviceCreator.createService(IRequestAttendanceDataApi.class);
//        Call<List<AttendanceDataModel>> call = service
//                .getOneAttendanceData(userId,date);
//        call.enqueue(new Callback<List<AttendanceDataModel>>() {
//
//            @Override
//            public void onResponse(Call<List<AttendanceDataModel>> call, retrofit2.Response<List<AttendanceDataModel>> response) {
//                if (response.isSuccessful()){
//                    mainUseCase.onFinished(response.body());
//                }
//                else {
//                    System.out.println(response.code());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<AttendanceDataModel>> call, Throwable t) {
//                mainUseCase.onFailure(t);
//            }
//        });
//    }


    @Override
    public void putAttendanceData(int userId, String date, String startTime, String finishTime, int workStatus, int attendanceFlag, int straddlingFlag) {
        //Api
        ServiceCreator serviceCreator = new ServiceCreator();
        IRequestAttendanceDataApi service = serviceCreator.createService(IRequestAttendanceDataApi.class);
        Call<List<AttendanceDataModel>> call = service
                .putAttendanceData(new AttendanceDataModel(
                        userId
                        ,date
                        ,null
                        ,null
                        ,1
                        ,1
                        ,0
                ));
        call.enqueue(new Callback<List<AttendanceDataModel>>() {

            @Override
            public void onResponse(Call<List<AttendanceDataModel>> call, retrofit2.Response<List<AttendanceDataModel>> response) {
                if (response.isSuccessful()){
                    homeUseCase.onFinished(response.body());
                }
                else {
                    System.out.println(response.code());
                }
            }

            @Override
            public void onFailure(Call<List<AttendanceDataModel>> call, Throwable t) {
                homeUseCase.onFailure(t);
            }
        });
    }


    //    public List<AttendanceDataModel> attendanceData(int userId, String date){
//
//        //Api
//        IRequestAttendanceDataApi service = createService(IRequestAttendanceDataApi.class);
//        Call<List<AttendanceDataModel>> call = service
//                .getOneAttendanceData(userId,date);
//        call.enqueue(new Callback<List<AttendanceDataModel>>() {
//
//            @Override
//            public void onResponse(Call<List<AttendanceDataModel>> call, retrofit2.Response<List<AttendanceDataModel>> response) {
//                if (response.isSuccessful()){
//
//                    attendanceDataList = response.body();
//                }
//                else {
//                    System.out.println(response.code());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<AttendanceDataModel>> call, Throwable t) {
//                System.out.println(t);
//            }
//        });
//
//        return attendanceDataList;
//    }
//
//
//    public List<AttendanceDataModel> attendanceData(int userId){
//
//        //Api
//        IRequestAttendanceDataApi service = createService(IRequestAttendanceDataApi.class);
//        Call<List<AttendanceDataModel>> call = service
//                .getAllAttendanceData(userId);
//        call.enqueue(new Callback<List<AttendanceDataModel>>() {
//
//            @Override
//            public void onResponse(Call<List<AttendanceDataModel>> call, retrofit2.Response<List<AttendanceDataModel>> response) {
//                if (response.isSuccessful()){
//                    attendanceDataList = response.body();
//                }
//                else {
//                    System.out.println(response.code());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<AttendanceDataModel>> call, Throwable t) {
//                System.out.println(t);
//            }
//        });
//
//        return attendanceDataList;
//    }
//
//    //update
//    public List<AttendanceDataModel> attendanceData(int userId, String date , String startTime, String finishTime, int workStatus,int attendanceFlag, int straddlingFlag){
//        //Api
//        IRequestAttendanceDataApi service = createService(IRequestAttendanceDataApi.class);
//        Call<List<AttendanceDataModel>> call = service
//                .putAttendanceData(new AttendanceDataModel(
//                        userId
//                        ,date
//                        ,startTime
//                        ,finishTime
//                        ,workStatus
//                        ,attendanceFlag
//                        ,straddlingFlag
//                ));
//        call.enqueue(new Callback<List<AttendanceDataModel>>() {
//
//            @Override
//            public void onResponse(Call<List<AttendanceDataModel>> call, retrofit2.Response<List<AttendanceDataModel>> response) {
//                if (response.isSuccessful()){
//                    attendanceDataList = response.body();
//                }
//                else {
//                    System.out.println(response.code());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<AttendanceDataModel>> call, Throwable t) {
//                System.out.println(t);
//            }
//        });
//        return attendanceDataList;
//    }

}
