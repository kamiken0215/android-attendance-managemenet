package com.kamiken0215.work.Util;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class DateFormatter {

    private String formattedDate;
    private Calendar calendar;
    private String formattedTime;
    LocalDate date;

    public DateFormatter(){
        date = LocalDate.now();
        this.formattedDate = date.toString();
        calendar = Calendar.getInstance();
    }

    //年月日
    public String getDate(){
        date = LocalDate.now();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return date.format(dtf);
    }

    //スラッシュ抜き年月日
    public String getTodayExceptSlash() {
        date = LocalDate.now();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return date.format(dtf).replace("-","");
    }

    //年月日時分
    public String getDateAndTime(){
        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return localDateTime.format(dtf);
    }

    //年のみ
    public String formatDateOnlyYear(){
        return formattedDate.substring(0,4);
    }

    //月日のみ
    public String formatToMonthAndDay(){
        return formattedDate.substring(5,10).replace("-","/");
//        if(s.indexOf("0") == s.length()-1 && s.indexOf("0") == 0){
//            return s.replace("0","");
//        }else if(s.indexOf("0") != s.length()-1 && s.indexOf("0") == 0){
//            return s.substring(1,4);
//        }else if ()
    }

    //時分のみ
    //0埋めをしろ
    public String formatDateOnlyHourAndMinute(){
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        Date date = new Date();
        return formattedTime = sdf.format(calendar.getTime());
    }

    //曜日取得
    public String getWeekDay(String date){
        String[] dayOfWeek = {"SUN","MON","TUE","WED","TUS","FRI","SAT"};
        int year = Integer.parseInt(date.substring(0,4));
        int month = Integer.parseInt(date.substring(5,7))-1;
        int day = Integer.parseInt(date.substring(8,10));
        Calendar cal = Calendar.getInstance();
        cal.set(year,month,day);

        return dayOfWeek[cal.get(Calendar.DAY_OF_WEEK)-1];
    }

    //現在の日付けから1日前を取得する
    public String formatYesterday(){
        date = LocalDate.now();
        date.minusDays(1);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return date.format(dtf);
    }

    //月日を0埋め
    public String paddingZeroMonthAndDay(int year,int month, int day){
        String strYear = String.valueOf(year);
        String strMonth = String.format("%02d",month+1);
        String strDayOfMonth = String.format("%02d",day);
        return strYear + strMonth + strDayOfMonth;
    }

    //yyyy/MM/DD HH:MM:DDをHH:MMに変換
    public String formatOnlyTime(String time){

        return time.substring(11, 16);

    }
}
