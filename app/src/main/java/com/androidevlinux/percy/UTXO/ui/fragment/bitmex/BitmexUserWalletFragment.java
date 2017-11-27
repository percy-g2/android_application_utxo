package com.androidevlinux.percy.UTXO.ui.fragment.bitmex;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidevlinux.percy.UTXO.R;
import com.androidevlinux.percy.UTXO.data.models.bitmex.UserWallet;
import com.androidevlinux.percy.UTXO.ui.base.BaseFragment;
import com.androidevlinux.percy.UTXO.utils.Constants;
import com.androidevlinux.percy.UTXO.utils.Utils;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by percy on 26/11/17.
 */

public class BitmexUserWalletFragment extends BaseFragment {

    public static String TAG = "BitmexUserWalletFragment";
    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        assert inflater != null;
        return inflater.inflate(R.layout.min_amount_fragment, container, false);
    }

    @Override
    public void onViewCreated(@Nullable View view, @Nullable Bundle savedInstanceState) {
        assert view != null;
        super.onViewCreated(view, savedInstanceState);
        TextView Title = getActivity().findViewById(R.id.txtTitle);
        Title.setText(getResources().getString(R.string.min_amount_check));

        UserWallet userWallet = new UserWallet();
        userWallet.setCurrency("XBt");

        String verb = "GET";
        String path = "/api/v1/user/wallet";
        long nonce = System.currentTimeMillis();
        String data = new Gson().toJson(userWallet);

        String message = verb + path + String.valueOf(nonce) + data;

        String Secretkey = Constants.bitmex_secret_key;
        String sign = null;
        try {
            sign = Utils.hmacDigest256(message, Secretkey);
        } catch (Exception e) {
            e.printStackTrace();
        }

        bitmexApiManager.getBitmexUserWallet(sign, nonce, new Callback<UserWallet>() {
            @Override
            public void onResponse(@NonNull Call<UserWallet> call, @NonNull Response<UserWallet> response) {
                if (response.body() != null) {
                    Log.i(TAG, response.body().toString());
                }
            }
            @Override
            public void onFailure(@NonNull Call<UserWallet> call, @NonNull Throwable t) {
            }
        });
    }
}
