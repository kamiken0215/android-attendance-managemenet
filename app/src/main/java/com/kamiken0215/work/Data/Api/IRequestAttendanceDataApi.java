package com.kamiken0215.work.Data.Api;

import com.kamiken0215.work.Domain.Entities.Attendance;
import com.kamiken0215.work.Domain.Entities.AttendanceDataModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface IRequestAttendanceDataApi {
    @GET("data/users/{userId}/attendances")
    Call<List<AttendanceDataModel>> getAllAttendanceData(@Path("userId") int userId);

    @GET("/attendances/{userId}/{date}")
    Call<List<Attendance>> fetchAttendances(@Path("userId") final String userId, @Path("date") final String date);

    @POST("/attendances")
    //Call<Attendance> saveAttendance(@Body Attendance attendance, @Path("token") final String token);
    Call<List<Attendance>> saveAttendance(@Header("Authorization") String auth, @Body Attendance attendance);

    @GET("data/users/{userId}/attendances/{date}")
    Call<List<AttendanceDataModel>> getOneAttendanceData(@Path("userId") int userId, @Path("date") String date);

    @PUT("data/attendances")
    //Call<AttendanceDataModel> putAttendanceData(@Body AttendanceDataModel body);
    Call<List<AttendanceDataModel>> putAttendanceData(@Body AttendanceDataModel body);

    @PUT("/attendances/{userId}/{date}")
    Call<Float> updateAttendance(@Header("Authorization") String auth, @Body Attendance attendance, @Path("userId") String userId, @Path("date") final String date);
    //Call<AttendanceDataModel> putAttendanceData(@Body AttendanceDataModel body);
}
