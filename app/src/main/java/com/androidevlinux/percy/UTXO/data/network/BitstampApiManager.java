package com.androidevlinux.percy.UTXO.data.network;

import com.androidevlinux.percy.UTXO.utils.NativeUtils;
import com.google.gson.JsonObject;

import retrofit2.Callback;

/**
 * Created by percy on 5/12/17.
 */

public class BitstampApiManager  extends BaseApiManager<BitstampAPI> {

    private static BitstampApiManager bitstampApiManager;
    private BitstampAPI bitstampAPI;
    private BitstampApiManager(){
        setBaseUrl(NativeUtils.getbitStampBaseUrl());
        bitstampAPI = getClient(BitstampAPI.class);

    }

    public static BitstampApiManager getInstance(){
        if(bitstampApiManager==null)
            bitstampApiManager = new BitstampApiManager();
        return bitstampApiManager;
    }
    public void getBitstampTicker(Callback<JsonObject> callback){
        bitstampAPI.getBitstampTicker().enqueue(callback);
    }
}
