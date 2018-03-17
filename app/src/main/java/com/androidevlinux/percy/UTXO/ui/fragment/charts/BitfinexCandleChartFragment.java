package com.androidevlinux.percy.UTXO.ui.fragment.charts;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatSpinner;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.androidevlinux.percy.UTXO.BuildConfig;
import com.androidevlinux.percy.UTXO.R;
import com.androidevlinux.percy.UTXO.ui.base.BaseFragment;
import com.androidevlinux.percy.UTXO.ui.fragment.SettingsFragment;
import com.github.mikephil.charting.charts.CandleStickChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by percy on 10/1/18.
 */

public class BitfinexCandleChartFragment extends BaseFragment {


    @BindView(R.id.get_fab)
    AppCompatImageView getFab;
    @BindView(R.id.candleChart)
    CandleStickChart candleChart;
    Unbinder unbinder;
    @BindView(R.id.spinner_TimeFrame)
    AppCompatSpinner spinnerTimeFrame;
    @BindView(R.id.spinner_currency)
    AppCompatSpinner spinnerCurrency;
    ArrayList<CandleEntry> entries = new ArrayList<>();
    ArrayList<String> xValues = new ArrayList<>();
    int currencyId = 0;
    Description description = new Description();
    @BindView(R.id.share)
    AppCompatImageView share;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;

    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        assert inflater != null;
        View view = inflater.inflate(R.layout.bitfinex_candle_chart_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@Nullable View view, @Nullable Bundle savedInstanceState) {
        assert view != null;
        super.onViewCreated(view, savedInstanceState);
        TextView Title = mActivity.findViewById(R.id.txtTitle);
        Title.setText(getResources().getString(R.string.bitfinex_candle_chart));
        SharedPreferences mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mActivity);

        getBitfinexData(currencyId);
        boolean isRefreshButtonEnabled = mSharedPreferences.getBoolean(SettingsFragment.refresh_btc_price_button_key, true);
        if (!isRefreshButtonEnabled) {
            getBitfinexData(currencyId);
            handler.postDelayed(runnable, 60000);
        } else {
            candleChart.setNoDataText("Click On Refresh Button");
            getFab.setVisibility(View.VISIBLE);
        }

        spinnerCurrency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currencyId = position;
                getBitfinexData(currencyId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerTimeFrame.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getBitfinexData(currencyId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    // Init
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            getBitfinexData(currencyId);
            handler.postDelayed(this, 60000);
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        handler.removeCallbacks(runnable);
        unbinder.unbind();
    }

    private void getBitfinexData(int currencyId) {
        String symbol = "tBTCUSD";
        if (currencyId == 0) {
            symbol = "tBTCUSD";
        } else if (currencyId == 1) {
            symbol = "tETHUSD";
        } else if (currencyId == 2) {
            symbol = "tXRPUSD";
        } else if (currencyId == 3) {
            symbol = "tXMRUSD";
        }
        apiManager.getBitfinexData(spinnerTimeFrame.getSelectedItem().toString(), symbol, new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                Gson gson = new Gson();
                try {
                    BigDecimal[][] newMap;
                    BigDecimal[][] dummy = new BigDecimal[0][0];
                    newMap = gson.fromJson(response.body().string(), dummy.getClass());
                    entries.clear();
                    xValues.clear();
                    if (candleChart != null) {
                        candleChart.clear();
                        candleChart.invalidate();
                    }
                    int count = -1;

                    Collections.reverse(Arrays.asList(newMap));
                    for (BigDecimal[] s : newMap) {
                        count += 1;
                        entries.add(new CandleEntry(count, Float.valueOf(String.valueOf(s[3])), Float.valueOf(String.valueOf(s[4])), Float.valueOf(String.valueOf(s[1])), Float.valueOf(String.valueOf(s[2]))));
                        CandleDataSet data_set = new CandleDataSet(entries, "Data");
                        data_set.setColor(Color.rgb(80, 80, 80));
                        data_set.setShadowColor(Color.DKGRAY);
                        data_set.setShadowWidth(0.7f);
                        data_set.setDecreasingColor(Color.RED);
                        data_set.setDecreasingPaintStyle(Paint.Style.FILL);
                        data_set.setIncreasingColor(Color.rgb(122, 242, 84));
                        data_set.setIncreasingPaintStyle(Paint.Style.FILL);
                        data_set.setNeutralColor(Color.BLUE);
                        data_set.setValueTextColor(Color.RED);
                        Date date = new Date(Long.valueOf(String.valueOf(s[0])));
                        SimpleDateFormat sdf;
                        if (spinnerTimeFrame.getSelectedItem().toString().equals("7D") ||
                                spinnerTimeFrame.getSelectedItem().toString().equals("14D") ||
                                spinnerTimeFrame.getSelectedItem().toString().equals("1M")) {
                            sdf = new SimpleDateFormat("dd MMM yy", Locale.ENGLISH);
                        } else {
                            sdf = new SimpleDateFormat("dd HH:mm", Locale.ENGLISH);
                        }
                        sdf.setTimeZone(TimeZone.getDefault());
                        String formattedDate = sdf.format(date);

                        xValues.add(formattedDate);
                        YAxis yAxis = candleChart.getAxisRight();
                        yAxis.setGranularityEnabled(true);
                        XAxis xAxis = candleChart.getXAxis();
                        xAxis.setGranularity(1f);
                        xAxis.setValueFormatter((value1, axis) -> xValues.get((int) value1 % xValues.size()));
                        CandleData data = new CandleData(data_set);
                        candleChart.setData(data);
                    }
                    description.setText(spinnerCurrency.getSelectedItem().toString());
                    description.setTextSize(15f);
                    description.setTextAlign(Paint.Align.RIGHT);
                    candleChart.setDescription(description);
                    candleChart.setDragEnabled(true);
                    candleChart.setScaleEnabled(true);
                    candleChart.setExtraRightOffset(30f);
                    candleChart.setBackgroundColor(Color.WHITE);
                    //mChart.setGridBackgroundColor(Color.WHITE);
                    candleChart.setDrawGridBackground(false);

                    candleChart.setDrawBorders(true);


                    candleChart.setPinchZoom(true);

                    Legend l = candleChart.getLegend();
                    l.setEnabled(false);
                    candleChart.setAutoScaleMinMaxEnabled(true);
                    candleChart.invalidate();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
            }
        });
    }

    @OnClick({R.id.share, R.id.get_fab})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.share:
                permissionCheck();
                break;
            case R.id.get_fab:
                getBitfinexData(currencyId);
                break;
        }
    }

    private void permissionCheck() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (mActivity.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_EXTERNAL_STORAGE);
            } else {
                saveChartAndShare();
            }
        } else {
            saveChartAndShare();
        }
    }

    private void saveChartAndShare() {
        candleChart.saveToGallery("chart", 100);

        File file = new File(Environment.getExternalStorageDirectory() + "/DCIM/chart.jpg");
        Intent target = new Intent();
        target.setAction(Intent.ACTION_SEND);
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(mActivity,
                    BuildConfig.APPLICATION_ID + ".provider",
                    file);
        } else {
            uri = Uri.fromFile(file);
        }
        target.putExtra(Intent.EXTRA_STREAM, uri);
        target.setType("image/jpeg");
        Intent intent = Intent.createChooser(target, "Share File");
        try {
            startActivity(intent);
        } catch (Exception e) {
            Log.i(BitfinexCandleChartFragment.class.getName(), e.getMessage());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_EXTERNAL_STORAGE
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            saveChartAndShare();
        }
    }

}