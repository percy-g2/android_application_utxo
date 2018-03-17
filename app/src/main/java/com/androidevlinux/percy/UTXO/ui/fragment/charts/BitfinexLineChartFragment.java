package com.androidevlinux.percy.UTXO.ui.fragment.charts;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
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
import com.androidevlinux.percy.UTXO.utils.CustomMarkerViewLineChart;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
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
 * Created by percy on 21/2/18.
 */

public class BitfinexLineChartFragment extends BaseFragment {


    @BindView(R.id.get_fab)
    AppCompatImageView getFab;
    @BindView(R.id.lineChart)
    LineChart lineChart;
    Unbinder unbinder;
    @BindView(R.id.spinner_TimeFrame)
    AppCompatSpinner spinnerTimeFrame;
    @BindView(R.id.spinner_currency)
    AppCompatSpinner spinnerCurrency;
    ArrayList<String> xValues = new ArrayList<>();
    ArrayList<Entry> yVals1 = new ArrayList<>();
    ArrayList<ILineDataSet> dataSets = new ArrayList<>();
    int count = -1;
    int currencyId = 0;
    Description description = new Description();
    @BindView(R.id.share)
    AppCompatImageView share;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;

    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        assert inflater != null;
        View view = inflater.inflate(R.layout.bitfinex_line_chart_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@Nullable View view, @Nullable Bundle savedInstanceState) {
        assert view != null;
        super.onViewCreated(view, savedInstanceState);
        TextView Title = mActivity.findViewById(R.id.txtTitle);
        Title.setText(getResources().getString(R.string.bitfinex_line_chart));
        SharedPreferences mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mActivity);

        getBitfinexData(currencyId);
        boolean isRefreshButtonEnabled = mSharedPreferences.getBoolean(SettingsFragment.refresh_btc_price_button_key, true);
        if (!isRefreshButtonEnabled) {
            getBitfinexData(currencyId);
            handler.postDelayed(runnable, 60000);
        } else {
            lineChart.setNoDataText("Click On Refresh Button");
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
        CustomMarkerViewLineChart mv = new CustomMarkerViewLineChart(lineChart, mActivity, R.layout.custom_marker_view_layout);
        // set the marker to the chart
        lineChart.setMarker(mv);

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
                    yVals1.clear();
                    xValues.clear();
                    if (lineChart != null) {
                        lineChart.clear();
                        lineChart.invalidate();
                    }
                    dataSets.clear();
                    count = -1;

                    Collections.reverse(Arrays.asList(newMap));
                    for (BigDecimal[] s : newMap) {
                        count += 1;
                        //new

                        yVals1.add(new Entry(count, Float.valueOf(String.valueOf(s[2]))));
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


                        XAxis xAxis = lineChart.getXAxis();
                        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                        xAxis.setDrawGridLines(false);
                        xAxis.setAxisLineColor(Color.BLACK);
                        xAxis.setTextColor(Color.BLACK);
                        xAxis.setGranularity(1f);

                        xAxis.setValueFormatter((value1, axis) -> xValues.get((int) value1 % xValues.size()));

                        YAxis leftAxis = lineChart.getAxisLeft();
                        leftAxis.setTextColor(Color.BLACK);
                        leftAxis.setDrawAxisLine(true);
                        leftAxis.setDrawZeroLine(true);
                        leftAxis.setDrawGridLines(false);
                        leftAxis.setGranularityEnabled(true);
                        leftAxis.setGridColor(Color.BLACK);
                        leftAxis.setAxisLineColor(Color.BLACK);

                        lineChart.getAxisRight().setEnabled(false);
                        LineDataSet set1;

                        set1 = new LineDataSet(yVals1, "DataSet 1");

                        set1.setAxisDependency(YAxis.AxisDependency.LEFT);
                        set1.setColor(Color.BLACK);
                        set1.setDrawCircles(false);
                        set1.setLineWidth(1f);
                        set1.setCircleRadius(3f);
                        set1.setFillAlpha(50);
                        set1.setDrawFilled(false);
                        Drawable drawable = ContextCompat.getDrawable(mActivity, R.drawable.fade);
                        set1.setFillDrawable(drawable);
                        set1.setDrawCircleHole(false);
                        set1.setDrawHorizontalHighlightIndicator(true);
                        dataSets.add(set1);
                        LineData lineData = new LineData(dataSets);
                        lineData.setDrawValues(false);
                        lineChart.animateY(30, Easing.EasingOption.EaseOutBack);
                        lineChart.animateX(30, Easing.EasingOption.EaseOutBack);
                        lineChart.setData(lineData);

                    }

                    description.setText(spinnerCurrency.getSelectedItem().toString());
                    description.setTextSize(20f);
                    description.setTextAlign(Paint.Align.RIGHT);
                    lineChart.setDescription(description);
                    // enable scaling and dragging
                    lineChart.setDragEnabled(true);
                    lineChart.setScaleEnabled(true);
                    lineChart.setExtraRightOffset(30f);
                    lineChart.setBackgroundColor(Color.WHITE);
                    //lineChart.setGridBackgroundColor(Color.WHITE);
                    lineChart.setDrawGridBackground(false);

                    lineChart.setDrawBorders(true);


                    lineChart.setPinchZoom(true);

                    Legend l = lineChart.getLegend();
                    l.setEnabled(false);
                    lineChart.invalidate();
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
        lineChart.saveToGallery("chart", 100);

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
            Log.i(BitfinexLineChartFragment.class.getName(), e.getMessage());
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