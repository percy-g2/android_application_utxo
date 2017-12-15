package com.androidevlinux.percy.UTXO.data.network;

import com.androidevlinux.percy.UTXO.data.models.bitfinex.BitfinexPubTickerResponseBean;
import com.androidevlinux.percy.UTXO.data.models.blocktrail.AddressBean;
import com.androidevlinux.percy.UTXO.data.models.blocktrail.TransactionBean;
import com.androidevlinux.percy.UTXO.data.models.changelly.GetCurrenciesResponseBean;
import com.androidevlinux.percy.UTXO.data.models.changelly.GetMinAmountReponseBean;
import com.androidevlinux.percy.UTXO.data.models.changelly.MainBodyBean;
import com.androidevlinux.percy.UTXO.data.models.price.ZebPayBean;
import com.androidevlinux.percy.UTXO.data.network.apis.BitfinexApiImpl;
import com.androidevlinux.percy.UTXO.data.network.apis.BitstampApiImpl;
import com.androidevlinux.percy.UTXO.data.network.apis.BlocktrailApiImpl;
import com.androidevlinux.percy.UTXO.data.network.apis.ChangellyApiImpl;
import com.androidevlinux.percy.UTXO.data.network.apis.ZebpayApiImpl;
import com.google.gson.JsonObject;

import retrofit2.Callback;

public class ApiManager {

    private static ApiManager apiManager;
    private BitfinexApiImpl bitfinexApiImpl;
    private BitstampApiImpl bitstampApiImpl;
    private BlocktrailApiImpl blocktrailApiImpl;
    private ChangellyApiImpl changellyApiImpl;
    private ZebpayApiImpl zebpayApiImpl;

    private ApiManager() {
        bitfinexApiImpl = BitfinexApiImpl.getInstance();
        bitstampApiImpl = BitstampApiImpl.getInstance();
        blocktrailApiImpl = BlocktrailApiImpl.getInstance();
        changellyApiImpl = ChangellyApiImpl.getInstance();
        zebpayApiImpl = ZebpayApiImpl.getInstance();
    }

    public static ApiManager getInstance() {
        if (apiManager == null) {
            apiManager = new ApiManager();
        }
        return apiManager;
    }

    public void getBitfinexPubTicker(Callback<BitfinexPubTickerResponseBean> callback) {
        bitfinexApiImpl.getBitfinexPubTicker(callback);
    }

    public void getBitstampTicker(Callback<JsonObject> callback) {
        bitstampApiImpl.getBitstampTicker(callback);
    }


    public void getBlockTrailAddressData(String query, String data, Callback<AddressBean> callback) {
        blocktrailApiImpl.getBlockTrailAddressData(query, data, callback);
    }

    public void getBlockTrailBlockData(String query, String data, Callback<JsonObject> callback) {
        blocktrailApiImpl.getBlockTrailBlockData(query, data, callback);
    }

    public void getBlockTrailTransactionData(String query, String data, Callback<TransactionBean> callback) {
        blocktrailApiImpl.getBlockTrailTransactionData(query, data, callback);
    }

    public void getCurrencies(String sign, MainBodyBean body, Callback<GetCurrenciesResponseBean> callback) {
        changellyApiImpl.getCurrencies(sign, body, callback);
    }

    public void getMinAmount(String sign, MainBodyBean body, Callback<GetMinAmountReponseBean> callback) {
        changellyApiImpl.getMinAmount(sign, body, callback);
    }

    public void createTransaction(String sign, MainBodyBean body, Callback<com.androidevlinux.percy.UTXO.data.models.changelly.TransactionBean> callback) {
        changellyApiImpl.createTransaction(sign, body, callback);
    }

    public void getZebpayTicker(Callback<ZebPayBean> callback) {
        zebpayApiImpl.getZebpayTicker(callback);
    }

}
