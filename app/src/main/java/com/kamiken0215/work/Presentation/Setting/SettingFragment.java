package com.kamiken0215.work.Presentation.Setting;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.kamiken0215.work.BuildConfig;
import com.kamiken0215.work.Domain.Entities.Attendance;
import com.kamiken0215.work.Presentation.Main.MainViewModel;
import com.kamiken0215.work.Presentation.Main.MainViewModelFactory;
import com.kamiken0215.work.Presentation.TimePicker.YearMonthPickerDialogFragment;
import com.kamiken0215.work.R;
import com.kamiken0215.work.Util.AttendanceStatusChecker;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Objects;

import es.dmoral.toasty.Toasty;

public class SettingFragment extends Fragment {

    MainViewModel mainViewModel;

    //カンマ
    private static final String COMMA = ",";
    //改行
    private static final String NEW_LINE= "\r\n";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_setting, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        Bundle bundle = getArguments();
        assert bundle != null;
        String userId = bundle.getString("USER_ID");
        String token = bundle.getString("TOKEN");


        mainViewModel = new ViewModelProvider(this, new MainViewModelFactory(Objects.requireNonNull(getActivity()).getApplication())).get(MainViewModel.class);
        mainViewModel.getAttendances().observe(getViewLifecycleOwner(), attendances -> {
            if (attendances == null) {
                Toasty.error(getActivity().getApplicationContext(),"データの取得に失敗しました",Toast.LENGTH_LONG).show();
            } else if (attendances.size() == 0) {
                Toasty.error(getActivity().getApplicationContext(),"データが存在しません",Toast.LENGTH_LONG).show();
            } else {
                //csv加工
                exportCsv(attendances);
            }
        });

        LinearLayout linerCsvExp = view.findViewById(R.id.setting_linear_expcsv);

        linerCsvExp.setOnClickListener(v -> {
            //年月選択ダイアログを表示
            YearMonthPickerDialogFragment pd = new YearMonthPickerDialogFragment();
            pd.setListener((datePicker, year, month, day) -> {
                String yyyyMM = String.format("%02d%02d",year,month);
                mainViewModel.fetchYearMonth(userId,yyyyMM);
            });
            pd.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), "MonthYearPickerDialog");

        });

    }

    private void exportCsv(List<Attendance> attendances) {

        AttendanceStatusChecker asc = new AttendanceStatusChecker();

        String path = Objects.requireNonNull(Objects.requireNonNull(
                getActivity()).getApplicationContext()
                .getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)).getPath() + "/kikyo_work.csv";

        BufferedWriter bw = null;

        try {
            // csvファイル作成
            bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path, false), "SJIS"));
            bw.write("出勤区分,");
            bw.write("出勤時間,");
            bw.write("退勤時間,");
            bw.newLine();

            for (Attendance a : attendances) {
                String status = asc.statusChecker(a.getAttendanceClass());
                bw.append(status);
                bw.append(COMMA);
                bw.append(a.getStartTime());
                bw.append(COMMA);
                bw.append(a.getEndTime());
                bw.append(NEW_LINE);
                bw.newLine();
            }

            bw.flush();

            System.out.println("CSVファイル出力完了");

            Toasty.success(getActivity().getApplicationContext(),"CSVファイル出力完了", Toast.LENGTH_SHORT).show();

            Uri uri = FileProvider.getUriForFile(Objects.requireNonNull(getActivity().getApplicationContext()),
                    BuildConfig.APPLICATION_ID + ".provider", new File(path));

            Intent intent = new Intent();
            intent.putExtra(Intent.EXTRA_STREAM, uri);
            intent.setAction(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_SUBJECT, "");
            intent.putExtra(Intent.EXTRA_TEXT, "");
            // Gmailにファイルの添付
            intent.setType("application/*");
            intent.setPackage("com.google.android.gm");
            try {
                startActivity(intent);
            } catch (android.content.ActivityNotFoundException ex) {
                ex.printStackTrace();
                //Toast.makeText(this.getApplicationContext(), "client not found", Toast.LENGTH_LONG).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bw != null) {
                    bw.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}
