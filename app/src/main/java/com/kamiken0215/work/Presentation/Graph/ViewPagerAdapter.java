package com.kamiken0215.work.Presentation.Graph;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.kamiken0215.work.Presentation.Calendar.CalendarFragment;
import com.kamiken0215.work.Presentation.Home.HomeFragment;
import com.kamiken0215.work.Presentation.Setting.SettingFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    //private final List<Fragment> fragmentList = new ArrayList<>();
    private static final int PAGE_NUM = 5;

    public ViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 1:
                return new HomeFragment();

            case 3:
                return new SettingFragment();

            case 5:
                return new CalendarFragment();
        }
        return null;
    }

    public void addFragment(Fragment fragment){

    }

    @Override
    public int getCount() {
        return PAGE_NUM;
    }
}
