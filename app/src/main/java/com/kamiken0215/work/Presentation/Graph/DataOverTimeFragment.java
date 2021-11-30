package com.kamiken0215.work.Presentation.Graph;

import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.kamiken0215.work.R;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.view.LineChartView;

public class DataOverTimeFragment extends Fragment {

    private LineChartView lineChartView1;
    private LineChartView lineChartView2;
    private LineChartView lineChartView3;

    private TextView tvHours;
    private TextView tvDays;
    private TextView tvOverTimes;
    private TextView tvTotalHours;
    private TextView tvTotalDays;
    private TextView tvTotalOverTimes;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        // 先ほどのレイアウトをここでViewとして作成します
        return inflater.inflate(R.layout.fragment_overtime_data, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        AssetManager assetManager = getResources().getAssets();
        //setup texttype
        Typeface avenir = Typeface.createFromAsset(assetManager,"AvenirLTStd-Black.otf");
        Typeface menlo = Typeface.createFromAsset(assetManager,"Menlo-Bold.ttf");
        Typeface orbitron = Typeface.createFromAsset(assetManager,"Orbitron-Bold.ttf");
        Typeface marion = Typeface.createFromAsset(assetManager,"Marion Bold.ttf");

        tvTotalHours = view.findViewById(R.id.overtime_data_tv_total_hours);
        tvTotalDays = view.findViewById(R.id.overtime_data_tv_total_days);
        tvTotalOverTimes = view.findViewById(R.id.overtime_data_tv_total_overtimes);
        tvDays = view.findViewById(R.id.overtime_data_tv_days);
        tvHours = view.findViewById(R.id.overtime_data_tv_hours);
        tvOverTimes = view.findViewById(R.id.overtime_data_tv_overtimes);

        tvTotalHours.setTypeface(marion);
        tvTotalDays.setTypeface(marion);
        tvTotalOverTimes.setTypeface(marion);
        tvHours.setTypeface(orbitron);
        tvDays.setTypeface(orbitron);
        tvOverTimes.setTypeface(orbitron);

        lineChartView1 = view.findViewById(R.id.chart);
        lineChartView2 = view.findViewById(R.id.chart2);
        lineChartView3 = view.findViewById(R.id.chart3);

        setupLineChatView();

    }

    private void setupLineChatView(){

        String[] axisData = {"Jan", "Feb", "Mar", "Apr", "May", "June", "July", "Aug", "Sept",
                "Oct", "Nov", "Dec"};
        int[] yAxisData = {50, 20, 15, 30, 20, 60, 15, 40, 45, 10, 90, 18};
        List yAxisValues = new ArrayList();
        List axisValues = new ArrayList();
        Line line = new Line(yAxisValues);
        for(int i = 0; i < axisData.length; i++){
            axisValues.add(i, new AxisValue(i).setLabel(axisData[i]));
        }

        for (int i = 0; i < yAxisData.length; i++){
            yAxisValues.add(new PointValue(i, yAxisData[i]));
        }

        List lines = new ArrayList();
        lines.add(line);
        LineChartData data = new LineChartData();
        data.setLines(lines);
        lineChartView1.setLineChartData(data);
        lineChartView2.setLineChartData(data);
        lineChartView3.setLineChartData(data);
    }
}
