package com.androidevlinux.percy.UTXO.ui.fragment;

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
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.List;

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
    BarChart mChart;
    Unbinder unbinder;
    @BindView(R.id.asasx)
    AppCompatButton asasx;
    int count = 0;
    BarData data;
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
        Description description = new Description();
        description.setText("Bitfinex");
        mChart.setDescription(description);
        data = new BarData();
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
                    count += 1;
                    Log.i("s", date);
                    List<BarEntry> entries = new ArrayList<>();
                    entries.add(new BarEntry(value, count));
                    BarDataSet set = new BarDataSet(entries, "Price");
                    data.addDataSet(set);
                    data.setBarWidth(0.9f); // set custom bar width
                    mChart.setData(data);
                    mChart.setFitBars(true); // make the x-axis fit exactly all bars
                    mChart.invalidate(); // refresh
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
