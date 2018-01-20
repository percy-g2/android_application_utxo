package com.androidevlinux.percy.UTXO.data.models.price;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by percy on 3/12/17.
 */

public class ZebPayBean implements Serializable {

    @SerializedName("market")
    @Expose
    private Integer market;
    @SerializedName("buy")
    @Expose
    private Integer buy;
    @SerializedName("sell")
    @Expose
    private Integer sell;
    @SerializedName("currency")
    @Expose
    private String currency;
    @SerializedName("volume")
    @Expose
    private Double volume;
    @SerializedName("virtualCurrency")
    @Expose
    private String virtualCurrency;
    private final static long serialVersionUID = -6887817422339566777L;

    public Integer getMarket() {
        return market;
    }

    public void setMarket(Integer market) {
        this.market = market;
    }

    public Integer getBuy() {
        return buy;
    }

    public void setBuy(Integer buy) {
        this.buy = buy;
    }

    public Integer getSell() {
        return sell;
    }

    public void setSell(Integer sell) {
        this.sell = sell;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Double getVolume() {
        return volume;
    }

    public void setVolume(Double volume) {
        this.volume = volume;
    }

    public String getVirtualCurrency() {
        return virtualCurrency;
    }

    public void setVirtualCurrency(String virtualCurrency) {
        this.virtualCurrency = virtualCurrency;
    }

}