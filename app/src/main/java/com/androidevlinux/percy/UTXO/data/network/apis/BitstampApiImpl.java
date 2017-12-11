package com.androidevlinux.percy.UTXO.data.network.apis;

import com.androidevlinux.percy.UTXO.utils.NativeUtils;
import com.google.gson.JsonObject;

import retrofit2.Callback;

/**
 * Created by percy on 5/12/17.
 */

public class BitstampApiImpl extends BaseApiManager<BitstampAPI> {

    private static BitstampApiImpl bitstampApiManager;
    private BitstampAPI bitstampAPI;
    private BitstampApiImpl(){
        setBaseUrl(NativeUtils.getbitStampBaseUrl());
        bitstampAPI = getClient(BitstampAPI.class);

    }

    public static BitstampApiImpl getInstance(){
        if(bitstampApiManager==null)
            bitstampApiManager = new BitstampApiImpl();
        return bitstampApiManager;
    }
    public void getBitstampTicker(Callback<JsonObject> callback){
        bitstampAPI.getBitstampTicker().enqueue(callback);
    }
}
