package com.androidevlinux.percy.UTXO.ui.fragment.charts;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidevlinux.percy.UTXO.R;
import com.androidevlinux.percy.UTXO.ui.base.BaseFragment;
import com.github.mikephil.charting.charts.CandleStickChart;
import com.google.gson.Gson;

import java.io.IOException;
import java.math.BigDecimal;

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
    FloatingActionButton getFab;
    @BindView(R.id.candleChart)
    CandleStickChart candleChart;
    Unbinder unbinder;
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
        View view = inflater.inflate(R.layout.bitfinex_candle_chart_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@Nullable View view, @Nullable Bundle savedInstanceState) {
        assert view != null;
        super.onViewCreated(view, savedInstanceState);
        TextView Title = mActivity.findViewById(R.id.txtTitle);
        Title.setText(getResources().getString(R.string.bitfinex_bar_graph));

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void getBitfinexData() {
        apiManager.getBitfinexData(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                Gson gson = new Gson();
                try {
                    BigDecimal[][] newMap;
                    BigDecimal[][] dummy = new BigDecimal[0][0];
                    newMap = gson.fromJson(response.body().string(), dummy.getClass());
                    Log.i("ResponseBody", String.valueOf(newMap.length));
                    for (BigDecimal[] s : newMap){
                        /*for (float ss : s) {
                            Log.i("ResponseBody", String.valueOf(ss));
                        }*/
                        Log.i("time", String.valueOf(s[0]));
                        Log.i("open", String.valueOf(s[1]));
                        Log.i("close", String.valueOf(s[2]));
                        Log.i("high", String.valueOf(s[3]));
                        Log.i("low", String.valueOf(s[4]));
                        Log.i("volume", String.valueOf(s[5]));
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
            }
        });
    }

    @OnClick(R.id.get_fab)
    public void onClick() {
        getBitfinexData();
    }
}