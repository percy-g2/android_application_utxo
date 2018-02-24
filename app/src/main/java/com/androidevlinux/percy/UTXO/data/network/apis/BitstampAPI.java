package com.androidevlinux.percy.UTXO.data.network.apis;

import com.google.gson.JsonObject;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * Created by percy on 5/12/17.
 */

public interface BitstampAPI {

    @GET("/api/ticker/")
    Observable<JsonObject> getBitstampTicker();
}
