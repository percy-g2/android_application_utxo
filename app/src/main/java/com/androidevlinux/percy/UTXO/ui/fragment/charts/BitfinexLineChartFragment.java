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
import com.androidevlinux.percy.UTXO.data.models.bitfinex.BitfinexPubTickerResponseBean;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by percy on 23/12/17.
 */

public class BitfinexLineChartFragment extends BaseFragment {

    Unbinder unbinder;
    @BindView(R.id.lineChart)
    LineChart mChart;
    @BindView(R.id.get_fab)
    FloatingActionButton getFab;
    private int mFillColor = Color.argb(150, 51, 181, 229);
    ArrayList<String> xValues = new ArrayList<>();
    ArrayList<Entry> yVals1 = new ArrayList<>();
    ArrayList<ILineDataSet> dataSets = new ArrayList<>();
    int count = -1;
    private Activity mActivity;

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
        View view = inflater.inflate(R.layout.bitfinex_line_chart_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@Nullable View view, @Nullable Bundle savedInstanceState) {
        assert view != null;
        super.onViewCreated(view, savedInstanceState);
        TextView Title = mActivity.findViewById(R.id.txtTitle);
        Title.setText(getResources().getString(R.string.bitfinex_line_graph));
        Description description = new Description();
        description.setText("Bitfinex");
        description.setTextAlign(Paint.Align.RIGHT);
        mChart.setDescription(description);
        CustomMarkerViewLineChart mv = new CustomMarkerViewLineChart(mChart, getActivity(), R.layout.custom_marker_view_layout);
        // set the marker to the chart
        mChart.setMarker(mv);
        SharedPreferences mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mActivity);
        boolean isRefreshButtonEnabled = mSharedPreferences.getBoolean(SettingsFragment.refresh_btc_price_button_key, false);
        if (!isRefreshButtonEnabled) {
            getBitfinexPubTicker();
            handler.postDelayed(runnable, 60000);
        } else {
            mChart.setNoDataText("Click On Get Data");
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
        unbinder.unbind();
    }

    private void getBitfinexPubTicker() {
        apiManager.getBitfinexPubTicker(new Callback<BitfinexPubTickerResponseBean>() {
            @Override
            public void onResponse(@NonNull Call<BitfinexPubTickerResponseBean> call, @NonNull Response<BitfinexPubTickerResponseBean> response) {
                if (response.body() != null) {
                    float value = Float.parseFloat(response.body().getLastPrice());
                    String dates = response.body().getTimestamp();
                    count += 1;
                    double s = Math.floor(Double.parseDouble(dates));
                    yVals1.add(new Entry(count, value));
                    Date date = new Date((int) s * 1000L);
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
                    sdf.setTimeZone(TimeZone.getDefault());
                    String formattedDate = sdf.format(date);

                    xValues.add(formattedDate);

                    mChart.setBackgroundColor(Color.WHITE);
                    mChart.setGridBackgroundColor(Color.WHITE);
                    mChart.setDrawGridBackground(true);

                    mChart.setDrawBorders(true);


                    mChart.setPinchZoom(true);

                    Legend l = mChart.getLegend();
                    l.setEnabled(false);

                    XAxis xAxis = mChart.getXAxis();
                    xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                    xAxis.setDrawGridLines(true);
                    xAxis.setAxisLineColor(Color.BLACK);
                    xAxis.setTextColor(Color.BLACK);

                    xAxis.setValueFormatter((value1, axis) -> xValues.get((int) value1 % xValues.size()));

                    YAxis leftAxis = mChart.getAxisLeft();
                    leftAxis.setTextColor(Color.BLACK);
                    leftAxis.setDrawAxisLine(true);
                    leftAxis.setDrawZeroLine(true);
                    leftAxis.setDrawGridLines(true);

                    leftAxis.setGridColor(Color.BLACK);
                    leftAxis.setAxisLineColor(Color.BLACK);

                    mChart.getAxisRight().setEnabled(false);
                    LineDataSet set1;

                    set1 = new LineDataSet(yVals1, "DataSet 1");

                    set1.setAxisDependency(YAxis.AxisDependency.LEFT);
                    set1.setColor(Color.BLACK);
                    set1.setDrawCircles(true);
                    set1.setLineWidth(1f);
                    set1.setCircleRadius(3f);
                    //  set1.setFillAlpha(50);
                    //   set1.setDrawFilled(true);
                    //  set1.setFillColor(Color.BLUE);
                    //   set1.setHighLightColor(Color.rgb(244, 117, 117));
                    set1.setDrawCircleHole(true);
                    dataSets.add(set1);

                    LineData datab = new LineData(dataSets);
                    datab.setDrawValues(false);
                    mChart.animateY(30, Easing.EasingOption.EaseOutBack);
                    mChart.animateX(30, Easing.EasingOption.EaseOutBack);
                    mChart.setData(datab);
                    mChart.invalidate();

                }
            }

            @Override
            public void onFailure(@NonNull Call<BitfinexPubTickerResponseBean> call, @NonNull Throwable t) {
            }
        });
    }


    @OnClick(R.id.get_fab)
    public void onClick() {
        getBitfinexPubTicker();
    }
}
