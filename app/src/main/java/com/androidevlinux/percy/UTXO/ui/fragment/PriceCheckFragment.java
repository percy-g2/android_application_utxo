package com.androidevlinux.percy.UTXO.ui.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidevlinux.percy.UTXO.R;
import com.androidevlinux.percy.UTXO.data.models.bitfinex.BitfinexPubTickerResponseBean;
import com.androidevlinux.percy.UTXO.ui.base.BaseFragment;
import com.androidevlinux.percy.UTXO.utils.Constants;
import com.androidevlinux.percy.UTXO.utils.CustomProgressDialog;
import com.google.gson.JsonObject;

import java.text.MessageFormat;
import java.util.List;

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
    @BindView(R.id.bitfinex_last_price)
    AppCompatTextView bitfinexLastPrice;
    Unbinder unbinder;
    @BindView(R.id.bitfinex_low_price)
    AppCompatTextView bitfinexLowPrice;
    @BindView(R.id.bitfinex_high_price)
    AppCompatTextView bitfinexHighPrice;
    @BindView(R.id.bitfinex_volume_price)
    AppCompatTextView bitfinexVolumePrice;
    SharedPreferences mSharedPreferences;
    @BindView(R.id.btn_refresh)
    AppCompatButton btnRefresh;
    @BindView(R.id.txt_source)
    AppCompatTextView txtSource;
    private Activity mActivity;
    String price_list_source, TAG = "PriceCheckFragment", strCurrencySymbol;

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
        price_list_source = mSharedPreferences.getString(SettingsFragment.price_list_key, "Bitfinex");
        TextView Title = mActivity.findViewById(R.id.txtTitle);
        Title.setText(getResources().getString(R.string.btc_price));
        if (price_list_source.equalsIgnoreCase("Bitfinex")) {
            strCurrencySymbol = "$";
            getBitfinexPubTicker();
        } else {
            strCurrencySymbol = "\u20B9";
            getCoinsecureTicker();
        }
        txtSource.setText(MessageFormat.format("SOURCE : {0}", price_list_source));
        boolean isRefreshButtonEnabled = mSharedPreferences.getBoolean(SettingsFragment.refresh_btc_price_button_key, false);
        if (!isRefreshButtonEnabled) {
            handler.postDelayed(runnable, 60000);
            bitfinexLastPrice.setText(MessageFormat.format(strCurrencySymbol +" {0}", Constants.btc_price));
            bitfinexLowPrice.setText(MessageFormat.format(strCurrencySymbol +" {0}", Constants.btc_price_low));
            bitfinexHighPrice.setText(MessageFormat.format(strCurrencySymbol +" {0}", Constants.btc_price_high));
        } else {
            btnRefresh.setVisibility(View.VISIBLE);
            bitfinexLastPrice.setText(MessageFormat.format(strCurrencySymbol +" {0}", Constants.btc_price));
            bitfinexLowPrice.setText(MessageFormat.format(strCurrencySymbol +" {0}", Constants.btc_price_low));
            bitfinexHighPrice.setText(MessageFormat.format(strCurrencySymbol +" {0}", Constants.btc_price_high));
        }
    }

    // Init
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (price_list_source.equalsIgnoreCase("Bitfinex")) {
                getBitfinexPubTicker();
            } else {
                getCoinsecureTicker();
            }
            handler.postDelayed(this, 60000);
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        handler.removeCallbacks(runnable);
        unbinder.unbind();
    }

    private void getCoinsecureTicker() {
        final Dialog dialogToSaveData = CustomProgressDialog.showCustomProgressDialog(mActivity, "Please Wait ...");
        coinsecureApiManager.getCoinsecureTicker(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if (response.body() != null) {
                    if (getVisibleFragment() instanceof PriceCheckFragment) {
                        Log.i(TAG, response.body().toString());
                        JsonObject jsonObject = response.body().getAsJsonObject("message");
                        Constants.btc_price = String.valueOf(jsonObject.get("lastPrice"));
                        Constants.btc_price_low = String.valueOf(jsonObject.get("low"));
                        Constants.btc_price_high = String.valueOf(jsonObject.get("high"));
                        bitfinexLastPrice.setText(MessageFormat.format(strCurrencySymbol +" {0}", rupeeFormat(Constants.btc_price.substring(0, Constants.btc_price.length() - 2))));
                        bitfinexLowPrice.setText(MessageFormat.format(strCurrencySymbol +" {0}", rupeeFormat(Constants.btc_price_low.substring(0, Constants.btc_price.length() - 2))));
                        bitfinexHighPrice.setText(MessageFormat.format(strCurrencySymbol +" {0}", rupeeFormat(Constants.btc_price_high.substring(0, Constants.btc_price.length() - 2))));
                        //bitfinexVolumePrice.setText(MessageFormat.format("Bitcoin {0}", response.body().getMessage().getCoinvolume()));
                    }
                }
                if (dialogToSaveData != null) {
                    CustomProgressDialog.dismissCustomProgressDialog(dialogToSaveData);
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                if (getVisibleFragment() instanceof PriceCheckFragment) {
                    bitfinexLastPrice.setText(Constants.btc_price);
                    bitfinexLowPrice.setText(getString(R.string.zerodotzerozero));
                    bitfinexHighPrice.setText(getString(R.string.zerodotzerozero));
                    bitfinexVolumePrice.setText(R.string.zerovolume);
                    if (dialogToSaveData != null) {
                        CustomProgressDialog.dismissCustomProgressDialog(dialogToSaveData);
                    }
                }
            }
        });
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
        final Dialog dialogToSaveData = CustomProgressDialog.showCustomProgressDialog(mActivity, "Please Wait ...");
        bitfinexApiManager.getBitfinexPubTicker(new Callback<BitfinexPubTickerResponseBean>() {
            @Override
            public void onResponse(@NonNull Call<BitfinexPubTickerResponseBean> call, @NonNull Response<BitfinexPubTickerResponseBean> response) {
                if (response.body() != null) {
                    if (getVisibleFragment() instanceof PriceCheckFragment) {
                        Constants.btc_price = response.body().getLastPrice();
                        Constants.btc_price_low = response.body().getLow();
                        Constants.btc_price_high = response.body().getHigh();
                        bitfinexLastPrice.setText(MessageFormat.format(strCurrencySymbol +" {0}", Constants.btc_price));
                        bitfinexLowPrice.setText(MessageFormat.format(strCurrencySymbol +" {0}", Constants.btc_price_low));
                        bitfinexHighPrice.setText(MessageFormat.format(strCurrencySymbol +" {0}", Constants.btc_price_high));
                        bitfinexVolumePrice.setText(MessageFormat.format("Bitcoin {0}", response.body().getVolume()));
                    }
                }
                if (dialogToSaveData != null) {
                    CustomProgressDialog.dismissCustomProgressDialog(dialogToSaveData);
                }
            }

            @Override
            public void onFailure(@NonNull Call<BitfinexPubTickerResponseBean> call, @NonNull Throwable t) {
                if (getVisibleFragment() instanceof PriceCheckFragment) {
                    bitfinexLastPrice.setText(Constants.btc_price);
                    bitfinexLowPrice.setText(getString(R.string.zerodotzerozero));
                    bitfinexHighPrice.setText(getString(R.string.zerodotzerozero));
                    bitfinexVolumePrice.setText(R.string.zerovolume);
                    if (dialogToSaveData != null) {
                        CustomProgressDialog.dismissCustomProgressDialog(dialogToSaveData);
                    }
                }
            }
        });
    }

    private Fragment getVisibleFragment() {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        for (Fragment fragment : fragments) {
            if (fragment != null && fragment.isVisible())
                return fragment;
        }
        return null;
    }

    @OnClick(R.id.btn_refresh)
    public void onClick() {
        if (price_list_source.equalsIgnoreCase("Bitfinex")) {
            getBitfinexPubTicker();
        } else {
            getCoinsecureTicker();
        }
    }
}
