package com.kamiken0215.work.Data;

public class LoginApiModel {

    //result of login
    //@Expose
    private int loginUserId;


    public int getId() {
        return loginUserId;
    }

    @Override
    public String toString() {
        return "LoginApiModel{" +
                "loginUserId='" + loginUserId + '\'' +
                '}';
    }
}
