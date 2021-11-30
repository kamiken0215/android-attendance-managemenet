package com.kamiken0215.work.Util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateTimeValidator {

    public String validateDate(String date) {

        if (date == null || "".equals(date)) {
            return "日付がありません";
        }

        try {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd");
            String s = dtf.format(LocalDate.parse(date, dtf));
            System.out.println("日付書式OK");
        } catch (DateTimeParseException dtp) {
            dtp.printStackTrace();
            return "日付書式がyyyyMMDDではありません";
        }

        return "OK";
    }

    public String validateTime(String date) {

        if (date == null || "".equals(date)) {
            return "日時がありません";
        }

        if (date.length() != 19) {
            return "日付書式の長さが一致しません";
        }

        //1:yyyy-MM-DD[半角スペース]かチェック
        //2:HH:mm:ssかチェック

        String day = date.substring(0,10);

        try {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String s = dtf.format(LocalDate.parse(day, dtf));
            System.out.println("日付書式OK");
        } catch (DateTimeParseException dtp) {
            dtp.printStackTrace();
            return "日付書式がyyyy-MM-DDではありません";
        }

        String time = date.substring(11,19);

        Pattern p = Pattern.compile("^([0-1][0-9]|[2][0-3]):[0-5][0-9]:[0-5][0-9]$");
        Matcher m = p.matcher(time);
        if (!m.find()) {
            return "時間がHH:MM:SSではありません";
        }

        return "OK";
    }
}
