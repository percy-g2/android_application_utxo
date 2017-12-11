package com.androidevlinux.percy.UTXO.data.network.apis;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by percy on 5/12/17.
 */

public interface BitstampAPI {
    @GET("/api/ticker/")
    Call<JsonObject> getBitstampTicker();
}
