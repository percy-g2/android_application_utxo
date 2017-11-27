package com.androidevlinux.percy.UTXO.data.network;

import com.androidevlinux.percy.UTXO.data.models.bitmex.Announcement;
import com.androidevlinux.percy.UTXO.data.models.bitmex.UserWallet;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

/**
 * Created by percy on 25/11/17.
 */

public interface BitmexAPI {
    @GET("/api/v1/announcement/")
    Call<Announcement> getBitmexAnnouncement(@Header("Content-Type") String strContentType, @Header("Accept") String strAccept);

    @GET("/api/v1/user/wallet")
    Call<UserWallet> getBitmexUserWallet( @Header("api-nonce") long apin, @Header("api-key") String api, @Header("api-signature") String apis);
}
