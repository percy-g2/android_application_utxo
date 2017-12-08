package com.androidevlinux.percy.UTXO.ui.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidevlinux.percy.UTXO.R;
import com.androidevlinux.percy.UTXO.data.models.bitfinex.BitfinexPubTickerResponseBean;
import com.androidevlinux.percy.UTXO.data.models.price.PriceBean;
import com.androidevlinux.percy.UTXO.data.models.price.ZebPayBean;
import com.androidevlinux.percy.UTXO.ui.adapter.PriceAdapter;
import com.androidevlinux.percy.UTXO.ui.base.BaseFragment;
import com.androidevlinux.percy.UTXO.utils.Constants;
import com.androidevlinux.percy.UTXO.utils.CustomProgressDialog;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by percy on 26/11/17.
 */

public class PriceCheckFragment extends BaseFragment {
    Unbinder unbinder;
    SharedPreferences mSharedPreferences;
    @BindView(R.id.btn_refresh)
    AppCompatButton btnRefresh;
    @BindView(R.id.price_list_recycler_view)
    RecyclerView priceListRecyclerView;
    private Activity mActivity;
    String TAG = "PriceCheckFragment", strRuppeSymbol = "\u20B9", strDollarSymbol = "$";
    ArrayList<PriceBean> priceBeanArrayList;
    PriceAdapter priceAdapter;

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
        View view = inflater.inflate(R.layout.btc_price_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;

    }

    @Override
    public void onViewCreated(@Nullable View view, @Nullable Bundle savedInstanceState) {
        assert view != null;
        super.onViewCreated(view, savedInstanceState);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mActivity);
        TextView Title = mActivity.findViewById(R.id.txtTitle);
        Title.setText(getResources().getString(R.string.btc_price));
        priceBeanArrayList = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        priceListRecyclerView.setLayoutManager(linearLayoutManager);
        priceAdapter = new PriceAdapter(priceBeanArrayList, mActivity);
        priceListRecyclerView.setAdapter(priceAdapter);
        getBitfinexPubTicker();
        getBitstampTicker();
        getZebpayTicker();
        boolean isRefreshButtonEnabled = mSharedPreferences.getBoolean(SettingsFragment.refresh_btc_price_button_key, false);
        if (!isRefreshButtonEnabled) {
            handler.postDelayed(runnable, 60000);
        } else {
            btnRefresh.setVisibility(View.VISIBLE);
        }
    }

    // Init
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            priceBeanArrayList.clear();
            getBitfinexPubTicker();
            getBitstampTicker();
            getZebpayTicker();
            handler.postDelayed(this, 60000);
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        handler.removeCallbacks(runnable);
        unbinder.unbind();
    }

    public static String rupeeFormat(String value) {
        value = value.replace(",", "");
        char lastDigit = value.charAt(value.length() - 1);
        String result = "";
        int len = value.length() - 1;
        int nDigits = 0;

        for (int i = len - 1; i >= 0; i--) {
            result = value.charAt(i) + result;
            nDigits++;
            if (((nDigits % 2) == 0) && (i > 0)) {
                result = "," + result;
            }
        }
        return (result + lastDigit);
    }

    private void getBitfinexPubTicker() {
        bitfinexApiManager.getBitfinexPubTicker(new Callback<BitfinexPubTickerResponseBean>() {
            @Override
            public void onResponse(@NonNull Call<BitfinexPubTickerResponseBean> call, @NonNull Response<BitfinexPubTickerResponseBean> response) {
                if (response.body() != null) {
                        Constants.btc_price = response.body().getLastPrice();
                        Constants.btc_price_low = response.body().getLow();
                        Constants.btc_price_high = response.body().getHigh();
                        PriceBean priceBean = new PriceBean();
                        priceBean.setTitle("Bitfinex");
                        priceBean.setPrice(strDollarSymbol + Constants.btc_price);
                        priceBean.setLow_price(strDollarSymbol + Constants.btc_price_low);
                        priceBean.setHigh_price(strDollarSymbol + Constants.btc_price_high);
                        priceBeanArrayList.add(priceBean);
                        priceAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(@NonNull Call<BitfinexPubTickerResponseBean> call, @NonNull Throwable t) {
            }
        });
    }

    private void getBitstampTicker() {
        bitstampApiManager.getBitstampTicker(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if (response.body() != null) {
                    Constants.btc_price = String.valueOf(response.body().get("last")).replaceAll("^\"|\"$", "");
                    Constants.btc_price_low = String.valueOf(response.body().get("high")).replaceAll("^\"|\"$", "");
                    Constants.btc_price_high = String.valueOf(response.body().get("low")).replaceAll("^\"|\"$", "");
                    PriceBean priceBean = new PriceBean();
                    priceBean.setTitle("Bitstamp");
                    priceBean.setPrice(strDollarSymbol + Constants.btc_price);
                    priceBean.setLow_price(strDollarSymbol + Constants.btc_price_low);
                    priceBean.setHigh_price(strDollarSymbol + Constants.btc_price_high);
                    priceBeanArrayList.add(priceBean);
                    priceAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
            }
        });
    }

    private void getZebpayTicker() {
        zebpayApiManager.getZebpayTicker(new Callback<ZebPayBean>() {
            @Override
            public void onResponse(@NonNull Call<ZebPayBean> call, @NonNull Response<ZebPayBean> response) {
                if (response.body() != null) {
                    Constants.btc_price = String.valueOf(response.body().getMarket());
                    Constants.btc_price_low = String.valueOf(response.body().getBuy());
                    Constants.btc_price_high = String.valueOf(response.body().getSell());
                    PriceBean priceBean = new PriceBean();
                    priceBean.setTitle("Zebpay");
                    priceBean.setPrice(strRuppeSymbol + rupeeFormat(Constants.btc_price.substring(0, Constants.btc_price.length() - 2)));
                    priceBean.setLow_price(strRuppeSymbol + rupeeFormat(Constants.btc_price_low.substring(0, Constants.btc_price_low.length() - 2)));
                    priceBean.setHigh_price(strRuppeSymbol + rupeeFormat(Constants.btc_price_high.substring(0, Constants.btc_price_high.length() - 2)));
                    priceBeanArrayList.add(priceBean);
                    priceAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ZebPayBean> call, @NonNull Throwable t) {
            }
        });
    }

    @OnClick(R.id.btn_refresh)
    public void onClick() {
        new UpdateTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private class UpdateTask extends AsyncTask<String, String, String> {

        private Dialog dialogToSaveData = null;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialogToSaveData = CustomProgressDialog.showCustomProgressDialog(getActivity(), "Please Wait Loading Data ...");
            priceBeanArrayList.clear();
        }

        @Override
        protected String doInBackground(String... value) {
            getBitfinexPubTicker();
            getBitstampTicker();
            getZebpayTicker();
            return null;
        }

        @Override
        protected void onPostExecute(String value) {
            priceAdapter.notifyDataSetChanged();
            if (dialogToSaveData != null) {
                CustomProgressDialog.dismissCustomProgressDialog(dialogToSaveData);
            }
        }
    }

}
