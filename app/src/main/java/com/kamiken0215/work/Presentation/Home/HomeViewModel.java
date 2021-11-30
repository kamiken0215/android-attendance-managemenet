package com.kamiken0215.work.Presentation.Home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.kamiken0215.work.Domain.Entities.Attendance;
import com.kamiken0215.work.Domain.Repository.IAttendanceRepository;
import com.kamiken0215.work.Domain.Repository.LocalDatabaseCallback;
import com.kamiken0215.work.Domain.Repository.RepositoryCallback;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

public class HomeViewModel extends ViewModel {

    private String userId;
    private String yyyyMM;
    private IAttendanceRepository attendanceRepository;
    private MutableLiveData<List<Attendance>> attendancesLiveData;
    private MutableLiveData<Attendance> attendanceMutableLiveData;

    public HomeViewModel(IAttendanceRepository attendanceRepository) {
        this.attendanceRepository = attendanceRepository;
        //init();
    }

    //表示するためのデータをAPIから取得してDBに保存
    public void init(String userId) {

        //today yyyyMMdd
        LocalDateTime ldt = LocalDateTime.now();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyyMM");
        yyyyMM = fmt.format(ldt);

        attendanceRepository.fetchAttendances(userId,yyyyMM,new RepositoryCallback<List<Attendance>>(){
            @Override
            public void onComplete(Response<List<Attendance>> response, Throwable t) {
                if (response != null) {
                    //200 ~ 300
                    if (response.isSuccessful()) {
                        //SAVE TO DB
                        System.out.println("******* api ******");
                        assert response.body() != null;
                        saveLocalDatabase(response.body());
                    } else if (response.code() > 400 && response.code() < 500) {
                        //loginView.onLoginError(response.code() + "Not Found...");
                    } else {
                        //loginView.onLoginError(response.code() + "Server Error...");
                    }
                } else {
                    //loginView.onLoginError("Check Your Internet Connection");
                }
            }
        });
    }

    private void saveLocalDatabase(final List<Attendance> attendance) {
        userId = attendance.get(0).getUserId();
        attendanceRepository.executeInsertAttendances(attendance, new LocalDatabaseCallback<List<Long>>() {
            @Override
            public void onComplete(List<Long> results) {
                if (results.size() > 0) {
                    System.out.println("******* save ******");
//                    if (isFinished) {
//                        attendancesLiveData.postValue(attendance);
//                    } else {
                        fetchFromLocalDatabase(attendance.get(0).getUserId());
                    //}
                } else {
                    attendancesLiveData.postValue(null);
                }
            }
        });
    }

    private void updateLocalDatabase(final List<Attendance> attendances) {
        userId = attendances.get(0).getUserId();
        attendanceRepository.executeUpdateAttendances(attendances, new LocalDatabaseCallback<Integer>() {
            @Override
            public void onComplete(Integer results) {
                if ((int) results > 0) {
                    System.out.println("******* save ******");
                    fetchFromLocalDatabase(attendances.get(0).getUserId());
                } else {
                    attendancesLiveData.postValue(null);
                }
            }
        });
    }

    public void fetchFromLocalDatabase(String userId) {
        //userId = attendance.get(0).getUserId();
        LocalDateTime ldt = LocalDateTime.now();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyyMM");
        String date = fmt.format(ldt);
        attendanceRepository.executeFindAttendance(userId, date, new LocalDatabaseCallback<List<Attendance>>() {
            @Override
            public void onComplete(List<Attendance> results) {
                System.out.println("******* fetched ******");
                attendancesLiveData.postValue(results);
            }
        });
    }

    private void updateLocalDatabase(final Attendance attendance) {
        List<Attendance> attendances = new ArrayList<>();
        attendances.add(attendance);
        userId = attendance.getUserId();
        attendanceRepository.executeFindAttendance(userId, yyyyMM, new LocalDatabaseCallback<List<Attendance>>() {
            @Override
            public void onComplete(List<Attendance> results) {
                System.out.println("******* updated ******");
                updateLocalDatabase(attendances);
            }
        });
    }

    public void saveAttendance(Attendance attendance,String token) {

        attendanceRepository.saveAttendance(attendance,token, new RepositoryCallback<List<Attendance>>() {
            @Override
            public void onComplete(Response<List<Attendance>> response, Throwable t) {
                if (response != null) {
                    //200 ~ 300
                    if (response.isSuccessful()) {
                        //notice to activity
                        assert response.body() != null;
                        saveLocalDatabase(response.body());
                    } else if (response.code() > 400 && response.code() < 500) {
                        //loginView.onLoginError(response.code() + "Not Found...");
                    } else {
                        //loginView.onLoginError(response.code() + "Server Error...");
                    }
                } else {
                    //loginView.onLoginError("Check Your Internet Connection");
                }
            }
        });
    }

    public void updateAttendance(final Attendance attendance,String token) {
        attendanceRepository.updateAttendance(attendance,token, new RepositoryCallback<Float>() {
            @Override
            public void onComplete(Response<Float> response, Throwable t) {
                if (response != null) {
                    //200 ~ 300
                    if (response.isSuccessful()) {
                        //notice to activity\
                        assert response.body() != null;
                        int updated = Math.round(response.body());
                        if (updated > 0) {
                            //to database
                            updateLocalDatabase(attendance);
                        }
                    } else if (response.code() > 400 && response.code() < 500) {
                        //loginView.onLoginError(response.code() + "Not Found...");
                    } else {
                        //loginView.onLoginError(response.code() + "Server Error...");
                    }
                } else {
                    //loginView.onLoginError("Check Your Internet Connection");
                }
            }
        });
    }

    public LiveData<List<Attendance>> getAttendances() {
        if (attendancesLiveData == null) {
            attendancesLiveData = new MutableLiveData<>();
        }
        return attendancesLiveData;
    }

}
