package com.androidevlinux.percy.UTXO.data.network;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by percy on 2/12/17.
 */

public interface CoinsecureAPI {
    @GET("/v1/exchange/ticker/")
    Call<JsonObject> getCoinsecureTicker();
}
