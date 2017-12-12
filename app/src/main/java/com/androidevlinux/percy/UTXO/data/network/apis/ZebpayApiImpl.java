package com.androidevlinux.percy.UTXO.data.network.apis;

import com.androidevlinux.percy.UTXO.data.models.price.ZebPayBean;
import com.androidevlinux.percy.UTXO.utils.NativeUtils;

import retrofit2.Callback;

/**
 * Created by percy on 3/12/17.
 */

public class ZebpayApiImpl extends AbstractBaseApi<ZebpayAPI> {

    private static ZebpayApiImpl zebpayApiManager;
    private ZebpayAPI zebpayAPI;
    private ZebpayApiImpl(){
        setBaseUrl(NativeUtils.getZebpayBaseUrl());
        zebpayAPI = getClient(ZebpayAPI.class);

    }

    public static ZebpayApiImpl getInstance(){
        if(zebpayApiManager==null)
            zebpayApiManager = new ZebpayApiImpl();
        return zebpayApiManager;
    }
    public void getZebpayTicker(Callback<ZebPayBean> callback){
        zebpayAPI.getZebpayTicker().enqueue(callback);
    }
}