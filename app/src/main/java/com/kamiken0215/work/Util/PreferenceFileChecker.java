package com.kamiken0215.work.Util;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceFileChecker {

    //check one day has already data
    public Boolean hasValue(Context context, String fileName, String key){
        boolean hasValue = false;
        SharedPreferences pref = context.getSharedPreferences(fileName,Context.MODE_PRIVATE);
        String value = pref.getString(key,null);
        if(value != null) {
            hasValue = true;
        }
        return hasValue;
    }

    //Check both of start and finish time in a day
    public boolean hasStratAndFinish(Context context,String fileName,String key){
        return false;
    }
}
