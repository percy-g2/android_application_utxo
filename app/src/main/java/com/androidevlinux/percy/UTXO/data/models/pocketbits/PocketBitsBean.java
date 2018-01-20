package com.androidevlinux.percy.UTXO.data.models.pocketbits;

/**
 * Created by percy on 20/1/18.
 */


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PocketBitsBean implements Serializable
{

    @SerializedName("buy")
    @Expose
    private Integer buy;
    @SerializedName("sell")
    @Expose
    private Integer sell;
    @SerializedName("currency")
    @Expose
    private String currency;
    private final static long serialVersionUID = 5465714077736737751L;

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

}
