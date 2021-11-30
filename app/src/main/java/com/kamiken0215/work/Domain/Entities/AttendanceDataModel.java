package com.kamiken0215.work.Domain.Entities;

import com.google.gson.annotations.Expose;

public class AttendanceDataModel {

    //public AttendanceDataModel(){}

    //JsonArray allAttendanceData;

    public AttendanceDataModel(int userId, String date, String startTime, String finishTime, int workStatus, int attendanceFlag, int straddlingFlag){
        this.userId = userId;
        this.date = date;
        this.startTime = startTime;
        this.finishTime = finishTime;
        this.workStatus = workStatus;
        this.attendanceFlag = attendanceFlag;
        this.straddlingFlag = straddlingFlag;
    }


    @Override
    public String toString() {
        return "{" +
                "userId:" + userId +
                ",date:'" + date + '\'' +
                ",startTime:'" + startTime + '\'' +
                ",finishTime:'" + finishTime + '\'' +
                ",workStatus:" + workStatus +
                ",straddlingFlag:" + straddlingFlag +
                '}';
    }

    @Expose
    private int userId;
    @Expose
    private String date;
    @Expose
    private String startTime;
    @Expose
    private String finishTime;
    @Expose
    private int workStatus;
    @Expose
    private int attendanceFlag;
    @Expose
    private int straddlingFlag;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(String finishTime) {
        this.finishTime = finishTime;
    }

    public int getWorkStatus() {
        return workStatus;
    }

    public void setWorkStatus(int workStatus) {
        this.workStatus = workStatus;
    }

    public int getAttendanceFlag() {
        return attendanceFlag;
    }

    public void setAttendanceFlag(int attendanceFlag) {
        this.attendanceFlag = attendanceFlag;
    }

    public int getStraddlingFlag() {
        return straddlingFlag;
    }

    public void setStraddlingFlag(int straddlingFlag) {
        this.straddlingFlag = straddlingFlag;
    }
}
