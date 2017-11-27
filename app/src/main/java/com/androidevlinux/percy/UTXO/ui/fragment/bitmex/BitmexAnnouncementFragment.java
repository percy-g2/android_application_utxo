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
import com.androidevlinux.percy.UTXO.data.models.bitmex.Announcement;
import com.androidevlinux.percy.UTXO.ui.base.BaseFragment;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by percy on 25/11/17.
 */

public class BitmexAnnouncementFragment extends BaseFragment {

    public static String TAG = "BitmexAnnouncementFragment";
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

        Announcement announcement = new Announcement();
        announcement.setId(1);
        announcement.setContent("");
        announcement.setDate("");
        announcement.setLink("");
        announcement.setTitle("");
        String verb = "GET";
        String path = "/api/v1/get/announcement";
        String nonce = String.valueOf(System.currentTimeMillis() * 1000);
        String data = new Gson().toJson(announcement);

        String message = verb + path + nonce + data;

        String sign1 = "WngRRpm2Gly0zz5nZEwbtgCWeEDdzO2QHfPfCsa8d8oCSbg2";
        String sign = null;
        try {
           // sign = encode(message, sign1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        bitmexApiManager.getBitmexAnnouncement( new Callback<Announcement>() {
            @Override
            public void onResponse(@NonNull Call<Announcement> call, @NonNull Response<Announcement> response) {
                    Log.i(TAG, response.body().toString());
            }
            @Override
            public void onFailure(@NonNull Call<Announcement> call, @NonNull Throwable t) {
            }
        });
    }


}
