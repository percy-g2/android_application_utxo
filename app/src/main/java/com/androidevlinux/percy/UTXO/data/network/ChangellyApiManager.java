package com.androidevlinux.percy.UTXO.data.network;

import com.androidevlinux.percy.UTXO.data.models.GetCurrenciesResponseBean;
import com.androidevlinux.percy.UTXO.data.models.GetMinAmountReponseBean;
import com.androidevlinux.percy.UTXO.data.models.MainBodyBean;
import com.androidevlinux.percy.UTXO.data.models.TransactionBean;
import com.androidevlinux.percy.UTXO.utils.Constants;
import com.androidevlinux.percy.UTXO.utils.NativeUtils;

import retrofit2.Callback;

/**
 * Created by percy on 14/11/2017.
 */

public class ChangellyApiManager extends BaseApiManager<ChangellyAPI> {
    private ChangellyAPI changellyAPI;
    private static ChangellyApiManager changellyApiManager;
    private ChangellyApiManager(){
        setBaseUrl(NativeUtils.getChangellyBaseUrl());
        changellyAPI= getClient(ChangellyAPI.class);
    }

    public static ChangellyApiManager getInstance(){
        if(changellyApiManager==null){
            changellyApiManager = new ChangellyApiManager();
        }
        return changellyApiManager;
    }

    public void getCurrencies(String sign, MainBodyBean body, Callback<GetCurrenciesResponseBean> callback){
        changellyAPI.getCurrencies(CONTENT_TYPE, Constants.api_key,sign,body).enqueue(callback);
    }
    public void getMinAmount(String sign, MainBodyBean body, Callback<GetMinAmountReponseBean> callback){
        changellyAPI.getMinAmount(CONTENT_TYPE,Constants.api_key,sign,body).enqueue(callback);
    }

    public void createTransaction(String sign, MainBodyBean body, Callback<TransactionBean> callback){
        changellyAPI.createTransaction(CONTENT_TYPE,Constants.api_key,sign,body).enqueue(callback);
    }
}

