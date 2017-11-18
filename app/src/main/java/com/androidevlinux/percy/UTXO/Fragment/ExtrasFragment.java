package com.androidevlinux.percy.UTXO.Fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidevlinux.percy.UTXO.Beans.BitfinexPubTickerResponseBean;
import com.androidevlinux.percy.UTXO.R;
import com.androidevlinux.percy.UTXO.Retrofit.BitfinexRetrofitBaseApi;
import com.androidevlinux.percy.UTXO.Retrofit.InterfaceAPI;
import com.androidevlinux.percy.UTXO.Utils.CustomProgressDialog;

import java.text.MessageFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
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
        //timer.schedule(timerTask, 0, 10000);
        //Start
        handler.postDelayed(runnable, 5000);
    }

    // Init
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            getBitfinexPubTicker();
            handler.postDelayed(this, 5000);
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
                if (getVisibleFragment() instanceof ExtrasFragment) {
                    Log.i("DownloadFlagSuccess", response.body().getLastPrice());
                    bitfinexLastPrice.setText(MessageFormat.format("$ {0}", response.body().getLastPrice()));
                    bitfinexLowPrice.setText(MessageFormat.format("$ {0}", response.body().getLastPrice()));
                    bitfinexHighPrice.setText(MessageFormat.format("$ {0}", response.body().getHigh()));
                    bitfinexVolumePrice.setText(MessageFormat.format("Bitcoin {0}", response.body().getVolume()));
                    if (dialogToSaveData != null) {
                        CustomProgressDialog.dismissCustomProgressDialog(dialogToSaveData);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<BitfinexPubTickerResponseBean> call, @NonNull Throwable t) {
                if (getVisibleFragment() instanceof ExtrasFragment) {
                    Log.i("DownloadFlagFail", t.getMessage());
                    bitfinexLastPrice.setText(getString(R.string.zerodotzerozero));
                    bitfinexLowPrice.setText(getString(R.string.zerodotzerozero));
                    bitfinexHighPrice.setText(getString(R.string.zerodotzerozero));
                    bitfinexVolumePrice.setText("Bitcoin 0.00");
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
}
