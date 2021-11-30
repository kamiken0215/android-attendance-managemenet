package com.kamiken0215.work.Presentation.Graph;

import java.util.List;

public interface IDataPresenter {
    //send to usecase
    void toWeeklyData();
    //show graph
    void showGraph(List<Integer[]> yAxis, List<String[]> xAxis);
}
