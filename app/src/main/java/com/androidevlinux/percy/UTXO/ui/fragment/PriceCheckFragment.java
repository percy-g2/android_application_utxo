package com.androidevlinux.percy.UTXO.ui.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidevlinux.percy.UTXO.R;
import com.androidevlinux.percy.UTXO.data.models.bitfinex.BitfinexPubTickerResponseBean;
import com.androidevlinux.percy.UTXO.data.models.pocketbits.PocketBitsBean;
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
    @BindView(R.id.price_list_recycler_view)
    RecyclerView priceListRecyclerView;
    private Activity mActivity;
    String TAG = "PriceCheckFragment", strRuppeSymbol = "\u20B9", strDollarSymbol = "$";
    ArrayList<PriceBean> priceBeanArrayList;
    PriceAdapter priceAdapter;
    FloatingActionButton refreshFab;

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
        refreshFab = mActivity.findViewById(R.id.refresh_fab);
        Title.setText(getResources().getString(R.string.btc_price));
        priceBeanArrayList = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        priceListRecyclerView.setLayoutManager(linearLayoutManager);
        priceAdapter = new PriceAdapter(priceBeanArrayList);
        priceListRecyclerView.setAdapter(priceAdapter);
        getBitfinexPubTicker();
        getBitstampTicker();
        getZebpayTicker();
        getPocketbitsTicker();
        boolean isRefreshButtonEnabled = mSharedPreferences.getBoolean(SettingsFragment.refresh_btc_price_button_key, true);
        if (!isRefreshButtonEnabled) {
            refreshFab.hide();
            handler.postDelayed(runnable, 60000);
        } else {
            refreshFab.show();
        }

        refreshFab.setOnClickListener(view1 -> new UpdateTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR));
    }

    // Init
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            new UpdateTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
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
        StringBuilder result = new StringBuilder();
        int len = value.length() - 1;
        int nDigits = 0;

        for (int i = len - 1; i >= 0; i--) {
            result.insert(0, value.charAt(i));
            nDigits++;
            if (((nDigits % 2) == 0) && (i > 0)) {
                result.insert(0, ",");
            }
        }
        return (result.toString() + lastDigit);
    }

    private void getBitfinexPubTicker() {
        apiManager.getBitfinexPubTicker(new Callback<BitfinexPubTickerResponseBean>() {
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
        apiManager.getBitstampTicker(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if (response.body() != null) {
                    Constants.btc_price = String.valueOf(response.body().get("last")).replaceAll("^\"|\"$", "");
                    Constants.btc_price_low = String.valueOf(response.body().get("low")).replaceAll("^\"|\"$", "");
                    Constants.btc_price_high = String.valueOf(response.body().get("high")).replaceAll("^\"|\"$", "");
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
        apiManager.getZebpayTicker(new Callback<ZebPayBean>() {
            @Override
            public void onResponse(@NonNull Call<ZebPayBean> call, @NonNull Response<ZebPayBean> response) {
                if (response.body() != null) {
                    Constants.btc_price = String.valueOf(response.body().getBuy());
                    Constants.btc_price_low = String.valueOf(response.body().getSell());
                    Constants.btc_price_high = String.valueOf(response.body().getBuy());
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

    private void getPocketbitsTicker() {
        apiManager.getPocketbitsTicker(new Callback<PocketBitsBean>() {
            @Override
            public void onResponse(@NonNull Call<PocketBitsBean> call, @NonNull Response<PocketBitsBean> response) {
                if (response.body() != null) {
                    Constants.btc_price = String.valueOf(response.body().getBuy());
                    Constants.btc_price_low = String.valueOf(response.body().getSell());
                    Constants.btc_price_high = String.valueOf(response.body().getBuy());
                    PriceBean priceBean = new PriceBean();
                    priceBean.setTitle("Pocketbits");
                    priceBean.setPrice(strRuppeSymbol + rupeeFormat(Constants.btc_price.substring(0, Constants.btc_price.length())));
                    priceBean.setLow_price(strRuppeSymbol + rupeeFormat(Constants.btc_price_low.substring(0, Constants.btc_price_low.length())));
                    priceBean.setHigh_price(strRuppeSymbol + rupeeFormat(Constants.btc_price_high.substring(0, Constants.btc_price_high.length())));
                    priceBeanArrayList.add(priceBean);
                    priceAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(@NonNull Call<PocketBitsBean> call, @NonNull Throwable t) {
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
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
            getPocketbitsTicker();
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
