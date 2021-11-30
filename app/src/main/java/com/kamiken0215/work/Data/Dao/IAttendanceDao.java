package com.kamiken0215.work.Data.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.kamiken0215.work.Domain.Entities.Attendance;

import java.util.List;

@Dao
public interface IAttendanceDao {

    @Query("SELECT * FROM attendance WHERE userId = :id")
    List<Attendance> findAll(String id);

    @Query("SELECT * FROM Attendance WHERE userId = :id AND attendanceDate = :date")
    LiveData<Attendance> findOneDay(String id, String date);

    @Query("SELECT * FROM Attendance WHERE userId = :id AND attendanceDate LIKE :date ||'%'")
    List<Attendance> findById(String id, String date);

    @Insert
    List<Long> insert(List<Attendance> attendance);

    @Update
    int update(List<Attendance> attendance);

    @Query("DELETE FROM Attendance")
    void deleteAll();
}
