package com.androidevlinux.percy.UTXO.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
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

import java.util.ArrayList;

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

public class Test extends BaseFragment {

    @BindView(R.id.chart1)
    LineChart mChart;
    Unbinder unbinder;
    ArrayList<Entry> x;
    ArrayList<String> y;
    public String TAG = "YOUR CLASS NAME";
    int count = 0;
    @BindView(R.id.asasx)
    AppCompatButton asasx;

    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        assert inflater != null;
        View view = inflater.inflate(R.layout.test, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;

    }

    @Override
    public void onViewCreated(@Nullable View view, @Nullable Bundle savedInstanceState) {
        assert view != null;
        super.onViewCreated(view, savedInstanceState);
        x = new ArrayList<Entry>();
        y = new ArrayList<String>();
        mChart.setDrawGridBackground(false);
        //mChart.setDescription("");
        mChart.setTouchEnabled(true);
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setPinchZoom(true);
        XAxis xl = mChart.getXAxis();
        xl.setAvoidFirstLastClipping(true);
        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setInverted(true);
        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setEnabled(false);
        Legend l = mChart.getLegend();
        l.setForm(Legend.LegendForm.LINE);
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
                    String date = response.body().getTimestamp();
                    count = count + 1;
                    x.add(new Entry(value, count));
                    y.add(date);
                    LineDataSet set1 = new LineDataSet(x, "NAV Data Value");
                    set1.setLineWidth(1.5f);
                    set1.setCircleRadius(4f);
                    LineData data = new LineData(set1);
                    mChart.setData(data);
                    mChart.invalidate();

                }
            }

            @Override
            public void onFailure(@NonNull Call<BitfinexPubTickerResponseBean> call, @NonNull Throwable t) {
            }
        });
    }

    @OnClick(R.id.asasx)
    public void onClick() {
        getBitfinexPubTicker();
    }
}
