package com.androidevlinux.percy.UTXO.data.network.apis;

import com.androidevlinux.percy.UTXO.data.models.bitfinex.BitfinexPubTickerResponseBean;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by percy on 22/11/17.
 */

public interface BitfinexAPI {

    @GET("/v1/pubticker/btcusd/")
    Call<BitfinexPubTickerResponseBean> getBitfinexPubTicker();
}
