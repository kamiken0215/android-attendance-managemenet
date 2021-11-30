package com.kamiken0215.work.Presentation.Graph;

import android.content.res.AssetManager;
import android.graphics.Color;
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

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.view.ColumnChartView;


public class DataHoursFragment extends Fragment{

    private List<String[]> axisData;
    private List<Integer[]> yAxis;

    private ColumnChartView columnChartView1;
    private ColumnChartView columnChartView2;
    private ColumnChartView columnChartView3;
    private ColumnChartView columnChartView4;
    private ColumnChartView columnChartView5;

    private TextView tvHours;
    private TextView tvDays;
    private TextView tvOverTimes;
    private TextView tvTotalHours;
    private TextView tvTotalDays;
    private TextView tvTotalOverTimes;

    private Typeface marion;

    DataHoursFragment(List<String[]> axisData,List<Integer[]> yAxis){
        this.axisData = axisData;
        this.yAxis = yAxis;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        // 先ほどのレイアウトをここでViewとして作成します
        return inflater.inflate(R.layout.fragment_data, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        AssetManager assetManager = getResources().getAssets();
        //setup texttype
        Typeface avenir = Typeface.createFromAsset(assetManager,"AvenirLTStd-Black.otf");
        Typeface menlo = Typeface.createFromAsset(assetManager,"Menlo-Bold.ttf");
        Typeface orbitron = Typeface.createFromAsset(assetManager,"Orbitron-Bold.ttf");
        marion = Typeface.createFromAsset(assetManager,"Marion Bold.ttf");

        tvTotalHours = view.findViewById(R.id.hour_data_tv_total_hours);
        tvTotalDays = view.findViewById(R.id.hour_data_tv_total_days);
        tvTotalOverTimes = view.findViewById(R.id.hour_data_tv_total_over_times);
        tvDays = view.findViewById(R.id.hour_data_tv_days);
        tvHours = view.findViewById(R.id.hour_data_tv_hours);
        tvOverTimes = view.findViewById(R.id.hour_data_tv_over_times);

        tvTotalHours.setTypeface(marion);
        tvTotalDays.setTypeface(marion);
        tvTotalOverTimes.setTypeface(marion);
        tvHours.setTypeface(orbitron);
        tvDays.setTypeface(orbitron);
        tvOverTimes.setTypeface(orbitron);

        columnChartView1 = view.findViewById(R.id.chart);
        columnChartView2 = view.findViewById(R.id.chart2);
        columnChartView3 = view.findViewById(R.id.chart3);
        columnChartView4 = view.findViewById(R.id.chart4);
        columnChartView5 = view.findViewById(R.id.chart5);

        //get data
        setupLineChatView(axisData,yAxis);
    }

    private void setupLineChatView(List<String[]> axisData,List<Integer[]> yAxis) {
        int numOfGraph = yAxis.size();
        int graphNum = 0;
        for (int i = 0; i < numOfGraph; i++){
            createLineChatView(axisData.get(i),yAxis.get(i),graphNum);
            graphNum = graphNum + 1;
        }
    }

    private void createLineChatView(String[] axisData, Integer[] yAxisData, int graphNum){

        List<Column> columnList  = new ArrayList<>();
        List<SubcolumnValue> subColumnValueList;
        List<AxisValue> axisValues = new ArrayList<>();
        for (int i = 0; i < yAxisData.length; ++i) {
            subColumnValueList = new ArrayList<>();
            if(yAxisData[i] <= 9){
                subColumnValueList.add(new SubcolumnValue((float) yAxisData[i], Color.parseColor("#A8A8A8")));
            }else{
                subColumnValueList.add(new SubcolumnValue((float) yAxisData[i], Color.parseColor("#FF9315")));
            }
            axisValues.add(new AxisValue(i).setLabel(axisData[i]));
            Column column = new Column(subColumnValueList);
            column.setHasLabels(true);
            columnList.add(column);
        }

        ColumnChartData columnChartData = new ColumnChartData(columnList);

        Axis axisX = new Axis(axisValues);
        Axis axisY = new Axis().setHasLines(true);
        axisX.setName("day");
        axisY.setName("time");
        columnChartData.setAxisXBottom(axisX);
        columnChartData.setAxisYLeft(axisY);
        columnChartData.setValueLabelBackgroundAuto(false);
        columnChartData.setValueLabelTextSize(14);
        columnChartData.setValueLabelsTextColor(Color.parseColor("#FFFFFF"));
        columnChartData.setValueLabelBackgroundColor(Color.parseColor("#00000000"));

        switch (graphNum){
            case 0:
                columnChartView1.setZoomEnabled(false);
                columnChartView1.setColumnChartData(columnChartData);
            case 1:
                columnChartView2.setZoomEnabled(false);
                columnChartView2.setColumnChartData(columnChartData);
            case 2:
                columnChartView3.setZoomEnabled(false);
                columnChartView3.setColumnChartData(columnChartData);
            case 3:
                columnChartView4.setZoomEnabled(false);
                columnChartView4.setColumnChartData(columnChartData);
            case 4:
                columnChartView5.setZoomEnabled(false);
                columnChartView5.setColumnChartData(columnChartData);
        }

    }

}
