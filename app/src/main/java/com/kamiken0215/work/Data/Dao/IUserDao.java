package com.kamiken0215.work.Data.Dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.kamiken0215.work.Domain.Entities.User;

import java.util.List;

@Dao
public interface IUserDao {

    @Query("SELECT * FROM User WHERE userId = :id")
    User findById(String id);

    @Query("SELECT * FROM User")
    List<User> findAll();

    @Insert
    Long insert(User user);

    @Update
    void update(User user);

    @Query("DELETE FROM User")
    void deleteAll();

}
