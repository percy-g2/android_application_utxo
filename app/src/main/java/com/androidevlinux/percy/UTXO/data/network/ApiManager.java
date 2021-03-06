package com.androidevlinux.percy.UTXO.data.network;

import com.androidevlinux.percy.UTXO.data.models.bitfinex.BitfinexPubTickerResponseBean;
import com.androidevlinux.percy.UTXO.data.models.blocktrail.AddressBean;
import com.androidevlinux.percy.UTXO.data.models.blocktrail.TransactionBean;
import com.androidevlinux.percy.UTXO.data.models.changelly.GetCurrenciesResponseBean;
import com.androidevlinux.percy.UTXO.data.models.changelly.GetMinAmountReponseBean;
import com.androidevlinux.percy.UTXO.data.models.changelly.MainBodyBean;
import com.androidevlinux.percy.UTXO.data.models.pocketbits.PocketBitsBean;
import com.androidevlinux.percy.UTXO.data.models.price.ZebPayBean;
import com.androidevlinux.percy.UTXO.data.network.apis.BitfinexApiImpl;
import com.androidevlinux.percy.UTXO.data.network.apis.BitstampApiImpl;
import com.androidevlinux.percy.UTXO.data.network.apis.BlocktrailApiImpl;
import com.androidevlinux.percy.UTXO.data.network.apis.ChangellyApiImpl;
import com.androidevlinux.percy.UTXO.data.network.apis.PocketbitsApiImpl;
import com.androidevlinux.percy.UTXO.data.network.apis.ZebpayApiImpl;
import com.google.gson.JsonObject;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Callback;

public class ApiManager {

    private static ApiManager apiManager;
    private BitfinexApiImpl bitfinexApiImpl;
    private BitstampApiImpl bitstampApiImpl;
    private BlocktrailApiImpl blocktrailApiImpl;
    private ChangellyApiImpl changellyApiImpl;
    private ZebpayApiImpl zebpayApiImpl;
    private PocketbitsApiImpl pocketbitsApiImpl;

    private ApiManager() {
        bitfinexApiImpl = BitfinexApiImpl.getInstance();
        bitstampApiImpl = BitstampApiImpl.getInstance();
        blocktrailApiImpl = BlocktrailApiImpl.getInstance();
        changellyApiImpl = ChangellyApiImpl.getInstance();
        zebpayApiImpl = ZebpayApiImpl.getInstance();
        pocketbitsApiImpl = PocketbitsApiImpl.getInstance();
    }

    public static ApiManager getInstance() {
        if (apiManager == null) {
            apiManager = new ApiManager();
        }
        return apiManager;
    }

    public Observable<BitfinexPubTickerResponseBean> getBitfinexTicker() {
        return bitfinexApiImpl.getBitfinexTicker();
    }

    public void getBitfinexData(String time, String symbol, Callback<ResponseBody> callback) {
        bitfinexApiImpl.getBitfinexData(time, symbol, callback);
    }

    public Observable<JsonObject> getBitstampTicker(){
        return bitstampApiImpl.getBitstampTicker();
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

    public Observable<ZebPayBean> getZebpayTicker() {
        return zebpayApiImpl.getZebpayTicker();
    }

    public Observable<PocketBitsBean> getPocketbitsTicker() {
        return pocketbitsApiImpl.getPocketbitsTicker();
    }
}
