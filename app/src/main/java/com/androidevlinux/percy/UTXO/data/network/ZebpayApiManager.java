package com.androidevlinux.percy.UTXO.data.network;

import com.androidevlinux.percy.UTXO.data.models.price.ZebPayBean;
import com.androidevlinux.percy.UTXO.utils.NativeUtils;

import retrofit2.Callback;

/**
 * Created by percy on 3/12/17.
 */

public class ZebpayApiManager extends BaseApiManager<ZebpayAPI> {

    private static ZebpayApiManager zebpayApiManager;
    private ZebpayAPI zebpayAPI;
    private ZebpayApiManager(){
        setBaseUrl(NativeUtils.getZebpayBaseUrl());
        zebpayAPI = getClient(ZebpayAPI.class);

    }

    public static ZebpayApiManager getInstance(){
        if(zebpayApiManager==null)
            zebpayApiManager = new ZebpayApiManager();
        return zebpayApiManager;
    }
    public void getZebpayTicker(Callback<ZebPayBean> callback){
        zebpayAPI.getZebpayTicker().enqueue(callback);
    }
}