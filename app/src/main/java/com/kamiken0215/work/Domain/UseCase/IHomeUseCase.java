package com.kamiken0215.work.Domain.UseCase;

import com.kamiken0215.work.Domain.Entities.AttendanceDataModel;

import java.util.List;

public interface IHomeUseCase {
    //出退勤時間とステータスの取得
    void initProcess(int userId, String password);
    void registerAttendanceTime(int userId,
                                String date,
                                String startTime,
                                String finishTime,
                                int workStatus,
                                int attendanceFlag,
                                int straddlingFlag);
    void onFinished(List<AttendanceDataModel> attendanceData);
    void onFailure(Throwable t);
}
