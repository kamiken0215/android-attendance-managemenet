package com.kamiken0215.work.Data.DateResource;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.kamiken0215.work.Data.Dao.IAttendanceDao;
import com.kamiken0215.work.Domain.Entities.Attendance;

@Database(entities = {Attendance.class}, version = 5, exportSchema = false)
public abstract class AttendanceDatabase extends RoomDatabase {

    public static AttendanceDatabase instance;

    public abstract IAttendanceDao attendanceDao();

    public static synchronized AttendanceDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    AttendanceDatabase.class, "attendance")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
