package com.androidevlinux.percy.UTXO.data.network.apis;

import com.androidevlinux.percy.UTXO.data.models.bitfinex.BitfinexPubTickerResponseBean;
import com.androidevlinux.percy.UTXO.utils.NativeUtils;

import retrofit2.Callback;

/**
 * Created by percy on 18/11/17.
 */

public class BitfinexApiImpl extends BaseApiManager<BitfinexAPI> {

    private static BitfinexApiImpl bitfinexApiManager;
    private BitfinexAPI bitfinexAPI;
    private BitfinexApiImpl(){
        setBaseUrl(NativeUtils.getBitfinexBaseUrl());
        bitfinexAPI = getClient(BitfinexAPI.class);

    }

    public static BitfinexApiImpl getInstance(){
        if(bitfinexApiManager==null)
            bitfinexApiManager = new BitfinexApiImpl();
        return bitfinexApiManager;
    }
    public void getBitfinexPubTicker(Callback<BitfinexPubTickerResponseBean> callback){
        bitfinexAPI.getBitfinexPubTicker().enqueue(callback);
    }
}
