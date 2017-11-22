package com.androidevlinux.percy.UTXO.data.network;

import com.androidevlinux.percy.UTXO.data.models.BitfinexPubTickerResponseBean;
import com.androidevlinux.percy.UTXO.utils.NativeUtils;

import retrofit2.Callback;

/**
 * Created by percy on 18/11/17.
 */

public class BitfinexApiManager extends BaseApiManager<BitfinexAPI> {

    private static BitfinexApiManager bitfinexApiManager;
    private BitfinexAPI bitfinexAPI;
    private BitfinexApiManager(){
        setBaseUrl(NativeUtils.getBitfinexBaseUrl());
        bitfinexAPI = getClient(BitfinexAPI.class);

    }

    public static BitfinexApiManager getInstance(){
        if(bitfinexApiManager==null)
            bitfinexApiManager = new BitfinexApiManager();
        return bitfinexApiManager;
    }
    public void getBitfinexPubTicker(Callback<BitfinexPubTickerResponseBean> callback){
        bitfinexAPI.getBitfinexPubTicker().enqueue(callback);
    }
}
