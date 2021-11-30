package com.kamiken0215.work.Presentation.Init;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.util.Log;

import androidx.lifecycle.ViewModel;

public class InitViewModel extends ViewModel {

    IInitView initView;

    public InitViewModel(IInitView initView) {
        this.initView = initView;
    }

    public void isConnectedWeb(Application application) {

        ConnectivityManager cm =
                (ConnectivityManager)application.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        final Handler handler = new Handler();
        final Runnable r = new Runnable() {
            int count = 0;
            @Override
            public void run() {
                count ++;
                if (count > 5) {
                    Log.d("Connection","Not Connect");
                    initView.onFailureInit("ネットワークに接続されていません");
                    return;
                }
                // UIスレッド
                boolean isConnected = activeNetwork != null &&
                        activeNetwork.isConnectedOrConnecting();
                if (isConnected) {
                    Log.d("Connection","Finish Connect");
                    initView.onSuccessConnect();
                }
            }
        };
        handler.postDelayed(r, 1000);

//        if (isConnected) {
//            Log.d("Connection","Finish Connect");
//            initView.onSuccessConnect();
//        } else {
//            Log.d("Connection","Not Connect");
//            initView.onFailureInit("ネットワークに接続されていません");
//        }
    }
}
