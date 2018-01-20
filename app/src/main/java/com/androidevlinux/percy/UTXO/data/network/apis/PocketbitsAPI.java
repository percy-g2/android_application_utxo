package com.androidevlinux.percy.UTXO.data.network.apis;

import com.androidevlinux.percy.UTXO.data.models.pocketbits.PocketBitsBean;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by percy on 20/1/18.
 */

public interface PocketbitsAPI {
    @GET("/api/ticker/")
    Call<PocketBitsBean> getPocketbitsTicker();
}
