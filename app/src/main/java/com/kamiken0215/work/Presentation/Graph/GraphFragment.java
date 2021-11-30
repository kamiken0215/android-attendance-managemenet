package com.kamiken0215.work.Presentation.Graph;

import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.kamiken0215.work.R;
import com.google.android.material.tabs.TabLayout;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class GraphFragment extends Fragment implements IGraphView{

    private GraphPresenter graphPresenter;

    private TextView tvYear;
    private TextView tvMonth;

    private ProgressBar progressBar;
    private LinearLayout bgApiProcess;
    Handler handler = new Handler();
    Bundle extras = new Bundle();

    //private DataHoursFragment dataHoursFragment;

    private int userId;
    private String year;
    private String month;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    private int[] attendanceTimes;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view =  inflater.inflate(R.layout.fragment_graph, container, false);
        // Setting ViewPager for each Tabs
        viewPager = view.findViewById(R.id.viewpager);
        // Set Tabs inside Toolbar
        tabLayout = view.findViewById(R.id.result_tabs);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        AssetManager assetManager = getResources().getAssets();
        //setup texttype
        Typeface avenir = Typeface.createFromAsset(assetManager, "AvenirLTStd-Black.otf");
        Typeface menlo = Typeface.createFromAsset(assetManager, "Menlo-Bold.ttf");

        tvYear = view.findViewById(R.id.graph_tv_year);
        tvMonth = view.findViewById(R.id.graph_tv_month);

        tvYear.setTypeface(avenir);
        tvMonth.setTypeface(avenir);

        Button btnSearch = view.findViewById(R.id.graph_btn_search);

        progressBar = view.findViewById(R.id.progress_bar);
        bgApiProcess = view.findViewById(R.id.background_api_process);

        MaterialBetterSpinner yearMaterialBetterSpinner = view.findViewById(R.id.graph_spinner_year);
        MaterialBetterSpinner monthMaterialBetterSpinner = view.findViewById(R.id.graph_spinner_month);

        yearMaterialBetterSpinner.setTypeface(avenir);
        monthMaterialBetterSpinner.setTypeface(avenir);

        //ここもpresenterになげるし加工はGraphDataUseCase
        String[] years = {"2020", "2019", "2018"};
        String[] months = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};

        ArrayAdapter<String> yearAdapter = new ArrayAdapter<>(getActivity().getApplicationContext()
                , R.layout.custom_spinner
                , years);
        yearAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown);
        yearMaterialBetterSpinner.setAdapter(yearAdapter);

        ArrayAdapter<String> monthAdapter = new ArrayAdapter<>(getActivity().getApplicationContext()
                , R.layout.custom_spinner
                , months);
        yearAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown);
        monthMaterialBetterSpinner.setAdapter(monthAdapter);

        final Bundle bundle = getArguments();

        if (bundle.getInt("userId") != 0) {
            userId = bundle.getInt("userId");
            System.out.println(userId);
        }


        graphPresenter = new GraphPresenter(this, userId);

        yearMaterialBetterSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                year = adapterView.getItemAtPosition(position).toString();
                System.out.println(year);
            }
        });

        monthMaterialBetterSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                month = adapterView.getItemAtPosition(position).toString();
                System.out.println(month);
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //search data
                String date = year;
                if (month != null) {
                    date += month;
                }
                viewPager.setCurrentItem(0);
                graphPresenter.searchAttendanceData(userId, date);

            }
        });
    }

    @Override
    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
        bgApiProcess.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        progressBar.setVisibility(View.INVISIBLE);
        bgApiProcess.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showError(String message) {
        Toasty.success(getActivity().getApplicationContext(),message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showGraph(List<String[]> xAxis,List<Integer[]> yAxis) {
        System.out.println(xAxis);
        setupViewPager(viewPager,yAxis,xAxis);
        tabLayout.setupWithViewPager(viewPager);
    }

    // Add Fragments to Tabs
    private void setupViewPager(ViewPager viewPager,List<Integer[]> yAxis, List<String[]> xAxis) {

        Adapter adapter = new Adapter(getChildFragmentManager());
        adapter.addFragment(new DataHoursFragment(xAxis,yAxis), "hours");
        adapter.addFragment(new DataOverTimeFragment(), "overtime");
        viewPager.setAdapter(adapter);

    }

    static class Adapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        private Adapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        private void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

        @Override
        public int getItemPosition(Object object) {
            Fragment target = (Fragment) object;
            if (mFragmentList.contains(target)) {
                return POSITION_UNCHANGED;
            }

            return POSITION_NONE;
        }

        public void refresh() {
            mFragmentList.clear();
            notifyDataSetChanged();
        }
    }
}
