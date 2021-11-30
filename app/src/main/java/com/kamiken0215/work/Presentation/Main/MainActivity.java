package com.kamiken0215.work.Presentation.Main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.kamiken0215.work.Presentation.Calendar.CalendarFragment;
import com.kamiken0215.work.Presentation.Graph.GraphFragment;
import com.kamiken0215.work.Presentation.Home.HomeFragment;
import com.kamiken0215.work.Presentation.Login.LoginActivity;
import com.kamiken0215.work.R;
import com.kamiken0215.work.Presentation.Setting.SettingFragment;
import com.kamiken0215.work.Util.OptionalWrapper;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity{

    private static final int CREATE_FILE = 1002;

    int startingPosition = 1;
    private String userId;
    private Bundle bundle;

    private MainViewModel mainViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        OptionalWrapper optionalWrapper = new OptionalWrapper();

        Intent intent = getIntent();
        String userId = intent.getStringExtra("USER_ID");
        String token = intent.getStringExtra("TOKEN");
        String paidHolidays = intent.getStringExtra("PAID_HOLIDAYS");
        String restPaidHolidays = intent.getStringExtra("REST_PAID_HOLIDAYS");
        String fragmentName = optionalWrapper.getOptionalString(intent.getStringExtra("FRAGMENT_NAME")).orElse("NONE");

        bundle = new Bundle();
        bundle.putString("USER_ID",userId);
        bundle.putString("TOKEN",token);
        bundle.putString("PAID_HOLIDAYS",paidHolidays);
        bundle.putString("REST_PAID_HOLIDAYS",restPaidHolidays);

        mainViewModel = new ViewModelProvider(this, new MainViewModelFactory(getApplication())).get(MainViewModel.class);
        mainViewModel.getAttendances().observe(this, attendance -> {
            //Call fragment
            HomeFragment homeFragment = new HomeFragment();
            homeFragment.setArguments(bundle);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.fragment_container,homeFragment);
            transaction.commit();
        });

        //縦横24dp(タブレット対応)
        int height = (int)(this.getResources().getDimension(R.dimen.ic_size_24dp));
        int width = (int)(this.getResources().getDimension(R.dimen.ic_size_24dp));
        setupBottomNavigation(height,width);

        if (fragmentName.equals("NONE")) {
            mainViewModel.init(userId);
        } else {
            Fragment fragment = null;
            if (fragmentName.equals("HOME")) {
                fragment = new HomeFragment();
                startingPosition = 1;
            } else if (fragmentName.equals("CALENDAR")) {
                fragment = new CalendarFragment();
                startingPosition = 2;
            }
            if (fragment != null) {
                fragment.setArguments(bundle);
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.add(R.id.fragment_container,fragment);
                transaction.commit();
            }
        }
    }

    private void setupBottomNavigation(int height, int width){
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        //bottomNavigationView.setItemIconSize(height);
        //Menu bottomNavigationViewMenu = bottomNavigationView.getMenu();
//        bottomNavigationViewMenu.findItem(R.id.nav_logout).setChecked(false);
//        bottomNavigationViewMenu.findItem(R.id.nav_data).setChecked(false);
//        bottomNavigationViewMenu.findItem(R.id.nav_menu).setChecked(true);
//        bottomNavigationViewMenu.findItem(R.id.nav_edit).setChecked(false);
//        bottomNavigationViewMenu.findItem(R.id.nav_setting).setChecked(false);
        //bottomNavigationViewMenu.findItem(R.id.nav_menu).setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                Fragment fragment = null;
                int newPosition = 0;

                switch (menuItem.getItemId()){
                    case R.id.nav_menu:
                        fragment = new HomeFragment();
                        newPosition = 1;
                        break;

                    case R.id.nav_edit:
                        fragment = new CalendarFragment();
                        newPosition = 2;
                        break;

                    case R.id.nav_data:
                        newPosition = 3;
                        fragment = new GraphFragment();
                        break;

                    case R.id.nav_setting:
                        fragment = new SettingFragment();
                        newPosition = 4;
                        break;

                    case R.id.nav_logout:
                        //fragment = new CalendarFragment();
                        newPosition = 5;
                        break;
//                        startActivity(new Intent(MenuActivity.this,LoginActivity.class));
//                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
//                        finish();
                }
                return loadFragment(fragment,newPosition);
            }
        });
    }

    private boolean loadFragment(Fragment fragment, int newPosition) {

        if(fragment != null) {
            if(startingPosition > newPosition) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setCustomAnimations(
                        R.anim.slide_in_left
                        ,R.anim.slide_out_right);
                transaction.replace(R.id.fragment_container, fragment);
                transaction.commit();
            }
            if(startingPosition < newPosition) {
                FragmentTransaction transaction = getSupportFragmentManager()
                        .beginTransaction();
                transaction.setCustomAnimations(
                        R.anim.slide_in_right
                        ,R.anim.slide_out_left);
                transaction.replace(R.id.fragment_container, fragment);
                transaction.commit();
            }
            fragment.setArguments(bundle);
            startingPosition = newPosition;
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        // do something on back.
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
        finish();
    }

}
