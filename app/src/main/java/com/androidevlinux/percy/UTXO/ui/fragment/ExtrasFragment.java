package com.androidevlinux.percy.UTXO.ui.fragment;

import android.app.Dialog;
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
import com.androidevlinux.percy.UTXO.data.models.BitfinexPubTickerResponseBean;
import com.androidevlinux.percy.UTXO.data.network.BitfinexRetrofitBaseApi;
import com.androidevlinux.percy.UTXO.data.network.InterfaceAPI;
import com.androidevlinux.percy.UTXO.utils.Constants;
import com.androidevlinux.percy.UTXO.utils.CustomProgressDialog;

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
 * Created by percy on 18/11/17.
 */

public class ExtrasFragment extends Fragment {
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

    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        assert inflater != null;
        View view = inflater.inflate(R.layout.extras_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;

    }

    @Override
    public void onViewCreated(@Nullable View view, @Nullable Bundle savedInstanceState) {
        assert view != null;
        super.onViewCreated(view, savedInstanceState);
        TextView Title = getActivity().findViewById(R.id.txtTitle);
        Title.setText(getResources().getString(R.string.extras));
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        boolean isRefreshButtonEnabled = mSharedPreferences.getBoolean(SettingsFragment.refresh_btc_price_button_key, false);
        if (!isRefreshButtonEnabled) {
            handler.postDelayed(runnable, 30000);
            bitfinexLastPrice.setText(MessageFormat.format("$ {0}", Constants.btc_price));
            bitfinexLowPrice.setText(MessageFormat.format("$ {0}", Constants.btc_price_low));
            bitfinexHighPrice.setText(MessageFormat.format("$ {0}", Constants.btc_price_high));
        } else {
            btnRefresh.setVisibility(View.VISIBLE);
            bitfinexLastPrice.setText(MessageFormat.format("$ {0}", Constants.btc_price));
            bitfinexLowPrice.setText(MessageFormat.format("$ {0}", Constants.btc_price_low));
            bitfinexHighPrice.setText(MessageFormat.format("$ {0}", Constants.btc_price_high));
        }
    }

    // Init
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            getBitfinexPubTicker();
            handler.postDelayed(this, 30000);
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        handler.removeCallbacks(runnable);
        unbinder.unbind();
    }

    private void getBitfinexPubTicker() {
        InterfaceAPI service = BitfinexRetrofitBaseApi.getClient().create(InterfaceAPI.class);
        Call<BitfinexPubTickerResponseBean> call = service.getBitfinexPubTicker();
        final Dialog dialogToSaveData = CustomProgressDialog.showCustomProgressDialog(getActivity(), "Please Wait Loading Bitcoin Last Price ...");
        call.enqueue(new Callback<BitfinexPubTickerResponseBean>() {
            @Override
            public void onResponse(@NonNull Call<BitfinexPubTickerResponseBean> call, @NonNull Response<BitfinexPubTickerResponseBean> response) {
                if (response.body() != null) {
                    if (getVisibleFragment() instanceof ExtrasFragment) {
                        Log.i("DownloadFlagSuccess", response.body().getLastPrice());
                        Constants.btc_price = response.body().getLastPrice();
                        Constants.btc_price_low = response.body().getLow();
                        Constants.btc_price_high = response.body().getHigh();
                        bitfinexLastPrice.setText(MessageFormat.format("$ {0}", Constants.btc_price));
                        bitfinexLowPrice.setText(MessageFormat.format("$ {0}", Constants.btc_price_low));
                        bitfinexHighPrice.setText(MessageFormat.format("$ {0}", Constants.btc_price_high));
                        bitfinexVolumePrice.setText(MessageFormat.format("Bitcoin {0}", response.body().getVolume()));
                    }
                }
                if (dialogToSaveData != null) {
                    CustomProgressDialog.dismissCustomProgressDialog(dialogToSaveData);
                }
            }

            @Override
            public void onFailure(@NonNull Call<BitfinexPubTickerResponseBean> call, @NonNull Throwable t) {
                if (getVisibleFragment() instanceof ExtrasFragment) {
                    Log.i("DownloadFlagFail", t.getMessage());
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
        getBitfinexPubTicker();
    }
}
