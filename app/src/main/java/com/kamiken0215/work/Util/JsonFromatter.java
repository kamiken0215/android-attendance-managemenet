package com.kamiken0215.work.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonFromatter {

    //return start and finish time and work status
    public List<String> formatOneDayTimesAndStatus(List<?> response){

        List<String> workInfo = new ArrayList<>();

        String s = response.toString();

        try {

            JSONArray jsonArray = new JSONArray(s);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                workInfo.add(obj.getString("startTime"));
                workInfo.add(obj.getString("finishTime"));
                workInfo.add(String.valueOf(obj.getInt("workStatus")));
                System.out.println(obj);
            }
        }catch (JSONException e){
            System.out.println(e);
        }

        return workInfo;
    }

    public List<String> formatSearchAttendanceData(List<?> response){

        List<String> workInfo = new ArrayList<>();

        String s = response.toString();

        try {

            JSONArray jsonArray = new JSONArray(s);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                System.out.println(obj);
            }
        }catch (JSONException e){
            System.out.println(e);
        }

        return null;
    }
}
