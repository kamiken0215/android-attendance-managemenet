package com.kamiken0215.work.Domain.UseCase.Attendance;

import com.kamiken0215.work.Domain.Entities.AttendanceDataModel;

import java.util.List;

public interface IFetchAttendanceUseCase {

    void fetchDate(int userId, String date);
    void onFinished(List<AttendanceDataModel> attendanceData);
    void onFailure(Throwable t);

}
