package com.androidevlinux.percy.UTXO.data.network;

import com.androidevlinux.percy.UTXO.utils.NativeUtils;
import com.google.gson.JsonObject;

import retrofit2.Callback;

/**
 * Created by percy on 2/12/17.
 */

public class CoinsecureApiManager extends BaseApiManager<CoinsecureAPI> {

    private static CoinsecureApiManager coinsecureApiManager;
    private CoinsecureAPI coinsecureAPI;
    private CoinsecureApiManager(){
        setBaseUrl(NativeUtils.getCoinSecureBaseUrl());
        coinsecureAPI = getClient(CoinsecureAPI.class);

    }

    public static CoinsecureApiManager getInstance(){
        if(coinsecureApiManager==null)
            coinsecureApiManager = new CoinsecureApiManager();
        return coinsecureApiManager;
    }
    public void getCoinsecureTicker(Callback<JsonObject> callback){
        coinsecureAPI.getCoinsecureTicker().enqueue(callback);
    }
}
