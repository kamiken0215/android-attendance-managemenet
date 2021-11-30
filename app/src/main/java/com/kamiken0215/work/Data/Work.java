package com.kamiken0215.work.Data;

import android.content.Context;

import com.kamiken0215.work.Data.Api.IRequestAttendanceDataApi;
import com.kamiken0215.work.Data.Api.OkHttpSingleton;
import com.kamiken0215.work.Domain.Entities.AttendanceDataModel;
import com.kamiken0215.work.Util.DateFormatter;
import com.kamiken0215.work.Util.JsonFromatter;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;


//おそうじしよう
public class Work implements IWork {

    private String strDate;
    private String time;
    private Context context;

    private List<String> resultList = new ArrayList<>();
    private List<Long> workTimes = new ArrayList<>();

    private JsonFromatter jsonFromatter = new JsonFromatter();

    //出退勤時刻をDBに登録

    @Override
    public String getDate() {
        DateFormatter dateFormatter = new DateFormatter();
        return dateFormatter.getDate();
    }

    public String getDateAndTime() {
        DateFormatter dateFormatter = new DateFormatter();
        return dateFormatter.getDateAndTime();
    }

    @Override
    public String getTime() {
        DateFormatter dateFormatter = new DateFormatter();
        this.time = dateFormatter.formatDateOnlyHourAndMinute();
        return time;
    }

    @Override
    public void setWorkingTime(String date, String time, Boolean isInMode) {
        this.strDate = date;
        this.time = time;
    }

    //initFetchAttendanceData
    //1 check yesterday
    //2 has yesterday --
    @Override
    public List<String> fetchAttendanceData(int userId, String date){
        
        //Api
        IRequestAttendanceDataApi service = createService(IRequestAttendanceDataApi.class);
        Call<List<AttendanceDataModel>> call = service
                .getOneAttendanceData(userId,date);
        call.enqueue(new Callback<List<AttendanceDataModel>>() {

            @Override
            public void onResponse(Call<List<AttendanceDataModel>> call, retrofit2.Response<List<AttendanceDataModel>> response) {
                if (response.isSuccessful()){
                    List<AttendanceDataModel> attendanceData = response.body();
                    if(attendanceData.size() > 0) {
//                        resultList.add(attendanceData.get(0).getStartTime());
//                        resultList.add(attendanceData.get(0).getFinishTime());
                        //参照きえとる
                        formatOneDayTimesAndStatus(attendanceData);
                    }

                }
                else {
                    System.out.println(response.code());
                }
            }

            @Override
            public void onFailure(Call<List<AttendanceDataModel>> call, Throwable t) {
                System.out.println(t);
            }
        });

        return resultList;
    }

    //return int array
    public List<Long> searchAttendanceDataTimes(int userId, String date){

        int[] timeData = new int[0];

        //Api
        IRequestAttendanceDataApi service = createService(IRequestAttendanceDataApi.class);
        Call<List<AttendanceDataModel>> call = service
                .getOneAttendanceData(userId,date);
        call.enqueue(new Callback<List<AttendanceDataModel>>() {

            @Override
            public void onResponse(Call<List<AttendanceDataModel>> call, retrofit2.Response<List<AttendanceDataModel>> response) {
                if (response.isSuccessful()){
                    List<AttendanceDataModel> attendanceData = response.body();
                    List<String> times = new ArrayList<>();
                    Gson gson = new Gson();
                    if(attendanceData.size() > 0) {
                        resultList.add(attendanceData.get(0).getStartTime());
                        resultList.add(attendanceData.get(0).getFinishTime());
                        //参照きえとる
                        //formatSearchAttendanceData(attendanceData);
                    }

                }
                else {
                    System.out.println(response.code());
                }
            }

            @Override
            public void onFailure(Call<List<AttendanceDataModel>> call, Throwable t) {
                System.out.println(t);
            }
        });

        return workTimes;
    }

    @Override
    public List<String> searchAttendanceData(int userId){

        //Api
        IRequestAttendanceDataApi service = createService(IRequestAttendanceDataApi.class);
        Call<List<AttendanceDataModel>> call = service
                .getAllAttendanceData(userId);
        call.enqueue(new Callback<List<AttendanceDataModel>>() {

            @Override
            public void onResponse(Call<List<AttendanceDataModel>> call, retrofit2.Response<List<AttendanceDataModel>> response) {
                if (response.isSuccessful()){
                    List<AttendanceDataModel> attendanceData = response.body();
                    //System.out.println(attendanceData.getStartTime());
                    if(attendanceData.size() > 0) {
                        resultList.add(attendanceData.get(0).getStartTime());
                        resultList.add(attendanceData.get(0).getFinishTime());
                    }
                    jsonFromatter.formatSearchAttendanceData(attendanceData);
                }
                else {
                    System.out.println(response.code());
                }
            }

            @Override
            public void onFailure(Call<List<AttendanceDataModel>> call, Throwable t) {
                System.out.println(t);
            }
        });

        return resultList;
    }

    //update
    public List<String> putAttendanceData(int userId, String date , String startTime, String finishTime, int workStatus,int attendanceFlag, int straddlingFlag){
        //Api
        IRequestAttendanceDataApi service = createService(IRequestAttendanceDataApi.class);
        Call<List<AttendanceDataModel>> call = service
                .putAttendanceData(new AttendanceDataModel(
                        userId
                        ,date
                        ,startTime
                        ,finishTime
                        ,workStatus
                        ,attendanceFlag
                        ,straddlingFlag
                ));
        call.enqueue(new Callback<List<AttendanceDataModel>>() {

            @Override
            public void onResponse(Call<List<AttendanceDataModel>> call, retrofit2.Response<List<AttendanceDataModel>> response) {
                if (response.isSuccessful()){
                    List<AttendanceDataModel> attendanceData = response.body();
                }
                else {
                    System.out.println(response.code());
                }
            }

            @Override
            public void onFailure(Call<List<AttendanceDataModel>> call, Throwable t) {
                System.out.println(t);
            }
        });
        return resultList;
    }

    private <T> T createService(Class<T> serviceClass){

        OkHttpSingleton okHttpSingleton = OkHttpSingleton.getInstance();

        OkHttpClient client = okHttpSingleton.getOkHttpClient();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DefineApi.HOME)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        return retrofit.create(serviceClass);
    }

    private void formatOneDayTimesAndStatus(List<?> response){

        String s = response.toString();

        try {

            JSONArray jsonArray = new JSONArray(s);

            //format to json
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                resultList.add(obj.getString("startTime"));
                resultList.add(obj.getString("finishTime"));
                resultList.add(String.valueOf(obj.getInt("workStatus")));
                System.out.println(obj);
            }

        }catch (JSONException e){
            System.out.println(e);
        }
    }


    //change List to array
    private void formatSearchAttendanceData(List<?> response){

        List<String> workInfo = new ArrayList<>();


        String s = response.toString();

        try {

            JSONArray jsonArray = new JSONArray(s);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                //開始時間と終了時間を取得してdatetime型に変換
                //労働時間を計算
                //配列の要素i番目にぶち込む
                String startTime = obj.getString("startTime");
                String finishTime = obj.getString("finishTime");

                if(!startTime.equals("null") && !finishTime.equals("null")) {
                    workTimes.add(calcWorkTime(startTime, finishTime));
                }
                System.out.println(workTimes);
            }
        }catch (JSONException e){
            System.out.println(e);
        }

    }

    //日付けに変換して計算するメソッド
    private long calcWorkTime(String startTime, String finishTime){
        String sT = startTime.substring(0,19);
        String sF = finishTime.substring(0,19);
        System.out.println(sT);
        System.out.println(sF);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime lcStartTime = LocalDateTime.parse(sT,dtf);
        LocalDateTime lcFinishTime = LocalDateTime.parse(sF,dtf);
        Duration d = Duration.between(lcStartTime, lcFinishTime);

        //long localDiffTime = ChronoUnit.MINUTES.between(lcStartTime,lcFinishTime)/60;
        long localDiffTime = d.toMinutes() /60;
        System.out.println(localDiffTime);
        return localDiffTime;
    }

}
