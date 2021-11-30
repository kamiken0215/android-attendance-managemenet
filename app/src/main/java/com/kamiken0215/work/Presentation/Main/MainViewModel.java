package com.kamiken0215.work.Presentation.Main;

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

public class MainViewModel extends ViewModel {

    private static final String RESPONSE_IS_NULL = "NO RESPONSE\nPLEASE CHECK NETWORK";
    private static final String RESPONSE_CODE_400 = "NO DATA";
    private static final String RESPONSE_CODE_500 = "SERVER ERROR";

    private String userId;
    private String yyyyMM;
    private IAttendanceRepository attendanceRepository;
    private MutableLiveData<List<Attendance>> attendancesLiveData;
    private MutableLiveData<Attendance> attendanceMutableLiveData;
    private MutableLiveData<String> errorLiveData;

    public MainViewModel(IAttendanceRepository attendanceRepository) {
        this.attendanceRepository = attendanceRepository;
    }

    public void init(String userId) {
        //today yyyyMMdd
        LocalDateTime ldt = LocalDateTime.now();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyyMM");
        yyyyMM = fmt.format(ldt);

        attendanceRepository.fetchAttendances(userId,yyyyMM, (response, t) -> {
            if (response != null) {
                //200 ~ 300
                if (response.isSuccessful()) {
                    //SAVE TO DB
                    System.out.println("******* api fetched complete ******");
                    assert response.body() != null;
                    //saveLocalDatabase(response.body());
                    attendancesLiveData.postValue(response.body());
                } else if (response.code() > 400 && response.code() < 500) {
                    errorLiveData.postValue(RESPONSE_CODE_400);
                } else {
                    errorLiveData.postValue(RESPONSE_CODE_500);
                }
            } else {
                errorLiveData.postValue(RESPONSE_IS_NULL);
            }
        });
    }

    public void fetchDayFromApi(String userId,String date) {
        attendanceRepository.fetchAttendances(userId, date, (response, t) -> {
            if (response != null) {
                //200 ~ 300
                if (response.isSuccessful()) {
                    //SAVE TO DB
                    System.out.println("******* api fetched complete ******");
                    assert response.body() != null;
                    //saveLocalDatabase(response.body());
                    attendancesLiveData.postValue(response.body());
                } else if (response.code() > 400 && response.code() < 500) {
                    errorLiveData.postValue(RESPONSE_CODE_400);
                } else {
                    errorLiveData.postValue(RESPONSE_CODE_500);
                }
            } else {
                errorLiveData.postValue(RESPONSE_IS_NULL);
            }
        });
    }

    public void fetchYearMonth(String userId,String yyyyMM) {

        attendanceRepository.fetchAttendances(userId,yyyyMM, (response, t) -> {
            if (response != null) {
                //200 ~ 300
                if (response.isSuccessful()) {
                    //SAVE TO DB
                    System.out.println("******* api fetched complete ******");
                    assert response.body() != null;
                    //saveLocalDatabase(response.body());
                    attendancesLiveData.postValue(response.body());
                } else if (response.code() > 400 && response.code() < 500) {
                    errorLiveData.postValue(RESPONSE_CODE_400);
                } else {
                    errorLiveData.postValue(RESPONSE_CODE_500);
                }
            } else {
                errorLiveData.postValue(RESPONSE_IS_NULL);
            }
        });
    }

    private void saveLocalDatabase(final List<Attendance> attendance) {
        if (attendance.size() > 0) {
            userId = attendance.get(0).getUserId();
            attendanceRepository.executeInsertAttendances(attendance, results -> {
                if (results.size() > 0) {
                    System.out.println("******* save local complete ******");
//                    if (isFinished) {
//                        attendancesLiveData.postValue(attendance);
//                    } else {
                    fetchFromLocalDatabase(attendance.get(0).getUserId());
                    //}
                } else {
                    attendancesLiveData.postValue(null);
                }
            });
        } else {
            attendancesLiveData.postValue(null);
        }
    }

    private void updateLocalDatabase(final List<Attendance> attendances) {
        userId = attendances.get(0).getUserId();
        attendanceRepository.executeUpdateAttendances(attendances, (LocalDatabaseCallback<Integer>) results -> {
            if ((int) results > 0) {
                System.out.println("******* local saved complete ******");
                fetchFromLocalDatabase(attendances.get(0).getUserId());
            } else {
                attendancesLiveData.postValue(null);
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
                System.out.println("******* local fetched complete ******");
                attendancesLiveData.postValue(results);
            }
        });
    }

    public void fetchDayFromLocalDatabase(String userId,String date) {
        attendanceRepository.executeFindAttendance(userId, date, new LocalDatabaseCallback<List<Attendance>>() {
            @Override
            public void onComplete(List<Attendance> results) {
                System.out.println("******* fetched "+ date + "******");
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
                System.out.println("******* local update start ******");
                updateLocalDatabase(attendances);
            }
        });
    }

    //home fragment用
    public void saveAttendance(Attendance attendance,String userId,String token) {

        System.out.println("******* api save start ******");

        attendanceRepository.saveAttendance(attendance,token, (response, t) -> {
            if (response != null) {
                //200 ~ 300
                if (response.isSuccessful()) {
                    //notice to activity
                    assert response.body() != null;
                    //saveLocalDatabase(response.body());
                    System.out.println("******* api save complete ******");
                    //fetchDayFromApi(attendance.getUserId(),attendance.getAttendanceDate());
                    init(userId);
                } else if (response.code() > 400 && response.code() < 500) {
                    System.out.println("******* api save failure ******");
                    errorLiveData.postValue(RESPONSE_CODE_400);
                } else {
                    System.out.println("******* api save failure ******");
                    errorLiveData.postValue(RESPONSE_CODE_500);
                }
            } else {
                System.out.println("******* api save failure ******");
                errorLiveData.postValue(RESPONSE_IS_NULL);
            }
        });
    }

    //Calendar fragment用
    public void saveAttendance(Attendance attendance,String token) {

        System.out.println("******* api save start ******");

        attendanceRepository.saveAttendance(attendance,token, (response, t) -> {
            if (response != null) {
                //200 ~ 300
                if (response.isSuccessful()) {
                    //notice to activity
                    assert response.body() != null;
                    //saveLocalDatabase(response.body());
                    System.out.println("******* api save complete ******");
                    fetchDayFromApi(attendance.getUserId(),attendance.getAttendanceDate());
                } else if (response.code() > 400 && response.code() < 500) {
                    System.out.println("******* api save failure ******");
                    errorLiveData.postValue(RESPONSE_CODE_400);
                } else {
                    System.out.println("******* api save failure ******");
                    errorLiveData.postValue(RESPONSE_CODE_500);
                }
            } else {
                System.out.println("******* api save failure ******");
                errorLiveData.postValue(RESPONSE_IS_NULL);
            }
        });
    }

    //home fragment用
    public void updateAttendance(final Attendance attendance,String userId,String token) {
        System.out.println("******* api update start ******");
        attendanceRepository.updateAttendance(attendance,token, new RepositoryCallback<Float>() {
            @Override
            public void onComplete(Response<Float> response, Throwable t) {
                if (response != null) {
                    //200 ~ 300
                    if (response.isSuccessful()) {
                        //notice to activity
                        assert response.body() != null;
                        int updated = Math.round(response.body());
                        if (updated > 0) {
                            //to database
                            System.out.println("******* api update complete ******");
                            init(userId);
                            //updateLocalDatabase(attendance);
                        }
                    } else if (response.code() > 400 && response.code() < 500) {
                        errorLiveData.postValue(RESPONSE_CODE_400);
                    } else {
                        errorLiveData.postValue(RESPONSE_CODE_500);
                    }
                } else {
                    errorLiveData.postValue(RESPONSE_IS_NULL);
                }
            }
        });
    }

    //calendar fragment用
    public void updateAttendance(final Attendance attendance,String token) {
        System.out.println("******* api update start ******");
        attendanceRepository.updateAttendance(attendance,token, (response, t) -> {
            if (response != null) {
                //200 ~ 300
                if (response.isSuccessful()) {
                    //notice to activity
                    assert response.body() != null;
                    int updated = Math.round(response.body());
                    if (updated > 0) {
                        //to database
                        System.out.println("******* api update complete ******");
                        fetchDayFromApi(attendance.getUserId(),attendance.getAttendanceDate());
                        //updateLocalDatabase(attendance);
                    }
                } else if (response.code() > 400 && response.code() < 500) {
                    errorLiveData.postValue(RESPONSE_CODE_400);
                } else {
                    errorLiveData.postValue(RESPONSE_CODE_500);
                }
            } else {
                errorLiveData.postValue(RESPONSE_IS_NULL);
            }
        });
    }

    public LiveData<List<Attendance>> getAttendances() {
        if (attendancesLiveData == null) {
            attendancesLiveData = new MutableLiveData<>();
        }
        return attendancesLiveData;
    }

    public LiveData<List<Attendance>> getTodayAttendances(String userId, String date) {

        if (attendancesLiveData == null) {
            attendancesLiveData = new MutableLiveData<>();
            fetchDayFromApi(userId, date);
        }
        return attendancesLiveData;
    }

    public LiveData<String> getError() {
        if (errorLiveData == null) {
            errorLiveData = new MutableLiveData<>();
        }
        return errorLiveData;
    }

}
