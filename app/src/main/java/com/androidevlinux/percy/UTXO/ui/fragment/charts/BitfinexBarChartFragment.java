package com.androidevlinux.percy.UTXO.ui.fragment.charts;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidevlinux.percy.UTXO.R;
import com.androidevlinux.percy.UTXO.data.models.bitfinex.BitfinexPubTickerResponseBean;
import com.androidevlinux.percy.UTXO.ui.base.BaseFragment;
import com.github.mikephil.charting.charts.BarChart;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by percy on 9/1/18.
 */

public class BitfinexBarChartFragment extends BaseFragment {

    @BindView(R.id.barChart)
    BarChart barChart;
    @BindView(R.id.btn_get_data)
    AppCompatButton btnGetData;
    private Activity mActivity;
    Unbinder unbinder;
    int count = 0;

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
                    count += 1;
                    double s = Math.floor(Double.parseDouble(dates));

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
