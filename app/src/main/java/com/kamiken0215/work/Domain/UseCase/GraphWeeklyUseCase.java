package com.kamiken0215.work.Domain.UseCase;

import com.kamiken0215.work.Domain.Entities.AttendanceDataModel;
import com.kamiken0215.work.Data.Repository.AttendanceDataRepository;
import com.kamiken0215.work.Presentation.Graph.IGraphPresenter;

import java.util.List;

public class GraphWeeklyUseCase implements IGraphWeeklyUseCase {

    //データ加工処理
    //プレゼンターに返す
    private IGraphPresenter graphPresenter;

    public GraphWeeklyUseCase(IGraphPresenter graphPresenter) {
        this.graphPresenter = graphPresenter;
    }

    @Override
    public void searchAttendanceData(int userId, String date) {
        AttendanceDataRepository attendanceDataRepository = new AttendanceDataRepository(this);
        attendanceDataRepository.fetchAttendanceData(userId,date);
    }

    @Override
    public void onFinished(List<AttendanceDataModel> attendanceData) {
        //データ加工
        long[] data = new long[0];
        //graphPresenter.showGraph(data);
    }

    @Override
    public void onFailure(Throwable t) {

    }


    private void formatWeeklyData(int userId, String Date){


    }
}
