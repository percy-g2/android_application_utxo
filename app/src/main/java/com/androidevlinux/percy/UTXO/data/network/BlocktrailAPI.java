package com.androidevlinux.percy.UTXO.data.network;

import com.androidevlinux.percy.UTXO.data.models.blocktrail.AddressBean;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by percy on 27/11/17.
 */

public interface BlocktrailAPI {
    @GET("/v1/btc/{query}/{data}")
    Call<AddressBean> getBlockTrailData(@Path(value = "query", encoded = true) String query, @Path(value = "data", encoded = true) String data, @Query("api_key") String api_key);
}
