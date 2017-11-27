package com.androidevlinux.percy.UTXO.data.network;

import com.androidevlinux.percy.UTXO.data.models.bitmex.Announcement;
import com.androidevlinux.percy.UTXO.data.models.bitmex.UserWallet;
import com.androidevlinux.percy.UTXO.utils.Constants;
import com.androidevlinux.percy.UTXO.utils.NativeUtils;

import retrofit2.Callback;

/**
 * Created by percy on 25/11/17.
 */

public class BitmexApiManager extends BaseApiManager<BitmexAPI> {
    private BitmexAPI bitmexAPI;
    private static BitmexApiManager bitmexApiManager;
    private BitmexApiManager(){
        setBaseUrl(NativeUtils.getBitmexBaseUrl());
        bitmexAPI= getClient(BitmexAPI.class);
    }

    public static BitmexApiManager getInstance(){
        if(bitmexApiManager==null){
            bitmexApiManager = new BitmexApiManager();
        }
        return bitmexApiManager;
    }

    public void getBitmexAnnouncement(Callback<Announcement> callback){
        bitmexAPI.getBitmexAnnouncement("application/json", "application/json").enqueue(callback);
    }

    public void getBitmexUserWallet(String sign, long nounce, Callback<UserWallet> callback){
        bitmexAPI.getBitmexUserWallet(nounce, Constants.bitmex_api_key, sign).enqueue(callback);
    }
}
