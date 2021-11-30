package com.kamiken0215.work.Presentation.Calendar;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.kamiken0215.work.Domain.Entities.Attendance;
import com.kamiken0215.work.Presentation.Main.MainViewModel;
import com.kamiken0215.work.Presentation.Main.MainViewModelFactory;
import com.kamiken0215.work.Presentation.TimePicker.TimePickerDialogFragment;
import com.kamiken0215.work.R;
import com.kamiken0215.work.Util.AttendanceStatusChecker;
import com.kamiken0215.work.Util.DateFormatter;
import com.kamiken0215.work.Util.OptionalWrapper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

import es.dmoral.toasty.Toasty;

public class CalendarFragment extends Fragment implements ICalendarView, TimePickerDialog.OnTimeSetListener {

    ConstraintLayout submitRootView;
    Boolean isStart = false;
    private Boolean dialogIsOn = true;
    //private TextView tvDay;
    private TextView tvWorkStatus;
    private TextView tvStartTime;
    private TextView tvEndTime;
    private TextView tvMemo;

    private MainViewModel mainViewModel;

    private FloatingActionButton fabSubmit;

    private CalendarView calendarView;
    private DateFormatter dateFormatter = new DateFormatter();

    private ProgressBar progressBar;
    private LinearLayout bgApiProcess;

    private ProgressBar submitProgressBar;
    private LinearLayout submitBgApiProcess;

    private int userId;
    private String date;
    private String selectedDate;
    //HH:mm
    private String selectedStartTime = "--:--";
    private String selectedEndTime = "--:--";

    //update or insert
    private boolean shouldInsert;

    private String updateDate;
    private int workStatus;
    private String startTime;
    private String finishTime;
    private Spinner spinnerWork;

    private String attendanceClass;

    OptionalWrapper optionalWrapper = new OptionalWrapper();
    AttendanceStatusChecker attendanceStatusChecker = new AttendanceStatusChecker();

    //Full Screen Dialog Materials
    private Dialog editDialog;
    private TextView tvSubmitDay;
    Button btnEditStartTime;
    Button btnEditEndTime;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        // 先ほどのレイアウトをここでViewとして作成します
        return inflater.inflate(R.layout.frgment_calendar, container, false);

    }

    // Viewが生成し終わった時に呼ばれるメソッド
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        Bundle bundle = getArguments();
        assert bundle != null;
        String userId = bundle.getString("USER_ID");
        String token = bundle.getString("TOKEN");

        AssetManager assetManager = getResources().getAssets();
        //setup texttype
        Typeface avenir = Typeface.createFromAsset(assetManager,"AvenirLTStd-Black.otf");
        Typeface menlo = Typeface.createFromAsset(assetManager,"Menlo-Bold.ttf");

        tvWorkStatus = view.findViewById(R.id.calendar_tv_work_status);
        tvStartTime = view.findViewById(R.id.calendar_tv_start_time);
        tvEndTime = view.findViewById(R.id.calendar_tv_end_time);
        tvMemo = view.findViewById(R.id.calendar_tv_memo);

        //tvDay.setTypeface(menlo);
        tvWorkStatus.setTypeface(menlo);
        tvStartTime.setTypeface(menlo);
        tvEndTime.setTypeface(menlo);
        tvMemo.setTypeface(avenir);


        progressBar = view.findViewById(R.id.progress_bar);
        bgApiProcess = view.findViewById(R.id.background_api_process);

        calendarView = view.findViewById(R.id.calendar_view);

        //today
        DateFormatter df = new DateFormatter();
        String today = df.getTodayExceptSlash();
        selectedDate = today;

        mainViewModel = new ViewModelProvider(this, new MainViewModelFactory(Objects.requireNonNull(getActivity()).getApplication())).get(MainViewModel.class);
        mainViewModel.getTodayAttendances(userId,today).observe(getViewLifecycleOwner(), attendance -> {
            if (attendance.size() != 0) {

                String startTime = optionalWrapper.getOptionalString(attendance.get(0).getStartTime()).orElse("--:--");
                String endTime = optionalWrapper.getOptionalString(attendance.get(0).getEndTime()).orElse("--:--");

                if (startTime.length() > 15) {
                    selectedStartTime = startTime.substring(11, 16);
                } else {
                    selectedStartTime = "--:--";
                }
                tvStartTime.setText(selectedStartTime);

                if (endTime.length() > 15) {
                    selectedEndTime = endTime.substring(11, 16);
                } else {
                    selectedEndTime = "--:--";
                }
                tvEndTime.setText(selectedEndTime);

                tvWorkStatus.setText(attendanceStatusChecker.statusChecker(attendance.get(0).getAttendanceClass()));
                tvMemo.setText(optionalWrapper.getOptionalString(attendance.get(0).getNotes()).orElse(""));
                shouldInsert = false;
            } else {
                selectedStartTime = "--:--";
                tvStartTime.setText(selectedStartTime);
                selectedEndTime = "--:--";
                tvEndTime.setText(selectedEndTime);
                tvWorkStatus.setText("");
                tvMemo.setText("");
                shouldInsert = true;
            }

            setSubmitDate(selectedDate,selectedStartTime,selectedEndTime);
            hideLoading();
            if (!dialogIsOn) {
                editDialog.dismiss();
                Toasty.success(Objects.requireNonNull(getActivity()).getApplicationContext(),"UPDATE SUCCESS！",Toast.LENGTH_SHORT).show();
            }
        });

        //カレンダーの日付をタップした時の処理
        calendarView.setOnDateChangeListener((v, year, month, dayOfMonth) -> {

            showLoading();
            //YYYYMMDDに変換
            selectedDate = dateFormatter.paddingZeroMonthAndDay(year,month,dayOfMonth);
            //mainViewModel.fetchDayFromLocalDatabase(userId,selectedDate);
            //calendarPresenter.fetchAttendanceData(userId,selectedDate);
            //当該日付の出勤データを取得する
            mainViewModel.fetchDayFromApi(userId,selectedDate);
        });

        //fab listener
        fabSubmit = view.findViewById(R.id.calendar_fab_submit);

        fabSubmit.setOnClickListener(v -> {
            dialogIsOn = true;
            editDialog.show();
        });


        //create and set full screen edit dialog
        View dialogView = getLayoutInflater().inflate(R.layout.full_screen_submit_dialog,(ViewGroup)null);
        editDialog = new Dialog(Objects.requireNonNull(getContext()),android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
        editDialog.setContentView(dialogView);

        submitRootView = dialogView.findViewById(R.id.submit_view_root);

        submitBgApiProcess = dialogView.findViewById(R.id.submit_background_api_process);
        submitProgressBar = dialogView.findViewById(R.id.submit_progress_bar);

        tvSubmitDay = dialogView.findViewById(R.id.submit_tv_day);
        tvSubmitDay.setText(selectedDate.substring(0, 4) + " / " + selectedDate.substring(4, 6) + " / " + selectedDate.substring(6, 8));

        Resources res = getResources();
        String spinnerItem[] = res.getStringArray(R.array.spinner_working);
        spinnerWork = dialogView.findViewById(R.id.submit_spinner);
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(Objects.requireNonNull(getActivity()).getBaseContext()
                ,R.layout.custom_spinner
                ,spinnerItem);
        adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown);
        spinnerWork.setAdapter(adapter);

        btnEditStartTime = dialogView.findViewById(R.id.submit_btn_start);
        btnEditEndTime = dialogView.findViewById(R.id.submit_btn_end);

        Button btnEditSubmit = dialogView.findViewById(R.id.submit_btn_submit);
        Button btnEditDialogClose = dialogView.findViewById(R.id.submit_btn_close);


        //spinner
        spinnerWork.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Spinner spinner = (Spinner) parent;
                String item = (String) spinner.getSelectedItem();
                System.out.println(item);
                System.out.println(id);
                attendanceClass = String.valueOf((int)id + 1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        TimePickerDialog.OnTimeSetListener timePickerListener =
            (v, hourOfDay, minute) -> {
                Log.i("",""+hourOfDay+":"+minute);
                if (isStart) {
                    btnEditStartTime.setText(String.format("%02d:%02d",hourOfDay,minute));
                } else {
                    btnEditEndTime.setText(String.format("%02d:%02d",hourOfDay,minute));
                }
        };

        btnEditStartTime.setOnClickListener(v -> {
            DialogFragment newFragment = new TimePickerDialogFragment(timePickerListener);
            newFragment.show(getParentFragmentManager(),"timePicker");
            isStart = true;
        });

        btnEditEndTime.setOnClickListener(v -> {
            DialogFragment newFragment = new TimePickerDialogFragment(timePickerListener);
            newFragment.show(getParentFragmentManager(),"timePicker");
            isStart = false;
        });

        btnEditSubmit.setOnClickListener(v -> {

            String day = selectedDate.substring(0, 4) + "-" + selectedDate.substring(4, 6) + "-" + selectedDate.substring(6, 8);
            String startTime = day + " " + btnEditStartTime.getText().toString() + ":00";
            String endTime = day + " " + btnEditEndTime.getText().toString() + ":00";
            Attendance attendance = new Attendance(userId,selectedDate,startTime,endTime,"on",attendanceClass,"none"," ", "on", "", userId,
                    "app", "", userId, "app");
            String result = attendance.validate();
            if (result.equals("OK")) {
                showLoading();
                if (shouldInsert) {
                    mainViewModel.saveAttendance(attendance, token);
                } else {
                    mainViewModel.updateAttendance(attendance, token);
                }
            } else {
                Toasty.error(Objects.requireNonNull(getActivity()).getApplicationContext(),result,Toast.LENGTH_LONG).show();
            }

            dialogIsOn = false;
        });

        btnEditDialogClose.setOnClickListener(v -> {
            dialogIsOn = false;
            editDialog.dismiss();
        });

        mainViewModel.getError().observe(getViewLifecycleOwner(),errorMsg -> {
            if (errorMsg != null) {
                hideLoading();
                Toasty.error(getActivity().getApplicationContext(),errorMsg,Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setSubmitDate(String date,String s,String e) {
        tvSubmitDay.setText(date.substring(0, 4) + " / " + date.substring(4, 6) + " / " + date.substring(6, 8));
        btnEditStartTime.setText(s);
        btnEditEndTime.setText(e);
    }


    @Override
    public void resetAttendance(String date, String startTime, String finishTime, String workType, int workStatus) {
        this.date = date;
        this.startTime = startTime;
        this.finishTime = finishTime;
        this.workStatus = workStatus;

        //tvDay.setText(date);
        tvStartTime.setText(startTime);
        tvEndTime.setText(finishTime);
        tvWorkStatus.setText(workType);
    }

    @Override
    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
        bgApiProcess.setVisibility(View.VISIBLE);
        submitProgressBar.setVisibility(View.VISIBLE);
        submitBgApiProcess.setVisibility(View.VISIBLE);
        submitRootView.setClickable(false);
    }

    @Override
    public void hideLoading() {
        progressBar.setVisibility(View.INVISIBLE);
        bgApiProcess.setVisibility(View.INVISIBLE);
        submitProgressBar.setVisibility(View.INVISIBLE);
        submitBgApiProcess.setVisibility(View.INVISIBLE);
        submitRootView.setClickable(true);
    }


    @Override
    public void showErrorMsg(String message) {
        Toasty.success(Objects.requireNonNull(getActivity()).getApplicationContext(),message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {

    }
}
