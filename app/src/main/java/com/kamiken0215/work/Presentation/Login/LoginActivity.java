package com.kamiken0215.work.Presentation.Login;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.kamiken0215.work.Data.Api.RetrofitSingleton;
import com.kamiken0215.work.Data.DefineApi;
import com.kamiken0215.work.Data.Repository.AttendanceRepository;
import com.kamiken0215.work.Data.Repository.UserRepository;
import com.kamiken0215.work.Domain.Entities.User;
import com.kamiken0215.work.Domain.Repository.IAttendanceRepository;
import com.kamiken0215.work.Domain.Repository.IUserRepository;
import com.kamiken0215.work.Presentation.Main.MainActivity;
import com.kamiken0215.work.R;
import com.google.android.material.textfield.TextInputLayout;


import es.dmoral.toasty.Toasty;

public class LoginActivity extends AppCompatActivity implements ILoginView {

    EditText edtUserId;
    EditText edtPassword;
    ImageView imgPasswordVisible;
    TextInputLayout tiPassword;
    Drawable resizedIcClosedKey;
    Drawable resizedIcOpenedKey;
    LinearLayout linearChangePasswordVisibility;
    Button btnLogin;

    private ProgressBar progressBar;
    private LinearLayout bgApiProcess;

    private Boolean isOn = false;
    int cursorPos;

    private LoginViewModel loginViewModel;
    private ILoginPresenter loginPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //******ここをDIコンテナで追い出す
//        RetrofitSingleton retrofitSingleton = RetrofitSingleton.getRetrofitInstance();
//        UserRepository userRepository = new UserRepository(retrofitSingleton);
//        LoginUseCase loginUseCase = new LoginUseCase(userRepository);
//        loginPresenter = new LoginPresenter(this,loginUseCase);
        //*****

        //set texttype
        Typeface avenir = Typeface.createFromAsset(getAssets(),"AvenirLTStd-Black.otf");
        Typeface menlo = Typeface.createFromAsset(getAssets(), "Menlo-Bold.ttf");

        TextView tvLoginTitleLeft = findViewById(R.id.login_tv_title_left);
        tvLoginTitleLeft.setTypeface(avenir);

        TextView tvLoginTitleRight = findViewById(R.id.login_tv_title_right);
        tvLoginTitleRight.setTypeface(avenir);

        TextView tvLoginForgot = findViewById(R.id.login_tv_forgot);
        tvLoginForgot.setTypeface(menlo);

        //パスワード入力フィールドのアイコン設定
        tiPassword = findViewById(R.id.login_ti_password);

        //縦横24dp(タブレット対応)
        int height = (int)(this.getResources().getDimension(R.dimen.ic_size_24dp));
        int width = (int)(this.getResources().getDimension(R.dimen.ic_size_24dp));

        //アイコン取得
        Drawable icClosedKey = getResources().getDrawable(R.drawable.ic_closedkey_white);
        Drawable icOpenedKey = getResources().getDrawable(R.drawable.ic_openedkey_orange);

        //取得したアイコンをビットマップに変換
        Bitmap bitmapClosedKey = ((BitmapDrawable) icClosedKey).getBitmap();
        Bitmap bitmapOpenedKey = ((BitmapDrawable) icOpenedKey).getBitmap();

        //アイコンにサイズを適用
        resizedIcClosedKey = new BitmapDrawable(getResources(),
                Bitmap.createScaledBitmap(bitmapClosedKey
                        ,width
                        ,height
                        , true));
        resizedIcOpenedKey = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmapOpenedKey, width, height, true));

        //パスワード入力フィールドにリサイズしたアイコンをセット
        imgPasswordVisible = findViewById(R.id.login_img_password_visible);
        imgPasswordVisible.setImageDrawable(resizedIcClosedKey);

        linearChangePasswordVisibility = findViewById(R.id.login_linear_change_password_visibility);
        linearChangePasswordVisibility.setClickable(true);

        btnLogin = findViewById(R.id.login_btn_do_login);
        //btnLogin.setTypeface(avenir);

        edtUserId = findViewById(R.id.login_edt_userId);
        edtPassword = findViewById(R.id.login_edt_password);

        //set loading
        progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.INVISIBLE);
        bgApiProcess = findViewById(R.id.background_api_process);
        bgApiProcess.setVisibility(View.GONE);

        RetrofitSingleton retrofitSingleton = RetrofitSingleton.getRetrofitInstance();
        IUserRepository userRepository = new UserRepository(getApplication(),retrofitSingleton);
        IAttendanceRepository attendanceRepository = new AttendanceRepository(getApplication(),retrofitSingleton);
        loginViewModel = new ViewModelProvider(this,new LoginViewModelFactory(this,userRepository,attendanceRepository)).get(LoginViewModel.class);
        loginViewModel.getUser().observe(this, user -> {
            hideLoading();
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
            intent.putExtra("USER_ID",user.getUserId());
            intent.putExtra("TOKEN",user.getToken());
            intent.putExtra("PAID_HOLIDAYS",user.getPaidHolidays());
            intent.putExtra("REST_PAID_HOLIDAYS",user.getRestPaidHolidays());
            intent.putExtra("FRAGMENT_NAME","NONE");
            startActivity(intent);
            finish();
        });

        //Event
        linearChangePasswordVisibility.setOnClickListener(v -> {
            imgPasswordVisible.setImageDrawable(null);
            cursorPos = edtPassword.getSelectionStart();
            isOn = !isOn;
            showPassword(isOn);
        });

        btnLogin.setOnClickListener(v -> {
            //design demo
            showLoading();
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            loginViewModel.login(edtUserId.getText().toString(),edtPassword.getText().toString());
        });

        tvLoginForgot.setOnClickListener(v -> {
            //loginPresenter.forgotPassword();
        });

        init();

    }

    private void init() {
        loginViewModel.cleanDatabase();
    }


    @Override
    public void onStarted() {
        //showLoading();
    }

    @Override
    public void onLoginSuccess(User user,String name) {
        hideLoading();
        //Toasty.success(this,"Hi! "+name+"!",Toast.LENGTH_SHORT).show();
        loginViewModel.save(user);
    }

    public void userFind(String name) {
        Toasty.success(this,"Hi! "+name+"!",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoginError(String message) {
        hideLoading();
        Toasty.error(this,message,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onSuccessSaveLocal(String userId) {
        loginViewModel.fetchUser(userId);

    }

    @Override
    public void onFailureSaveLocal() {

    }

    @Override
    public void onSuccessFetchLocal() {
        Toasty.success(this,"Hi!",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFailureFetchLocal() {

    }

    @Override
    public void resetPassword() {
        Intent browser = new Intent(Intent.ACTION_VIEW, Uri.parse(DefineApi.URI_AUTH));
        ResolveInfo defaultResInfo = getPackageManager().resolveActivity(browser, PackageManager.MATCH_DEFAULT_ONLY);
        if(defaultResInfo != null){
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(DefineApi.URI_AUTH));
            intent.setPackage(defaultResInfo.activityInfo.packageName);
            try{
                startActivity(intent);
            }catch (ActivityNotFoundException e){
                System.out.println(e);
            }
        }
    }

    private void showPassword(boolean isOn) {
        if (isOn) {
            imgPasswordVisible.setImageDrawable(resizedIcOpenedKey);
            edtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        } else {
            imgPasswordVisible.setImageDrawable(resizedIcClosedKey);
            edtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
        edtPassword.setSelection(cursorPos);
    }

    @Override
    public void showPassword() {
        imgPasswordVisible.setImageDrawable(resizedIcOpenedKey);
        edtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        edtPassword.setSelection(cursorPos);
    }

    @Override
    public void hidePassword() {
        imgPasswordVisible.setImageDrawable(resizedIcClosedKey);
        edtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        edtPassword.setSelection(cursorPos);
    }

    @Override
    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
        bgApiProcess.setVisibility(View.VISIBLE);
        //bgApiProcess.bringToFront();
    }

    @Override
    public void hideLoading() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        progressBar.setVisibility(ProgressBar.GONE);
        bgApiProcess.setVisibility(View.GONE);
    }

    //バックキーで戻ってきた際にDBをクリーン
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        init();
    }
}
