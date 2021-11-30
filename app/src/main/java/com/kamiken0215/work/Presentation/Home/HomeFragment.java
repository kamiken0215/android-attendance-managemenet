package com.kamiken0215.work.Presentation.Home;

import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.kamiken0215.work.Domain.Entities.Attendance;
import com.kamiken0215.work.Presentation.Main.MainViewModel;
import com.kamiken0215.work.Presentation.Main.MainViewModelFactory;
import com.kamiken0215.work.R;
import com.kamiken0215.work.Util.DateDifferenceCalculator;
import com.kamiken0215.work.Util.OptionalWrapper;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import es.dmoral.toasty.Toasty;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.ColumnChartView;

import static android.os.Looper.getMainLooper;

public class HomeFragment extends Fragment implements IHomeView {

    private Handler handler;
    private SimpleDateFormat mSdf = new SimpleDateFormat("MM/dd  HH:mm:ss");

    private HomeViewModel homeViewModel;
    private MainViewModel mainViewModel;

    private ColumnChartView columnChartView;

    private TextView tvOverTime;
    private TextView tvPto;
    private TextView tvStartTime;
    private TextView tvEndTime;
    private TextView tvWorkStatus;
    private TextView tvTime;
    private TextView tvProgressTime;

    private ImageView imgUnder;

    private ProgressBar timeProgressBar;

    private Button btnStart;
    private Button btnFinish;

    private Spinner spinnerWork;

    private LinearLayout linearHideStartBtn;
    private LinearLayout linearHideFinishBtn;

    private ProgressBar progressBar;
    private LinearLayout bgApiProcess;

    private String attendanceClass;
    private String savedStartTime;
    private String finishTime;
    private String today;
    private String token;
    private String lastDate;

    private boolean isInit = true;

    private Typeface menlo;

    OptionalWrapper optionalWrapper = new OptionalWrapper();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    // Viewが生成し終わった時に呼ばれるメソッド
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        Bundle bundle = getArguments();
        assert bundle != null;
        String userId = bundle.getString("USER_ID");
        String token = bundle.getString("TOKEN");
        String paidHolidays = bundle.getString("PAID_HOLIDAYS");
        String restPaidHolidays = bundle.getString("REST_PAID_HOLIDAYS");


        LocalDateTime ldt = LocalDateTime.now();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd");
        today = ldt.format(dtf);

        columnChartView = view.findViewById(R.id.home_chart);

        Resources res = getResources();
        String spinnerItem[] = res.getStringArray(R.array.spinner_working);

        AssetManager assetManager = getResources().getAssets();
        //setup texttype
        Typeface avenir = Typeface.createFromAsset(assetManager,"AvenirLTStd-Black.otf");
        menlo = Typeface.createFromAsset(assetManager,"Menlo-Bold.ttf");

        tvOverTime = view.findViewById(R.id.home_tv_overtime);
        tvPto = view.findViewById(R.id.home_tv_pto);
        tvStartTime = view.findViewById(R.id.home_tv_start);
        tvEndTime = view.findViewById(R.id.home_tv_end);
        tvWorkStatus = view.findViewById(R.id.home_tv_work_status);
        tvTime = view.findViewById(R.id.home_tv_time);
        tvProgressTime = view.findViewById(R.id.home_tv_progress_time);

        tvOverTime.setTypeface(menlo);
        tvPto.setTypeface(menlo);
        tvStartTime.setTypeface(menlo);
        tvEndTime.setTypeface(menlo);
        tvWorkStatus.setTypeface(avenir);
        tvTime.setTypeface(menlo);
        tvProgressTime.setTypeface(menlo);

        imgUnder = view.findViewById(R.id.home_img_under);

        timeProgressBar = view.findViewById(R.id.home_progress_time);

        //不可視化するview
        linearHideStartBtn = view.findViewById(R.id.home_linear_start_button_hide);
        linearHideFinishBtn = view.findViewById(R.id.home_linear_finish_button_hide);

        progressBar = view.findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.INVISIBLE);
        bgApiProcess = view.findViewById(R.id.background_api_process);
        bgApiProcess.setVisibility(View.GONE);

        //setup spinner
        spinnerWork = view.findViewById(R.id.home_spinner);

        //setup Bottom Navigation
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(Objects.requireNonNull(getActivity()).getBaseContext()
                ,R.layout.custom_spinner
                ,spinnerItem);
        adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown);
        spinnerWork.setAdapter(adapter);

        btnStart = view.findViewById(R.id.home_btn_start);
        btnFinish = view.findViewById(R.id.home_btn_end);

        tvPto.setText(restPaidHolidays + "/" +  paidHolidays+"day");

        mainViewModel = new ViewModelProvider(this, new MainViewModelFactory(getActivity().getApplication())).get(MainViewModel.class);
        mainViewModel.getAttendances().observe(getViewLifecycleOwner(), attendance -> {

           if (attendance.size() > 0) {
                Log.d("FETCH","当月データ取得処理完了");
                columnChartView.setVisibility(View.VISIBLE);
                lastDate = attendance.get(attendance.size() - 1).getAttendanceDate();
                savedStartTime = attendance.get(attendance.size() - 1).getStartTime();
                makeProgress(attendance);
                makeGraph(attendance);
                makeAttendanceTimes(attendance);
            } else {
                //データがない
                columnChartView.setVisibility(View.INVISIBLE);
            }
        });

        //start
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocalDateTime ldt = LocalDateTime.now();
                DateTimeFormatter fmtDate = DateTimeFormatter.ofPattern("yyyyMMdd");
                String date = fmtDate.format(ldt);

                DateTimeFormatter fmtDateTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String dateTime = fmtDateTime.format(ldt);

                Attendance attendance = new Attendance(userId, date, dateTime, "", "on",
                        attendanceClass,"none"," ", "off", "", userId,
                        "app", "", userId, "app");
                mainViewModel.saveAttendance(attendance,userId,token);
            }
        });

        //finish
        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocalDateTime ldt = LocalDateTime.now();
                DateTimeFormatter fmtDate = DateTimeFormatter.ofPattern("yyyyMMdd");
                String date = fmtDate.format(ldt);
                DateTimeFormatter fmtDateTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String dateTime = fmtDateTime.format(ldt);

                Attendance attendance = new Attendance(userId, lastDate, savedStartTime, dateTime, "off",
                        attendanceClass,"none", " ", "on", "", userId,
                        "app", "", userId, "app");
                mainViewModel.updateAttendance(attendance,userId,token);
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
                attendanceClass = String.valueOf((int)id + 1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        handler = new Handler(getMainLooper());
        Timer timer = new Timer();

        // 一秒ごとに定期的に実行します。
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(() -> {
                    Calendar calendar = Calendar.getInstance();
                    String nowDate = mSdf.format(calendar.getTime());
                    // 時刻表示をするTextView
                    tvTime.setText(nowDate);
                });}
        },0,1000);


        mainViewModel.getError().observe(getViewLifecycleOwner(),errorMsg -> {
            if (errorMsg != null) {
                hideLoading();
                Toasty.error(getActivity().getApplicationContext(),errorMsg,Toast.LENGTH_LONG).show();
            }
        });

        //Shared preferences
        loadData(userId);


    }

    //このフラグメントに切り替わるたびに当月データを取得する
    private void loadData(String userId){
        //mainViewModel.fetchFromLocalDatabase(userId);
        mainViewModel.init(userId);
    }

    @Override
    public void enableStartButton() {
        btnStart.setClickable(true);
    }

    @Override
    public void disableStartButton() {
        btnStart.setClickable(false);
    }

    @Override
    public void showOverlayStartButton() {
        linearHideStartBtn.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideOverlayStartButton() {
        linearHideStartBtn.setVisibility(View.INVISIBLE);
    }

    @Override
    public void enableFinishButton() {
        btnFinish.setClickable(true);
    }

    @Override
    public void disableFinishButton(String message) {
        btnFinish.setClickable(false);
        Toasty.success(Objects.requireNonNull(getActivity()).getApplicationContext(),message, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void showOverlayFinishButton() {
        linearHideFinishBtn.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideOverlayFinishButton() {
        linearHideFinishBtn.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
        bgApiProcess.setVisibility(View.VISIBLE);

    }

    @Override
    public void hideLoading() {
        progressBar.setVisibility(View.GONE);
        bgApiProcess.setVisibility(View.GONE);
    }

    @Override
    public void resetStartTime(String time) {
        //set start time got from db
        //startTime = time;
        tvStartTime.setText(time);
    }

    @Override
    public void resetFinishTime(String time) {
        finishTime = time;
        tvEndTime.setText(time);
    }

    @Override
    public void fixWorkStatus(int workStatus) {
        //退勤時用workStatusをセット
        //this.workStatus = workStatus;
    }

    @Override
    public void disableSpinner(String workingType) {
        spinnerWork.setVisibility(View.INVISIBLE);
        imgUnder.setVisibility(View.INVISIBLE);
        tvWorkStatus.setVisibility(View.VISIBLE);
        tvWorkStatus.setText(workingType);
        blinkText(tvWorkStatus,1000,500);
    }

    @Override
    public void enableSpinner() {
        spinnerWork.setVisibility(View.VISIBLE);
        imgUnder.setVisibility(View.VISIBLE);
        tvWorkStatus.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showErrorMsg(String message){
        Toasty.success(getActivity().getApplicationContext(),message, Toast.LENGTH_SHORT).show();
    }

    private void blinkText(TextView txtView, long duration, long offset){
        Animation anm = new AlphaAnimation(0.0f, 1.0f);
        anm.setDuration(duration);
        anm.setStartOffset(offset);
        anm.setRepeatMode(Animation.REVERSE);
        anm.setRepeatCount(Animation.INFINITE);
        txtView.startAnimation(anm);
    }

    private void makeAttendanceTimes(List<Attendance> attendances) {
        //IF   : 今日の日付データを取得できれば時刻表示
        //ELSE : デフォルトの--:--を表示
        //attendances : 当月の出勤データ
        //最も新しい出勤日(当日)のデータはattendances.size()-1で取得
        int todayDataIdx = attendances.size()-1;
        if (attendances.get(todayDataIdx).getAttendanceDate().equals(today)) {

            String startTime = optionalWrapper.getOptionalString(attendances.get(todayDataIdx).getStartTime()).orElse("--:--");
            String endTime = optionalWrapper.getOptionalString(attendances.get(todayDataIdx).getEndTime()).orElse("--:--");

            //IF   : closed_onがoffなら出勤中(退勤時間は存在しない) > 出勤時間があれば表示 > 退勤時間はデフォルト値　
            //ELSE : closed_onがonなら退勤済(出退勤時間が存在) > 出勤時間を表示 > 退勤時間があれば表示、なければデフォルト値
            if (attendances.get(todayDataIdx).getClosedOn().equals("off")) {

                btnStart.setEnabled(false);
                btnFinish.setEnabled(true);

                linearHideFinishBtn.setVisibility(View.INVISIBLE);
                tvEndTime.setText("--:--");
                //出勤時間が取得できればVISIBLE
                if (startTime.length() > 5) {
                    linearHideStartBtn.setVisibility(View.VISIBLE);
                    tvStartTime.setText(startTime.substring(11, 16));
                } else {
                    tvStartTime.setText(startTime);
                }
            } else {

                btnStart.setEnabled(false);
                btnFinish.setEnabled(false);

                linearHideStartBtn.setVisibility(View.VISIBLE);
                tvStartTime.setText(startTime.substring(11, 16));
                //退勤時間が取得できればVISIBLE
                if (endTime.length() > 5) {
                    linearHideFinishBtn.setVisibility(View.VISIBLE);
                    tvEndTime.setText(endTime.substring(11, 16));
                } else {
                    tvEndTime.setText(endTime);
                }
            }

            //スピナーを無効化
            spinnerWork.setEnabled(false);

        } else {

            //スピナーを有効化
            spinnerWork.setEnabled(true);

            linearHideStartBtn.setVisibility(View.INVISIBLE);
            linearHideFinishBtn.setVisibility(View.INVISIBLE);
            btnStart.setEnabled(true);
            btnFinish.setEnabled(true);
            tvStartTime.setText("--:--");
            tvEndTime.setText("--:--");
        }
    }

    private void makeProgress(final List<Attendance> attendances) {

        int regulatedWorkTime = 8;

        DateDifferenceCalculator ddc = new DateDifferenceCalculator();
        int workTime = 0;
        int overTime = 0;
        for (Attendance a : attendances) {
            String start = optionalWrapper.getOptionalString(a.getStartTime()).orElse("0");
            String end = optionalWrapper.getOptionalString(a.getEndTime()).orElse("0");
            if (start.length() > 1 && end.length() > 1) {
                int t = (int) ddc.diffCalc(start, end);
                workTime += t;
                if (t > regulatedWorkTime) {
                    overTime += t-regulatedWorkTime;
                }
            }
        }

        tvOverTime.setText(overTime + "h/45h");

        Drawable progressTimeDrawable = timeProgressBar.getProgressDrawable().getCurrent();
        if (160 < workTime && 200 >= workTime) {
            progressTimeDrawable.setColorFilter(Color.parseColor("#FF8900"),android.graphics.PorterDuff.Mode.MULTIPLY);
        } else if (workTime > 200) {
            progressTimeDrawable.setColorFilter(Color.parseColor("#FF0084"),android.graphics.PorterDuff.Mode.MULTIPLY);
        } else {
            progressTimeDrawable.setColorFilter(Color.parseColor("#BEF4FF"),android.graphics.PorterDuff.Mode.MULTIPLY);
        }
        timeProgressBar.setProgress(workTime);
        tvProgressTime.setText(workTime+"h/200h");
    }

    private void makeGraph(List<Attendance> attendances) {

        //横軸
        List<String> axisData = new ArrayList<>();
        //縦軸
        List<Integer> yAxisData = new ArrayList<>();


        DateDifferenceCalculator ddc = new DateDifferenceCalculator();
        int workTime = 0;
        for (Attendance a : attendances) {
            //日付をセット
            axisData.add(formatDate(a.getAttendanceDate()));
            //勤務時間をセット
            String start = optionalWrapper.getOptionalString(a.getStartTime()).orElse("0");
            String end = optionalWrapper.getOptionalString(a.getEndTime()).orElse("0");
            if (start.length() > 1 && end.length() > 1) {
                workTime = (int) ddc.diffCalc(start, end);
            }
            yAxisData.add(workTime);
        }

        List<PointValue> yAxisValues = new ArrayList<>();
        List<AxisValue>  axisValues = new ArrayList<>();

        //取得月の日付をセット
        for(int i = 0; i < axisData.size(); i++){
            axisValues.add(i, new AxisValue(i).setLabel(axisData.get(i)));
        }

        //取得データをセット
        for (int i = 0; i < yAxisData.size(); i++){
            yAxisValues.add(new PointValue(i, yAxisData.get(i)));
        }

        List<Column> columnList  = new ArrayList<>();
        List<SubcolumnValue> subColumnValueList;

        for (int i = 0; i < yAxisData.size(); ++i) {
            subColumnValueList = new ArrayList<>();
            if(yAxisData.get(i) <= 9){
                subColumnValueList.add(new SubcolumnValue((float) yAxisData.get(i), Color.parseColor("#A8A8A8")));
            }else{
                subColumnValueList.add(new SubcolumnValue((float) yAxisData.get(i), Color.parseColor("#DB4503")));
            }
            axisValues.add(new AxisValue(i).setLabel(axisData.get(i)));
            Column column = new Column(subColumnValueList);
            column.setHasLabels(true);
            columnList.add(column);
        }

        ColumnChartData columnChartData = new ColumnChartData(columnList);

        Axis axisX = new Axis(axisValues);
        Axis axisY = new Axis().setHasLines(true);
        axisX.setName("- WORK TIME (h/day) -");
        //axisY.setName("time");
        axisX.setTextSize(10);
        axisX.setTypeface(menlo);
        columnChartData.setAxisXBottom(axisX);
        columnChartData.setAxisYLeft(axisY);
        columnChartData.setValueLabelBackgroundAuto(false);
        columnChartData.setValueLabelTextSize(12);
        columnChartData.setBaseValue(0f);
        columnChartData.setValueLabelsTextColor(Color.parseColor("#FFFFFF"));
        columnChartData.setValueLabelBackgroundColor(Color.parseColor("#00000000"));
        columnChartData.setValueLabelTypeface(menlo);

        //横軸の最大表示項目数を設定し、はみ出した分はスクロールする
        Viewport v = new Viewport(columnChartView.getMaximumViewport());
        v.left = 0;
        v.right = 10;
        columnChartView.setCurrentViewport(v);

        columnChartView.setZoomEnabled(true);
        columnChartView.setColumnChartData(columnChartData);
    }

    //yyyyMMddを不要な0を抜いたMM/ddに変換
    private String formatDate(String yyyyMMdd) {
        StringBuffer sb = new StringBuffer(yyyyMMdd);
        sb.delete(0,4);
        sb.insert(2,"/");
        if (String.valueOf(sb.charAt(0)).equals("0")) {
            sb.deleteCharAt(0);
        }
        if (String.valueOf(sb.charAt(sb.length()-2)).equals("0")) {
            sb.deleteCharAt(sb.length()-2);
        }
        return sb.toString();
    }

}
