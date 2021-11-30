package com.kamiken0215.work.Data.DateResource;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.kamiken0215.work.Data.Dao.IUserDao;
import com.kamiken0215.work.Domain.Entities.User;

@Database(entities = {User.class}, version = 2, exportSchema = false)
public abstract class UserDatabase extends RoomDatabase {

    public static UserDatabase instance;

    public abstract IUserDao userDao();

    public static synchronized UserDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    UserDatabase.class, "user")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
