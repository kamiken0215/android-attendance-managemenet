package com.kamiken0215.work.Domain.Repository;

import com.kamiken0215.work.Domain.Entities.Attendance;

import java.util.List;

public interface IAttendanceRepository {

    //CRUD
    //void getAttendanceData(int userId, String date);
    void fetchAttendanceData(int userId, String date);
    void fetchAttendances(String userId,String date, final RepositoryCallback<List<Attendance>> callback);
    //void saveAttendance(Attendance attendance, final RepositoryCallback<Attendance> callback);
    void saveAttendance(Attendance attendanceModel,String token,final RepositoryCallback<List<Attendance>> callback);
    void updateAttendance(final Attendance attendanceModel,final String token, final RepositoryCallback<Float> callback);

    //LocalDatabase
    void executeInsertAttendances(final List<Attendance> attendances, final LocalDatabaseCallback<List<Long>> callback);
    void executeUpdateAttendances(final List<Attendance> attendance, final LocalDatabaseCallback<Integer> callback);
    void executeFindAttendance(final String userId, final String date, final LocalDatabaseCallback<List<Attendance>> callback);
    void executeDeleteLocalAttendances(final LocalDatabaseCallback<Long> callback);
    void searchAttendanceData(int userId, String date);
    void putAttendanceData(int userId,
                           String date,
                           String startTime,
                           String finishTime,
                           int workStatus,
                           int attendanceFlag,
                           int straddlingFlag);

}
