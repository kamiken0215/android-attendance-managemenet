package com.kamiken0215.work.Presentation.Graph;

import java.util.List;

public class DataPresenter implements IDataPresenter{

    private IGraphPresenter graphPresenter;
    private IDataView graphAttendanceDataView;

    DataPresenter(IGraphPresenter graphPresenter, IDataView graphAttendanceDataView) {
        this.graphPresenter = graphPresenter;
        this.graphAttendanceDataView = graphAttendanceDataView;
    }

    @Override
    public void toWeeklyData() {

    }

    @Override
    public void showGraph(List<Integer[]> yAxis, List<String[]> xAxis) {
        graphAttendanceDataView.showHoursDataView(yAxis,xAxis);
    }
}
