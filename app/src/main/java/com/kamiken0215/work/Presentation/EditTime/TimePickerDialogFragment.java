package com.kamiken0215.work.Presentation.EditTime;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TimePicker;

import com.kamiken0215.work.R;

import java.util.Calendar;

public class TimePickerDialogFragment extends androidx.fragment.app.DialogFragment implements TimePickerDialog.OnTimeSetListener {

    private Button btnStart;
    private Button btnFinish;
    private Boolean isStart;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        isStart = getArguments().getBoolean("isStart");
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), this, hour, minute, true);

        return timePickerDialog;
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        //時刻が選択されたときの処理

        String hour;
        String minutes;

        //padding zero
        if(hourOfDay < 10){
            hour = "0";
            hour += Integer.toString(hourOfDay);
        }else {
            hour = Integer.toString(hourOfDay);
        }

        if(minute < 10){
            minutes = "0";
            minutes += Integer.toString(minute);
        }else{
            minutes = Integer.toString(minute);
        }

        if(isStart) {
            btnStart = getActivity().findViewById(R.id.edit_btn_start);
            String time = hour;
            time += ":";
            time += minutes;
            btnStart.setText(time);
        }else {
            btnFinish = getActivity().findViewById(R.id.edit_btn_finish);
            String time = hour;
            time += ":";
            time += minutes;
            btnFinish.setText(time);
        }

    }

}