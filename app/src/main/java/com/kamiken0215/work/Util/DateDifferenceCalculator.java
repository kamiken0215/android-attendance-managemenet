package com.kamiken0215.work.Util;

import java.text.SimpleDateFormat;
import java.util.Date;


public class DateDifferenceCalculator {

    public long diffCalc(String start, String end) {

        if (start.equals("0") || end.equals("0")) {
            return 0;
        }
        //HH converts hour in 24 hours format (0-23), day calculation
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date d1 = null;
        Date d2 = null;

        try {
            d1 = format.parse(start);
            d2 = format.parse(end);

            //in milliseconds
            long diff = d2.getTime() - d1.getTime();

            long diffSeconds = diff/1000 % 60;
            long diffMinutes = diff/(60 * 1000) % 60;
            long diffHours = diff/(60 * 60 * 1000) % 24;
            long diffDays = diff/(24 * 60 * 60 *1000);

            System.out.print(diffDays + " days, ");
            System.out.print(diffHours + " hours, ");
            System.out.print(diffMinutes + " minutes, ");
            System.out.print(diffSeconds + " seconds.");

            return diffHours;

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }

    }
}
