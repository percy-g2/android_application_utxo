package com.androidevlinux.percy.UTXO.ui.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
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
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by percy on 26/11/17.
 */

public class PriceCheckFragment extends BaseFragment {
    Unbinder unbinder;
    SharedPreferences mSharedPreferences;
    @BindView(R.id.price_list_recycler_view)
    RecyclerView priceListRecyclerView;
    String TAG = "PriceCheckFragment", strRuppeSymbol = "\u20B9", strDollarSymbol = "$";
    ArrayList<PriceBean> priceBeanArrayList;
    PriceAdapter priceAdapter;
    FloatingActionButton refreshFab;

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
        getBitStampTicker();
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
        Observable<BitfinexPubTickerResponseBean> observable = apiManager.getBitfinexTicker();
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BitfinexPubTickerResponseBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BitfinexPubTickerResponseBean value) {
                        Constants.btc_price = value.getLastPrice();
                        Constants.btc_price_low = value.getLow();
                        Constants.btc_price_high = value.getHigh();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        PriceBean priceBean = new PriceBean();
                        priceBean.setTitle("Bitfinex");
                        priceBean.setPrice(strDollarSymbol + Constants.btc_price);
                        priceBean.setLow_price(strDollarSymbol + Constants.btc_price_low);
                        priceBean.setHigh_price(strDollarSymbol + Constants.btc_price_high);
                        priceBeanArrayList.add(priceBean);
                        priceAdapter.notifyDataSetChanged();
                    }
                });
    }

    private void getBitStampTicker() {
        Observable<JsonObject> observable = apiManager.getBitstampTicker();
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JsonObject>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(JsonObject value) {
                        Constants.btc_price = String.valueOf(value.get("last")).replaceAll("^\"|\"$", "");
                        Constants.btc_price_low = String.valueOf(value.get("low")).replaceAll("^\"|\"$", "");
                        Constants.btc_price_high = String.valueOf(value.get("high")).replaceAll("^\"|\"$", "");
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        PriceBean priceBean = new PriceBean();
                        priceBean.setTitle("Bitstamp");
                        priceBean.setPrice(strDollarSymbol + Constants.btc_price);
                        priceBean.setLow_price(strDollarSymbol + Constants.btc_price_low);
                        priceBean.setHigh_price(strDollarSymbol + Constants.btc_price_high);
                        priceBeanArrayList.add(priceBean);
                        priceAdapter.notifyDataSetChanged();
                    }
                });
    }

    private void getZebpayTicker() {
        Observable<ZebPayBean> observable = apiManager.getZebpayTicker();
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ZebPayBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ZebPayBean value) {
                        Constants.btc_price = String.valueOf(value.getBuy());
                        Constants.btc_price_low = String.valueOf(value.getSell());
                        Constants.btc_price_high = String.valueOf(value.getBuy());
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        PriceBean priceBean = new PriceBean();
                        priceBean.setTitle("Zebpay");
                        priceBean.setPrice(strRuppeSymbol + rupeeFormat(Constants.btc_price.substring(0, Constants.btc_price.length() - 2)));
                        priceBean.setLow_price(strRuppeSymbol + rupeeFormat(Constants.btc_price_low.substring(0, Constants.btc_price_low.length() - 2)));
                        priceBean.setHigh_price(strRuppeSymbol + rupeeFormat(Constants.btc_price_high.substring(0, Constants.btc_price_high.length() - 2)));
                        priceBeanArrayList.add(priceBean);
                        priceAdapter.notifyDataSetChanged();
                    }
                });
    }

    private void getPocketbitsTicker() {
        Observable<PocketBitsBean> observable = apiManager.getPocketbitsTicker();
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<PocketBitsBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(PocketBitsBean value) {
                        Constants.btc_price = String.valueOf(value.getBuy());
                        Constants.btc_price_low = String.valueOf(value.getSell());
                        Constants.btc_price_high = String.valueOf(value.getBuy());
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        PriceBean priceBean = new PriceBean();
                        priceBean.setTitle("Pocketbits");
                        priceBean.setPrice(strRuppeSymbol + rupeeFormat(Constants.btc_price.substring(0, Constants.btc_price.length())));
                        priceBean.setLow_price(strRuppeSymbol + rupeeFormat(Constants.btc_price_low.substring(0, Constants.btc_price_low.length())));
                        priceBean.setHigh_price(strRuppeSymbol + rupeeFormat(Constants.btc_price_high.substring(0, Constants.btc_price_high.length())));
                        priceBeanArrayList.add(priceBean);
                        priceAdapter.notifyDataSetChanged();
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
            getBitStampTicker();
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
