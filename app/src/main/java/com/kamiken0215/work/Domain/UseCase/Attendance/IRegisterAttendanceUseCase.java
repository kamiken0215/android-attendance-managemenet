package com.kamiken0215.work.Domain.UseCase.Attendance;

import com.kamiken0215.work.Domain.Entities.AttendanceDataModel;

import java.util.List;

public interface IRegisterAttendanceUseCase {
    void registerDate(int userId,
                      String date,
                      String startTime,
                      String finishTime,
                      int workStatus,
                      int attendanceFlag,
                      int straddlingFlag);
    void onFinished(List<AttendanceDataModel> attendanceData);
    void onFailure(Throwable t);
}
