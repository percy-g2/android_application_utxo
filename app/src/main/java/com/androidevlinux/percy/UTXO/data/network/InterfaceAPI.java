package com.androidevlinux.percy.UTXO.data.network;

import com.androidevlinux.percy.UTXO.data.models.BitfinexPubTickerResponseBean;
import com.androidevlinux.percy.UTXO.data.models.GetCurrenciesResponseBean;
import com.androidevlinux.percy.UTXO.data.models.GetMinAmountReponseBean;
import com.androidevlinux.percy.UTXO.data.models.MainBodyBean;
import com.androidevlinux.percy.UTXO.data.models.TransactionBean;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by percy on 14/11/2017.
 */

public interface InterfaceAPI {
    @GET("/v1/pubticker/btcusd/")
    Call<BitfinexPubTickerResponseBean> getBitfinexPubTicker();

    @POST("/")
    Call<GetCurrenciesResponseBean> getCurrencies(@Header("Content-Type") String content_type, @Header("api-key") String api_key, @Header("sign") String sign, @Body MainBodyBean p);

    @POST("/")
    Call<GetMinAmountReponseBean> getMinAmount(@Header("Content-Type") String content_type, @Header("api-key") String api_key, @Header("sign") String sign, @Body MainBodyBean p);

    @POST("/")
    Call<TransactionBean> createTransaction(@Header("Content-Type") String content_type, @Header("api-key") String api_key, @Header("sign") String sign, @Body MainBodyBean p);
}
