package com.kamiken0215.work.Data.Repository;

import android.app.Application;
import android.util.Log;

import com.kamiken0215.work.Data.Api.IRequestAttendanceDataApi;
import com.kamiken0215.work.Data.Api.RetrofitSingleton;
import com.kamiken0215.work.Data.Api.ServiceCreator;
import com.kamiken0215.work.Data.Dao.IAttendanceDao;
import com.kamiken0215.work.Data.Dao.IUserDao;
import com.kamiken0215.work.Data.DateResource.AttendanceDatabase;
import com.kamiken0215.work.Domain.Entities.Attendance;
import com.kamiken0215.work.Domain.Entities.AttendanceDataModel;
import com.kamiken0215.work.Domain.Entities.User;
import com.kamiken0215.work.Domain.Repository.IAttendanceRepository;
import com.kamiken0215.work.Domain.Repository.LocalDatabaseCallback;
import com.kamiken0215.work.Domain.Repository.RepositoryCallback;
import com.kamiken0215.work.Domain.UseCase.Attendance.IAttendanceUseCases;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;

public class AttendanceRepository implements IAttendanceRepository {

    private RetrofitSingleton retrofitSingleton;
    private IUserDao userDao;
    private IAttendanceDao attendanceDao;
    private List<User> user;
    private List<Attendance> attendances;
    private Attendance attendanceModel;
    private String userId;
    //private String token;

    //UseCases
    private IAttendanceUseCases attendanceUseCases;

    public AttendanceRepository(Application application, RetrofitSingleton retrofitSingleton) {
        this.retrofitSingleton = retrofitSingleton;
//        UserDatabase userDatabase = UserDatabase.getInstance(application);
//        userDao = userDatabase.userDao();
        AttendanceDatabase attendanceDatabase = AttendanceDatabase.getInstance(application);
        attendanceDao = attendanceDatabase.attendanceDao();
        //init();
    }

//    public void init() {
//        executeFetchUser();
//        executeDeleteAllAttendances();
//    }

    @Override
    public void fetchAttendanceData(int userId, String date) {
        //Api
        IRequestAttendanceDataApi service = retrofitSingleton.getRetrofit().create(IRequestAttendanceDataApi.class);
        Call<List<AttendanceDataModel>> call = service
                .getOneAttendanceData(userId,date);
        call.enqueue(new Callback<List<AttendanceDataModel>>() {
            @Override
            public void onResponse(Call<List<AttendanceDataModel>> call, retrofit2.Response<List<AttendanceDataModel>> response) {
                if (response.isSuccessful()){
                    System.out.println(response.body());
                    attendanceUseCases.onFetchFinished(response.body());
                }
                else {
                    System.out.println(response.code());
                }
            }
            @Override
            public void onFailure(Call<List<AttendanceDataModel>> call, Throwable t) {
                attendanceUseCases.onFetchFailure(t);
            }
        });
    }


    @Override
    public void fetchAttendances(final String userId, final String date, final RepositoryCallback<List<Attendance>> callback) {
        //final User loginUser = new User(email,password);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                IRequestAttendanceDataApi service = retrofitSingleton.getRetrofit().create(IRequestAttendanceDataApi.class);
                Call<List<Attendance>> call = service.fetchAttendances(userId,date);
                call.enqueue(new Callback<List<Attendance>>() {
                    @Override
                    public void onResponse(Call<List<Attendance>> call, retrofit2.Response<List<Attendance>> response) {
                        if(response.isSuccessful()) {
                            //update or insert to local database
                            //attendances = response.body();
                            //saveAttendances(date);
                            callback.onComplete(response,null);
                        } else {
                            callback.onComplete(response,null);
                        }
                    }
                    @Override
                    public void onFailure(Call<List<Attendance>> call, Throwable t) {
                        Log.d("api/attendance_repository","cant get data");
                        callback.onComplete(null,t);
                    }
                });
            }
        });
    }

    @Override
    public void saveAttendance(final Attendance attendanceModel,final String token, final RepositoryCallback<List<Attendance>> callback) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                IRequestAttendanceDataApi service = retrofitSingleton.getRetrofit().create(IRequestAttendanceDataApi.class);
                System.out.println("token *************"+token);
                final Call<List<Attendance>> call = service.saveAttendance("Bearer "+token,attendanceModel);
                call.enqueue(new Callback<List<Attendance>>() {
                    @Override
                    public void onResponse(Call<List<Attendance>> call, retrofit2.Response<List<Attendance>> response) {
                        if(response.isSuccessful()) {
                            //update or insert to local database
                            //attendances = response.body();
                            callback.onComplete(response,null);
                        } else {
                            callback.onComplete(response,null);
                        }
                    }
                    @Override
                    public void onFailure(Call<List<Attendance>> call, Throwable t) {
                        Log.d("api/attendance_repository","cant get data");
                        callback.onComplete(null,t);
                    }
                });
            }
        });
    }

    @Override
    public void updateAttendance(final Attendance attendanceModel,final String token, final RepositoryCallback<Float> callback) {
        String userId = attendanceModel.getUserId();
        String date = attendanceModel.getAttendanceDate();
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                IRequestAttendanceDataApi service = retrofitSingleton.getRetrofit().create(IRequestAttendanceDataApi.class);
                System.out.println("***** token **** "+token);
                final Call<Float> call = service.updateAttendance("Bearer "+token,attendanceModel,userId,date);
                call.enqueue(new Callback<Float>() {
                    @Override
                    public void onResponse(Call<Float> call, retrofit2.Response<Float> response) {
                        if(response.isSuccessful()) {
                            //update or insert to local database
                            //attendances = response.body();
                            callback.onComplete(response,null);
                        } else {
                            callback.onComplete(response,null);
                        }
                    }
                    @Override
                    public void onFailure(Call<Float> call, Throwable t) {
                        Log.d("api/attendance_repository","cant get data");
                        callback.onComplete(null,t);
                    }
                });
            }
        });
    }

    @Override
    public void searchAttendanceData(int userId, String date) {

        //Api
        IRequestAttendanceDataApi service = retrofitSingleton.getRetrofit().create(IRequestAttendanceDataApi.class);
        Call<List<AttendanceDataModel>> call = service
                .getOneAttendanceData(userId,date);
        call.enqueue(new Callback<List<AttendanceDataModel>>() {

            @Override
            public void onResponse(Call<List<AttendanceDataModel>> call, retrofit2.Response<List<AttendanceDataModel>> response) {
                if (response.isSuccessful()){
                    attendanceUseCases.onSearchFinished(response.body());
                }
                else {
                    System.out.println(response.code());
                }
            }

            @Override
            public void onFailure(Call<List<AttendanceDataModel>> call, Throwable t) {
                attendanceUseCases.onSearchFailure(t);
            }
        });
    }

    //update
    @Override
    public void putAttendanceData(int userId,
                                  String date,
                                  String startTime,
                                  String finishTime,
                                  int workStatus,
                                  int attendanceFlag,
                                  int straddlingFlag) {
        //Api
        ServiceCreator serviceCreator = new ServiceCreator();
        IRequestAttendanceDataApi service = serviceCreator.createService(IRequestAttendanceDataApi.class);
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
                    attendanceUseCases.onUpdateFinished(response.body());
                }
                else {
                    System.out.println(response.code());
                }
            }

            @Override
            public void onFailure(Call<List<AttendanceDataModel>> call, Throwable t) {
                attendanceUseCases.onUpdateFailure(t);
            }
        });
    }

    @Override
    public void executeFindAttendance(final String userId, final String date, final LocalDatabaseCallback<List<Attendance>> callback) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                callback.onComplete(attendanceDao.findById(userId,date));
            }
        });
    }

    @Override
    public void executeInsertAttendances(final List<Attendance> attendances, final LocalDatabaseCallback<List<Long>> callback) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                callback.onComplete(attendanceDao.insert(attendances));
            }
        });
    }

    @Override
    public void executeUpdateAttendances(final List<Attendance> attendances, final LocalDatabaseCallback<Integer> callback) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                callback.onComplete(attendanceDao.update(attendances));
            }
        });
    }

    private void executeDeleteAllAttendances() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                attendanceDao.deleteAll();
            }
        });
    }

    @Override
    public void executeDeleteLocalAttendances(final LocalDatabaseCallback<Long> callback) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                attendanceDao.deleteAll();
                callback.onComplete((long) 1);
            }
        });
    }

}
