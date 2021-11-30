package com.kamiken0215.work.Domain.UseCase;

import com.kamiken0215.work.Domain.Entities.AttendanceDataModel;
import com.kamiken0215.work.Presentation.Graph.IDataPresenter;
import com.kamiken0215.work.Presentation.Graph.IGraphPresenter;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

public class CalendarUseCase implements ICalendarUseCase {

    //private ISearchAttendanceUseCase searchAttendanceUseCase;
    private IGraphPresenter graphPresenter;
    private IDataPresenter dataPresenter;

//    public CalendarUseCase(ISearchAttendanceUseCase searchAttendanceUseCase) {
//        this.searchAttendanceUseCase = searchAttendanceUseCase;
//    }


    public CalendarUseCase(IGraphPresenter graphPresenter) {
        this.graphPresenter = graphPresenter;
    }

    @Override
    public void formatDate() {

    }

    @Override
    public void formatTime() {

    }

    @Override
    public void formatStatus() {

    }

    @Override
    public List<Integer[]> toWeeklyList(List<AttendanceDataModel> attendanceData,String date){

        //LinkedHashMap<String,Long> workingHoursMap = new LinkedHashMap<>();

        //2 4 6 9 11
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMM");
        YearMonth formatted = YearMonth.parse(date,dtf);
        int month = formatted.getMonthValue();
        int day = formatted.lengthOfMonth();

        List<Integer> workingTimesList = toMonthlyDataList(attendanceData,date);

        List<Integer[]> weeklyList = new ArrayList<>();

        //for(int i = 0; i < workingTimesList.size(); i++){
        Integer[] firstWeekArray = workingTimesList.subList(0,7).toArray(new Integer[7]);
        Integer[] secondWeekArray = workingTimesList.subList(7,14).toArray(new Integer[7]);
        Integer[] thirdWeekArray = workingTimesList.subList(14,21).toArray(new Integer[7]);
        Integer[] fourthWeekArray = workingTimesList.subList(21,28).toArray(new Integer[7]);
        weeklyList.add(firstWeekArray);
        weeklyList.add(secondWeekArray);
        weeklyList.add(thirdWeekArray);
        weeklyList.add(fourthWeekArray);
        if(day == 29){
            Integer[] fifthWeekArray = workingTimesList.subList(28,29).toArray(new Integer[1]);
            weeklyList.add(fifthWeekArray);
        }else if(day == 30){
            Integer[] fifthWeekArray = workingTimesList.subList(28,30).toArray(new Integer[2]);
            weeklyList.add(fifthWeekArray);
        }else if(day == 31){
            Integer[] fifthWeekArray = workingTimesList.subList(28,31).toArray(new Integer[3]);
            weeklyList.add(fifthWeekArray);
        }
        //}

        System.out.println(weeklyList);

        return weeklyList;

    }

    @Override
    public List<String[]> createAxisData(String date){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMM");
        YearMonth formatted = YearMonth.parse(date,dtf);
        String month = date.substring(5,6);
        int lengthOfMonth = formatted.lengthOfMonth();

        List<String[]> axisList = new ArrayList<>();
        LinkedList<String> weeklyAxisList = new LinkedList<>();

        for(int i = 1; i <= lengthOfMonth; i++){
            weeklyAxisList.add(month + "/" + i);
        }

        String[] firstWeekArray = weeklyAxisList.subList(0,7).toArray(new String[7]);
        String[] secondWeekArray = weeklyAxisList.subList(7,14).toArray(new String[7]);
        String[] thirdWeekArray = weeklyAxisList.subList(14,21).toArray(new String[7]);
        String[] fourthWeekArray = weeklyAxisList.subList(21,28).toArray(new String[7]);
        axisList.add(firstWeekArray);
        axisList.add(secondWeekArray);
        axisList.add(thirdWeekArray);
        axisList.add(fourthWeekArray);

        if(lengthOfMonth == 29){
            String[] fifthWeekArray = weeklyAxisList.subList(28,29).toArray(new String[1]);
            axisList.add(fifthWeekArray);
        }else if(lengthOfMonth == 30){
            String[] fifthWeekArray = weeklyAxisList.subList(28,30).toArray(new String[1]);
            axisList.add(fifthWeekArray);
        }else if(lengthOfMonth == 31){
            String[] fifthWeekArray = weeklyAxisList.subList(28,31).toArray(new String[1]);
            axisList.add(fifthWeekArray);
        }

        System.out.println(axisList);

        return axisList;
    }

    private List<Integer> toMonthlyDataList(List<AttendanceDataModel> attendanceData, String date){

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMM");
        YearMonth formatted = YearMonth.parse(date,dtf);
        int year = Integer.parseInt(date.substring(0,4));
        int month = Integer.parseInt(date.substring(5,6));
        int day = 1;
        int lengthOfMonth = formatted.lengthOfMonth();

        LinkedHashMap<String,Integer> workingHoursMap = new LinkedHashMap<>();
        List<Integer> workingTimesList = new ArrayList<>();

        int index = 0;
        for(int i = 0; i < lengthOfMonth; i++){

            String formattedDate = paddingZeroMonthAndDay(year,month,day);

            if(index < attendanceData.size() && attendanceData.get(index) != null){
                String workingDay = attendanceData.get(index).getDate();
                if(workingDay.equals(formattedDate)) {
                    String startTime = attendanceData.get(index).getStartTime();
                    String finishTime = attendanceData.get(index).getFinishTime();
                    if (startTime != null && finishTime != null) {
                        workingTimesList.add(calcWorkingHours(startTime, finishTime));
                    } else {
                        workingTimesList.add(0);
                    }
                    index += 1;
                }else {
                    workingTimesList.add(0);
                }
            }else {
                workingTimesList.add(0);
            }
            day += 1;
        }

        System.out.println(workingTimesList);

        return workingTimesList;

    }

    private int calcWorkingHours(String startTime, String finishTime){
        String sT = startTime.substring(0,19);
        String sF = finishTime.substring(0,19);
        System.out.println(sT);
        System.out.println(sF);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime lcStartTime = LocalDateTime.parse(sT,dtf);
        LocalDateTime lcFinishTime = LocalDateTime.parse(sF,dtf);
        Duration d = Duration.between(lcStartTime, lcFinishTime);

        //long localDiffTime = ChronoUnit.MINUTES.between(lcStartTime,lcFinishTime)/60;
        int localDiffTime = (int) d.toMinutes()/60;
        System.out.println(localDiffTime);
        return localDiffTime;
    }

    //月日を0埋め
    private String paddingZeroMonthAndDay(int year,int month, int day){
        String strYear = String.valueOf(year);
        String strMonth = String.format("%02d",month);
        String strDayOfMonth = String.format("%02d",day);
        return strYear + "-" + strMonth + "-" + strDayOfMonth;
    }

}
