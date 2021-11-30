package com.kamiken0215.work.Data;

import android.text.TextUtils;

public class Users implements IUsers {

    private int userId;
    private String userName;
    private String password;
    private int status;

    @Override
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public int isValidData() {
        //0. Check username is empty
        //1. Check password is empty
        //2. Check username length > 10
        //3. password length > 8

        if (TextUtils.isEmpty(getUserName()))
            return 0;
        else if(TextUtils.isEmpty(getPassword()))
            return 1;
        else if(getUserName().length() < 4)
            return 2;
        else if(getPassword().length() < 4)
            return 3;
        else
            return -1;
    }


}