package com.kamiken0215.work.Domain.Entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity
public class User {

    @SerializedName("id")
    @NonNull
    @PrimaryKey
    private String userId;

    @SerializedName("name")
    private String userName;

    @SerializedName("email")
    private String email;

    @SerializedName("password")
    private String password;

    @SerializedName("status")
    private String status;

    @SerializedName("paid_holidays")
    private String paidHolidays;

    @SerializedName("rest_paid_holidays")
    private String restPaidHolidays;

    @SerializedName("token")
    @ColumnInfo(name = "token")
    private String token;

    @Ignore
    public User(@NonNull String userId, String userName, String email, String password, String status, String paidHolidays, String restPaidHolidays, String token) {
        this.userId = userId;
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.status = status;
        this.paidHolidays = paidHolidays;
        this.restPaidHolidays = restPaidHolidays;
        this.token = token;
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPaidHolidays() {
        return paidHolidays;
    }

    public void setPaidHolidays(String paidHolidays) {
        this.paidHolidays = paidHolidays;
    }

    public String getRestPaidHolidays() {
        return restPaidHolidays;
    }

    public void setRestPaidHolidays(String restPaidHolidays) {
        this.restPaidHolidays = restPaidHolidays;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", status='" + status + '\'' +
                ", paidHolidays='" + paidHolidays + '\'' +
                ", restPaidHolidays='" + restPaidHolidays + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}
