package com.kamiken0215.work.Domain.UseCase;

import com.kamiken0215.work.Domain.Entities.AttendanceDataModel;

import java.util.List;

public interface ICalendarUseCase {
    //business logic
    void formatDate();
    //show time
    void formatTime();
    //show status
    void formatStatus();
    List<Integer[]> toWeeklyList(List<AttendanceDataModel> attendanceData,String date);
    List<String[]> createAxisData(String date);
}
