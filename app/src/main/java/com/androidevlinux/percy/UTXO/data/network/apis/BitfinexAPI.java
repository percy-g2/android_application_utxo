package com.androidevlinux.percy.UTXO.data.network.apis;

import com.androidevlinux.percy.UTXO.data.models.bitfinex.BitfinexPubTickerResponseBean;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by percy on 22/11/17.
 */

public interface BitfinexAPI {

    @GET("/v1/pubticker/btcusd/")
    Call<BitfinexPubTickerResponseBean> getBitfinexPubTicker();

    @GET("/v2/candles/trade:1m:tBTCUSD/hist/")
    Call<ResponseBody> getBitfinexData();
}
