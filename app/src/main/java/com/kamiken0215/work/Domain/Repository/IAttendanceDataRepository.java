package com.kamiken0215.work.Domain.Repository;

public interface IAttendanceDataRepository {

    //CRUD
    //void getAttendanceData(int userId, String date);
    void fetchAttendanceData(int userId, String date);
    void searchAttendanceData(int userId, String date);
    void putAttendanceData(int userId,
                           String date,
                           String startTime,
                           String finishTime,
                           int workStatus,
                           int attendanceFlag,
                           int straddlingFlag);

}
