package com.androidevlinux.percy.UTXO.data.network.apis;

import com.androidevlinux.percy.UTXO.data.models.price.ZebPayBean;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by percy on 3/12/17.
 */

public interface ZebpayAPI {
    @GET("/api/v1/market/ticker-new/BTC/INR")
    Call<ZebPayBean> getZebpayTicker();
}
