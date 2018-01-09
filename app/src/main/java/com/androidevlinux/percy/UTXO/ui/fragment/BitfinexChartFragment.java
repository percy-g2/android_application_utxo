package com.androidevlinux.percy.UTXO.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidevlinux.percy.UTXO.R;
import com.androidevlinux.percy.UTXO.data.models.bitfinex.BitfinexPubTickerResponseBean;
import com.androidevlinux.percy.UTXO.ui.base.BaseFragment;
import com.github.mikephil.charting.charts.LineChart;
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

public class BitfinexChartFragment extends BaseFragment {

    Unbinder unbinder;
    @BindView(R.id.btn_get_data)
    AppCompatButton btn_get_data;
    @BindView(R.id.barChart)
    LineChart mChart;
    private int mFillColor = Color.argb(150, 51, 181, 229);
    ArrayList<String> xValues = new ArrayList<>();
    ArrayList<Entry> yVals1 = new ArrayList<>();
    ArrayList<ILineDataSet> dataSets = new ArrayList<>();

    int count = 0;
    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        assert inflater != null;
        View view = inflater.inflate(R.layout.bitfinex_chart_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@Nullable View view, @Nullable Bundle savedInstanceState) {
        assert view != null;
        super.onViewCreated(view, savedInstanceState);
        mChart.setNoDataText("Creating Chart");
    }


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
                    Log.i("s", dates);
                    count += 1;
                    double s = Math.floor(Double.parseDouble(dates));
                    yVals1.add(new Entry(count, value));
                    Date date = new Date((int) s *1000L);
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
                    xAxis.setDrawGridLines(false);
                    xAxis.setAxisLineColor(Color.BLACK);
                    xAxis.setTextColor(Color.BLACK);

                    xAxis.setValueFormatter((value1, axis) -> xValues.get((int) value1 % xValues.size()));

                    YAxis leftAxis = mChart.getAxisLeft();
                    leftAxis.setTextColor(Color.BLACK);
                    leftAxis.setDrawAxisLine(true);
                    leftAxis.setDrawZeroLine(false);
                    leftAxis.setDrawGridLines(true);

                    leftAxis.setGridColor(Color.BLACK);
                    leftAxis.setAxisLineColor(Color.BLACK);

                    mChart.getAxisRight().setEnabled(false);
                    LineDataSet set1;

                    set1 = new LineDataSet(yVals1, "DataSet 1");

                    set1.setAxisDependency(YAxis.AxisDependency.LEFT);
                    set1.setColor(Color.BLACK);
                    set1.setDrawCircles(false);
                    set1.setLineWidth(1f);
                    set1.setCircleRadius(3f);
                    set1.setFillAlpha(50);
                    set1.setDrawFilled(true);
                    set1.setFillColor(Color.BLUE);
                    set1.setHighLightColor(Color.rgb(244, 117, 117));
                    set1.setDrawCircleHole(false);

                    dataSets.add(set1);

                    LineData datab = new LineData(dataSets);
                    datab.setDrawValues(false);

                    mChart.setData(datab);
                    mChart.invalidate();
                }
            }

            @Override
            public void onFailure(@NonNull Call<BitfinexPubTickerResponseBean> call, @NonNull Throwable t) {
            }
        });
    }


    @OnClick(R.id.btn_get_data)
    public void onClick() {
        getBitfinexPubTicker();
    }
}
