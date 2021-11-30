package com.kamiken0215.work.Data.Dao;

import com.kamiken0215.work.Domain.Entities.Attendance;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AttendanceDao {

    private IAttendanceDao attendanceDao;
    private List<Attendance> attendances;

    public AttendanceDao(IAttendanceDao attendanceDao) {
        this.attendanceDao = attendanceDao;
    }

    public synchronized List<Attendance> findAttendancesBy(final String userId, final String date) {
        synchronized (this) {
            executeFindAttendance(userId,date);
        }
        return this.attendances;
    }

    public synchronized void delete() {
        executeDeleteAllAttendances();
    }

    public synchronized void insert(List<Attendance> attendances) {
        executeInsertAttendances(attendances);
    }

    private void executeFindAttendance(final String userId, final String date) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                attendances = attendanceDao.findById(userId,date);
            }
        });
    }

    private void executeInsertAttendances(final List<Attendance> attendances) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                attendanceDao.insert(attendances);
            }
        });
    }

    private void executeUpdateAttendances(final List<Attendance> attendances) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                attendanceDao.update(attendances);
            }
        });
    }

    private void executeDeleteAllAttendances() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                attendanceDao.deleteAll();
            }
        });
    }
}
