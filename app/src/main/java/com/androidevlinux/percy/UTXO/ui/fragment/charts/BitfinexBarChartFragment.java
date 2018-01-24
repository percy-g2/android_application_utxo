package com.androidevlinux.percy.UTXO.ui.fragment.charts;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidevlinux.percy.UTXO.R;
import com.androidevlinux.percy.UTXO.ui.base.BaseFragment;
import com.androidevlinux.percy.UTXO.ui.fragment.SettingsFragment;
import com.androidevlinux.percy.UTXO.utils.CustomMarkerViewBarChart;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.google.gson.Gson;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
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
 * Created by percy on 9/1/18.
 */

public class BitfinexBarChartFragment extends BaseFragment {

    @BindView(R.id.barChart)
    BarChart barChart;
    @BindView(R.id.get_fab)
    FloatingActionButton getFab;
    private Activity mActivity;
    Unbinder unbinder;
    int count = -1;
    List<BarEntry> entries = new ArrayList<>();
    ArrayList<String> xValues = new ArrayList<>();
    ArrayList<IBarDataSet> dataSets = new ArrayList<>();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.mActivity = null;
    }

    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        assert inflater != null;
        View view = inflater.inflate(R.layout.bitfinex_bar_chart_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@Nullable View view, @Nullable Bundle savedInstanceState) {
        assert view != null;
        super.onViewCreated(view, savedInstanceState);
        TextView Title = mActivity.findViewById(R.id.txtTitle);
        Title.setText(getResources().getString(R.string.bitfinex_bar_graph));
        Description description = new Description();
        description.setText("Bitfinex");
        description.setTextAlign(Paint.Align.RIGHT);
        barChart.setDescription(description);
        CustomMarkerViewBarChart mv = new CustomMarkerViewBarChart(barChart, getActivity(), R.layout.custom_marker_view_layout);
        // set the marker to the chart
        barChart.setMarker(mv);
        getBitfinexPubTicker();
        SharedPreferences mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mActivity);
        boolean isRefreshButtonEnabled = mSharedPreferences.getBoolean(SettingsFragment.refresh_btc_price_button_key, true);
        if (!isRefreshButtonEnabled) {
            getBitfinexPubTicker();
            handler.postDelayed(runnable, 60000);
        } else {
            barChart.setNoDataText("Click On Get Data");
            getFab.setVisibility(View.VISIBLE);
        }
    }


    // Init
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            getBitfinexPubTicker();
            handler.postDelayed(this, 60000);
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        handler.removeCallbacks(runnable);
        unbinder.unbind();
    }

    private void getBitfinexPubTicker() {
        apiManager.getBitfinexData("1m", new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        Gson gson = new Gson();

                        BigDecimal[][] newMap;
                        BigDecimal[][] dummy = new BigDecimal[0][0];

                        newMap = gson.fromJson(response.body().string(), dummy.getClass());

                        entries.clear();
                        xValues.clear();
                        count = -1;
                        Collections.reverse(Arrays.asList(newMap));
                        for (BigDecimal[] s : newMap) {
                            count += 1;
                            entries.add(new BarEntry(count, Float.valueOf(String.valueOf(s[2]))));
                            Date date = new Date(Long.valueOf(String.valueOf(s[0])));
                            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
                            sdf.setTimeZone(TimeZone.getDefault());
                            String formattedDate = sdf.format(date);

                            xValues.add(formattedDate);

                            Legend l = barChart.getLegend();
                            l.setEnabled(false);

                            XAxis xAxis = barChart.getXAxis();
                            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                            xAxis.setDrawGridLines(true);
                            xAxis.setAxisLineColor(Color.BLACK);
                            xAxis.setTextColor(Color.BLACK);
                            xAxis.setValueFormatter((value1, axis) -> xValues.get((int) value1 % xValues.size()));

                            YAxis leftAxis = barChart.getAxisLeft();
                            leftAxis.setTextColor(Color.BLACK);
                            leftAxis.setDrawAxisLine(true);
                            leftAxis.setDrawZeroLine(true);
                            leftAxis.setDrawGridLines(true);

                            leftAxis.setGridColor(Color.BLACK);
                            leftAxis.setAxisLineColor(Color.BLACK);

                            barChart.getAxisRight().setEnabled(false);

                            BarDataSet set = new BarDataSet(entries, "Price");
                            set.setAxisDependency(YAxis.AxisDependency.LEFT);

                            dataSets.add(set);
                            BarData datab = new BarData(dataSets);
                            datab.setBarWidth(0.9f); // set custom bar width
                            barChart.setData(datab);
                        }
                        barChart.setFitBars(true); // make the x-axis fit exactly all bars
                        barChart.invalidate(); // refresh
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
            }
        });
    }


    @OnClick(R.id.get_fab)
    public void onClick() {
        getBitfinexPubTicker();
    }
}
