package com.kamiken0215.work.Presentation.Init;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.kamiken0215.work.Presentation.Login.LoginActivity;
import com.kamiken0215.work.R;

import es.dmoral.toasty.Toasty;

public class InitActivity extends AppCompatActivity implements IInitView {

    InitViewModel initViewModel;
    private Runnable connect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init);

        ImageView imageView = findViewById(R.id.init_img_gif_soft);
        GlideDrawableImageViewTarget softGif = new GlideDrawableImageViewTarget(imageView);
        Glide.with(this).load(R.raw.soft_pentagon).into(softGif);

        //set
        //initViewModel = new InitViewModel(this);
        //initViewModel.isConnectedWeb(this.getApplication());
        //boolean isConnected = activeNetwork != null &&activeNetwork.isConnectedOrConnecting();

        final Handler handler = new Handler();

        connect = new Runnable() {
            int count = 0;
            ConnectivityManager cm = (ConnectivityManager)getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            boolean isConnected = activeNetwork != null &&
                    activeNetwork.isConnectedOrConnecting();
            public void run() {
                count ++;
                if (count == 10) {
                    Log.d("Connection","Not Connect");
                    showDialog();
                }
                if (isConnected) {
                    Log.d("Connection","Finish Connect");
                    onSuccessConnect();
                    return;
                }
                handler.removeCallbacks(connect);
                handler.postDelayed(connect, 2000);
            }
        };
        handler.postDelayed(connect, 2000);
    }

    @Override
    public void onSuccessConnect() {
        startActivity(new Intent(InitActivity.this, LoginActivity.class));
        finish();
    }

    @Override
    public void onFailureInit(String message) {
        System.out.println("failure *********************************");
        Toasty.error(this,message, Toast.LENGTH_LONG).show();
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("ネットワーク接続を確認してください")
                .setPositiveButton("OK", null)
                .show();
    }
}