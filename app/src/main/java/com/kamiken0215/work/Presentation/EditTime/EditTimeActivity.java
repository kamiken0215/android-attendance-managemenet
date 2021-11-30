package com.kamiken0215.work.Presentation.EditTime;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.kamiken0215.work.Presentation.Main.MainActivity;
import com.kamiken0215.work.R;
import com.kamiken0215.work.Util.DateFormatter;

import es.dmoral.toasty.Toasty;

public class EditTimeActivity extends AppCompatActivity implements IEditTimeView {

    Bundle extras;
    //Bundle bundle;

    private String weekDay;

    private int userId;
    private String date;
    private String startTime;
    private String finishTime;
    int workStatus;

    private TextView tvDate;
    private TextView tvWeekDay;
    private Button btnStart;
    private Button btnFinish;
    private Button btnChange;
    private Button btnBack;

    private Spinner spinnerWork;

    private Boolean isStart;

    private ProgressBar progressBar;
    private LinearLayout bgApiProcess;

    private EditTimePresenter editTimePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_time);

        Intent intent = getIntent();
        //extras = intent.getExtras();
        userId = intent.getIntExtra("userId",0);
        date = intent.getStringExtra("date");
        startTime = intent.getStringExtra("startTime");
        finishTime = intent.getStringExtra("finishTime");
        workStatus = intent.getIntExtra("workingStatus",1);

        Resources res = getResources();
        String spinnerItem[] = res.getStringArray(R.array.spinner_working);

        //view
        tvDate = findViewById(R.id.edit_tv_date);
        tvWeekDay = findViewById(R.id.edit_tv_week);
        btnChange = findViewById(R.id.edit_btn_change);
        btnBack = findViewById(R.id.edit_btn_cancel);
        btnStart = findViewById(R.id.edit_btn_start);
        btnFinish = findViewById(R.id.edit_btn_finish);

        DateFormatter dateFormatter = new DateFormatter();
        weekDay = dateFormatter.getWeekDay(date);
        tvDate.setText(date);
        tvWeekDay.setText(weekDay);

        if(startTime != null) {
            btnStart.setText(startTime);
        }else {
            btnStart.setText("--:--");
        }

        if(finishTime != null){
            btnFinish.setText(finishTime);
        }else {
            btnFinish.setText("--:--");
        }

        //setup spinner
        spinnerWork = findViewById(R.id.edit_spinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,R.layout.custom_spinner
                ,spinnerItem);
        adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown);
        spinnerWork.setAdapter(adapter);


        //btnOut.setText(finishTime.substring(11,16));
        progressBar = findViewById(R.id.progress_bar);
        bgApiProcess = findViewById(R.id.background_api_process);

        //initFetchAttendanceData
        editTimePresenter = new EditTimePresenter(this);

        //setup listener
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isStart = true;
                showTimePicker(isStart);
            }
        });

        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isStart = false;
                showTimePicker(isStart);
            }
        });

        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTime = btnStart.getText().toString();
                finishTime = btnFinish.getText().toString();
                editTimePresenter.updateAttendanceData(userId,date,startTime,finishTime,workStatus);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditTimeActivity.this,MainActivity.class);
                intent.putExtra("userId",userId);
                startActivity(intent);
                finish();
            }
        });

        //spinner
        spinnerWork.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Spinner spinner = (Spinner) parent;
                String item = (String) spinner.getSelectedItem();
                System.out.println(item);
                System.out.println(id);
                workStatus = (int) id + 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @Override
    public void onEditSuccess(String message) {
        Toasty.success(this,message, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(EditTimeActivity.this,MainActivity.class);
        intent.putExtra("userId",userId);
        startActivity(intent);
        finish();
    }

    @Override
    public void onEditError(String message) {
        Toasty.error(this,message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoading() {
        progressBar.setVisibility(ProgressBar.VISIBLE);
        bgApiProcess.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        progressBar.setVisibility(ProgressBar.INVISIBLE);
        bgApiProcess.setVisibility(View.INVISIBLE);
    }

    @Override
    public void resetStartTime(String startTime) {

    }

    @Override
    public void resetFinishTime(String finishTime) {

    }

    @Override
    public void resetWorkingStatus(String workingStatus) {

    }

    @Override
    public void enableChangeButton() {

    }

    @Override
    public void disableChangeButton() {

    }

    private void showTimePicker(boolean isStart){
        //Call fragment
        Bundle bundle = new Bundle();
        bundle.putBoolean("isStart",isStart);
        TimePickerDialogFragment timePickerDialogFragment = new TimePickerDialogFragment();
        timePickerDialogFragment.setArguments(bundle);
        timePickerDialogFragment.show(getSupportFragmentManager(),"timePicker");
    }
}
