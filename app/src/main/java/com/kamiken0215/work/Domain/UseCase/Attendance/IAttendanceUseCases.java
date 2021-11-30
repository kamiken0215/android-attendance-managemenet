package com.kamiken0215.work.Domain.UseCase.Attendance;

import com.kamiken0215.work.Domain.Entities.AttendanceDataModel;

import java.util.List;

public interface IAttendanceUseCases {
    //CRUD
    void fetchByIdAndDate(int userId, String date);
    void onFetchFinished(List<AttendanceDataModel> attendanceData);
    void onFetchFailure(Throwable t);
    void searchByIdAndDate(int userId, String date);
    void onSearchFinished(List<AttendanceDataModel> attendanceData);
    void onSearchFailure(Throwable t);
    void updateAttendanceDate(int userId,
                              String date,
                              String startTime,
                              String finishTime,
                              int workStatus,
                              int attendanceFlag,
                              int straddlingFlag);
    void onUpdateFinished(List<AttendanceDataModel> attendanceData);
    void onUpdateFailure(Throwable t);
    void deleteAttendanceDate(int userId,String date);
    void onDeleteFinished(List<AttendanceDataModel> attendanceData);
    void onDeleteFailure(Throwable t);
}
