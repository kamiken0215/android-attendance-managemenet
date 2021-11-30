package com.kamiken0215.work.Util;

import java.util.Map;

public class AttendanceStatusChecker {

    private Map<String,String> statusMap;

    public String statusChecker(String attendanceStatus) {

        String statusName = "";

        int status = Integer.parseInt(attendanceStatus);

        switch (status) {
            case 1:
                statusName = "出勤";
                break;
            case 2:
                statusName = "休出";
                break;
            case 3:
                statusName = "有給";
                break;
            case 4:
                statusName = "欠勤";
                break;
            case 5:
                statusName = "シフト出";
                break;
            case 6:
                statusName = "シフト休";
                break;
            case 7:
                statusName = "フレックス";
                break;
            default:
                statusName = "";
        }


        return statusName;
    }

}
