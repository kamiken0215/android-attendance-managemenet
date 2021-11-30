package com.kamiken0215.work.Domain.UseCase;

import com.kamiken0215.work.Domain.Entities.AttendanceDataModel;

import java.util.List;

public interface IGraphWeeklyUseCase {

    void searchAttendanceData(int userId, String date);
    void onFinished(List<AttendanceDataModel> attendanceData);
    void onFailure(Throwable t);

}
