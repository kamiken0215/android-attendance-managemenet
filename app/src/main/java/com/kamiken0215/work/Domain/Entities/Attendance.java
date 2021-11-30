package com.kamiken0215.work.Domain.Entities;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Entity;

import com.kamiken0215.work.Util.AttendanceStatusChecker;
import com.kamiken0215.work.Util.DateTimeValidator;
import com.google.gson.annotations.SerializedName;


@Entity(primaryKeys = {"userId", "attendanceDate"})
public class Attendance {

    @NonNull
    @SerializedName("user_id")
    private String userId;

    @NonNull
    @SerializedName("attendance_date")
    private String attendanceDate;

    @SerializedName("start_time")
    private String startTime;

    @SerializedName("end_time")
    private String endTime;

    @SerializedName("on_duty")
    private String onDuty;

    @SerializedName("attendance_class")
    private String attendanceClass;

    @SerializedName("request_status")
    private String requestStatus;

    @SerializedName("notes")
    private String notes;

    @SerializedName("closed_on")
    private String closedOn;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("created_by")
    private String createdBy;

    @SerializedName("created_with")
    private String createdWith;

    @SerializedName("updated_at")
    private String updatedAt;

    @SerializedName("updated_by")
    private String updatedBy;

    @SerializedName("updated_with")
    private String updatedWith;

    public Attendance(String userId, String attendanceDate, String startTime, String endTime, String onDuty, String attendanceClass, String requestStatus, String notes, String closedOn, String createdAt, String createdBy, String createdWith, String updatedAt, String updatedBy, String updatedWith) {
        this.userId = userId;
        this.attendanceDate = attendanceDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.onDuty = onDuty;
        this.attendanceClass = attendanceClass;
        this.requestStatus = requestStatus;
        this.notes = notes;
        this.closedOn = closedOn;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.createdWith = createdWith;
        this.updatedAt = updatedAt;
        this.updatedBy = updatedBy;
        this.updatedWith = updatedWith;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAttendanceDate() {
        return attendanceDate;
    }

    public void setAttendanceDate(String attendanceDate) {
        this.attendanceDate = attendanceDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getOnDuty() {
        return onDuty;
    }

    public void setOnDuty(String onDuty) {
        this.onDuty = onDuty;
    }

    public String getAttendanceClass() {
        return attendanceClass;
    }

    public void setAttendanceClass(String attendanceClass) {
        this.attendanceClass = attendanceClass;
    }

    public String getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getClosedOn() {
        return closedOn;
    }

    public void setClosedOn(String closedOn) {
        this.closedOn = closedOn;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedWith() {
        return createdWith;
    }

    public void setCreatedWith(String createdWith) {
        this.createdWith = createdWith;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getUpdatedWith() {
        return updatedWith;
    }

    public void setUpdatedWith(String updatedWith) {
        this.updatedWith = updatedWith;
    }

    public String validate() {

        //全てクリアしたら"OK"
        String message = "";

        DateTimeValidator dateTimeValidator = new DateTimeValidator();

        message = dateTimeValidator.validateDate(this.attendanceDate);
        if (!message.equals("OK")) {
            return message;
        }

        message = dateTimeValidator.validateTime(this.startTime);
        if (!message.equals("OK")) {
            Log.d("VALIDATE_ERROR",message);
            return "出勤時間が不正です";
        }

        message = dateTimeValidator.validateTime(this.endTime);
        if (!message.equals("OK")) {
            Log.d("VALIDATE_ERROR",message);
            return "退勤時間が不正です";
        }

        if (!(this.onDuty.equals("on") || this.onDuty.equals("off"))) {
            return "出勤中ステータスが異常値";
        }

        AttendanceStatusChecker attendanceStatusChecker = new AttendanceStatusChecker();

        message = attendanceStatusChecker.statusChecker(this.attendanceClass);
        if (message.isEmpty()) {
            return "存在しない出勤区分です";
        } else {
            message = "OK";
        }

        return message;
    }
}
