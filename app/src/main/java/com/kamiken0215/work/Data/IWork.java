package com.kamiken0215.work.Data;

import java.util.List;

public interface IWork {
    String getDate();
    String getTime();
    void setWorkingTime(String date, String time, Boolean isInMode);
    List<String> fetchAttendanceData(int userId, String date);
    List<String> searchAttendanceData(int userId);
}
