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
import android.support.v7.widget.AppCompatSpinner;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidevlinux.percy.UTXO.R;
import com.androidevlinux.percy.UTXO.ui.base.BaseFragment;
import com.androidevlinux.percy.UTXO.ui.fragment.SettingsFragment;
import com.github.mikephil.charting.charts.CandleStickChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;
import com.google.gson.Gson;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
    FloatingActionButton getFab;
    @BindView(R.id.candleChart)
    CandleStickChart candleChart;
    Unbinder unbinder;
    @BindView(R.id.spinner_TimeFrame)
    AppCompatSpinner spinnerTimeFrame;
    private Activity mActivity;
    ArrayList<CandleEntry> entries = new ArrayList<>();
    ArrayList<String> xValues = new ArrayList<>();

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
        Title.setText(getResources().getString(R.string.bitfinex_candle_graph));
        Description description = new Description();
        description.setText("Bitfinex");
        description.setTextAlign(Paint.Align.RIGHT);
        candleChart.setDescription(description);
        SharedPreferences mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mActivity);
        boolean isRefreshButtonEnabled = mSharedPreferences.getBoolean(SettingsFragment.refresh_btc_price_button_key, false);
        if (!isRefreshButtonEnabled) {
            getBitfinexData();
            handler.postDelayed(runnable, 60000);
        } else {
            candleChart.setNoDataText("Click On Get Data");
            getFab.setVisibility(View.VISIBLE);
        }
    }

    // Init
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            getBitfinexData();
            handler.postDelayed(this, 60000);
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void getBitfinexData() {
        apiManager.getBitfinexData(spinnerTimeFrame.getSelectedItem().toString(), new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                Gson gson = new Gson();
                try {
                    BigDecimal[][] newMap;
                    BigDecimal[][] dummy = new BigDecimal[0][0];
                    newMap = gson.fromJson(response.body().string(), dummy.getClass());
                    entries.clear();
                    xValues.clear();
                    candleChart.clear();
                    candleChart.invalidate();
                    int count = -1;
                    for (BigDecimal[] s : newMap) {
                        count += 1;
                        entries.add(new CandleEntry(count, Float.valueOf(String.valueOf(s[3])), Float.valueOf(String.valueOf(s[4])), Float.valueOf(String.valueOf(s[1])), Float.valueOf(String.valueOf(s[2]))));
                        CandleDataSet dataset = new CandleDataSet(entries, "label");
                        dataset.setColor(Color.rgb(80, 80, 80));
                        dataset.setShadowColor(Color.DKGRAY);
                        dataset.setShadowWidth(0.7f);
                        dataset.setDecreasingColor(Color.RED);
                        dataset.setDecreasingPaintStyle(Paint.Style.FILL);
                        dataset.setIncreasingColor(Color.rgb(122, 242, 84));
                        dataset.setIncreasingPaintStyle(Paint.Style.FILL);
                        dataset.setNeutralColor(Color.BLUE);
                        dataset.setValueTextColor(Color.RED);
                        Date date = new Date(Long.valueOf(String.valueOf(s[0])));
                        SimpleDateFormat sdf;
                        if (spinnerTimeFrame.getSelectedItem().toString().equals("7D") ||
                                spinnerTimeFrame.getSelectedItem().toString().equals("14D") ||
                                spinnerTimeFrame.getSelectedItem().toString().equals("1M")) {
                            sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                        } else {
                            sdf = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
                        }
                        sdf.setTimeZone(TimeZone.getDefault());
                        String formattedDate = sdf.format(date);

                        xValues.add(formattedDate);
                        XAxis xAxis = candleChart.getXAxis();
                        xAxis.setValueFormatter((value1, axis) -> xValues.get((int) value1 % xValues.size()));
/*
                        Log.i("time", String.valueOf(s[0]));
                        Log.i("open", String.valueOf(s[1]));
                        Log.i("close", String.valueOf(s[2]));
                        Log.i("high", String.valueOf(s[3]));
                        Log.i("low", String.valueOf(s[4]));
                        Log.i("volume", String.valueOf(s[5]));
*/
                        CandleData data = new CandleData(dataset);
                        candleChart.setData(data);
                    }
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

    @OnClick(R.id.get_fab)
    public void onClick() {
        getBitfinexData();
    }
}