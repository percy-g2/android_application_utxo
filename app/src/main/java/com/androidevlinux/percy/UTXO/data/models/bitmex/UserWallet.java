package com.androidevlinux.percy.UTXO.data.models.bitmex;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by percy on 26/11/17.
 */

public class UserWallet implements Serializable
{

    @SerializedName("account")
    @Expose
    private Integer account;
    @SerializedName("currency")
    @Expose
    private String currency;
    @SerializedName("prevDeposited")
    @Expose
    private Integer prevDeposited;
    @SerializedName("prevWithdrawn")
    @Expose
    private Integer prevWithdrawn;
    @SerializedName("prevTransferIn")
    @Expose
    private Integer prevTransferIn;
    @SerializedName("prevTransferOut")
    @Expose
    private Integer prevTransferOut;
    @SerializedName("prevAmount")
    @Expose
    private Integer prevAmount;
    @SerializedName("prevTimestamp")
    @Expose
    private String prevTimestamp;
    @SerializedName("deltaDeposited")
    @Expose
    private Integer deltaDeposited;
    @SerializedName("deltaWithdrawn")
    @Expose
    private Integer deltaWithdrawn;
    @SerializedName("deltaTransferIn")
    @Expose
    private Integer deltaTransferIn;
    @SerializedName("deltaTransferOut")
    @Expose
    private Integer deltaTransferOut;
    @SerializedName("deltaAmount")
    @Expose
    private Integer deltaAmount;
    @SerializedName("deposited")
    @Expose
    private Integer deposited;
    @SerializedName("withdrawn")
    @Expose
    private Integer withdrawn;
    @SerializedName("transferIn")
    @Expose
    private Integer transferIn;
    @SerializedName("transferOut")
    @Expose
    private Integer transferOut;
    @SerializedName("amount")
    @Expose
    private Integer amount;
    @SerializedName("pendingCredit")
    @Expose
    private Integer pendingCredit;
    @SerializedName("pendingDebit")
    @Expose
    private Integer pendingDebit;
    @SerializedName("confirmedDebit")
    @Expose
    private Integer confirmedDebit;
    @SerializedName("timestamp")
    @Expose
    private String timestamp;
    @SerializedName("addr")
    @Expose
    private String addr;
    @SerializedName("script")
    @Expose
    private String script;
    @SerializedName("withdrawalLock")
    @Expose
    private List<String> withdrawalLock = null;
    private final static long serialVersionUID = 1684172534052037848L;

    public Integer getAccount() {
        return account;
    }

    public void setAccount(Integer account) {
        this.account = account;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Integer getPrevDeposited() {
        return prevDeposited;
    }

    public void setPrevDeposited(Integer prevDeposited) {
        this.prevDeposited = prevDeposited;
    }

    public Integer getPrevWithdrawn() {
        return prevWithdrawn;
    }

    public void setPrevWithdrawn(Integer prevWithdrawn) {
        this.prevWithdrawn = prevWithdrawn;
    }

    public Integer getPrevTransferIn() {
        return prevTransferIn;
    }

    public void setPrevTransferIn(Integer prevTransferIn) {
        this.prevTransferIn = prevTransferIn;
    }

    public Integer getPrevTransferOut() {
        return prevTransferOut;
    }

    public void setPrevTransferOut(Integer prevTransferOut) {
        this.prevTransferOut = prevTransferOut;
    }

    public Integer getPrevAmount() {
        return prevAmount;
    }

    public void setPrevAmount(Integer prevAmount) {
        this.prevAmount = prevAmount;
    }

    public String getPrevTimestamp() {
        return prevTimestamp;
    }

    public void setPrevTimestamp(String prevTimestamp) {
        this.prevTimestamp = prevTimestamp;
    }

    public Integer getDeltaDeposited() {
        return deltaDeposited;
    }

    public void setDeltaDeposited(Integer deltaDeposited) {
        this.deltaDeposited = deltaDeposited;
    }

    public Integer getDeltaWithdrawn() {
        return deltaWithdrawn;
    }

    public void setDeltaWithdrawn(Integer deltaWithdrawn) {
        this.deltaWithdrawn = deltaWithdrawn;
    }

    public Integer getDeltaTransferIn() {
        return deltaTransferIn;
    }

    public void setDeltaTransferIn(Integer deltaTransferIn) {
        this.deltaTransferIn = deltaTransferIn;
    }

    public Integer getDeltaTransferOut() {
        return deltaTransferOut;
    }

    public void setDeltaTransferOut(Integer deltaTransferOut) {
        this.deltaTransferOut = deltaTransferOut;
    }

    public Integer getDeltaAmount() {
        return deltaAmount;
    }

    public void setDeltaAmount(Integer deltaAmount) {
        this.deltaAmount = deltaAmount;
    }

    public Integer getDeposited() {
        return deposited;
    }

    public void setDeposited(Integer deposited) {
        this.deposited = deposited;
    }

    public Integer getWithdrawn() {
        return withdrawn;
    }

    public void setWithdrawn(Integer withdrawn) {
        this.withdrawn = withdrawn;
    }

    public Integer getTransferIn() {
        return transferIn;
    }

    public void setTransferIn(Integer transferIn) {
        this.transferIn = transferIn;
    }

    public Integer getTransferOut() {
        return transferOut;
    }

    public void setTransferOut(Integer transferOut) {
        this.transferOut = transferOut;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getPendingCredit() {
        return pendingCredit;
    }

    public void setPendingCredit(Integer pendingCredit) {
        this.pendingCredit = pendingCredit;
    }

    public Integer getPendingDebit() {
        return pendingDebit;
    }

    public void setPendingDebit(Integer pendingDebit) {
        this.pendingDebit = pendingDebit;
    }

    public Integer getConfirmedDebit() {
        return confirmedDebit;
    }

    public void setConfirmedDebit(Integer confirmedDebit) {
        this.confirmedDebit = confirmedDebit;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }

    public List<String> getWithdrawalLock() {
        return withdrawalLock;
    }

    public void setWithdrawalLock(List<String> withdrawalLock) {
        this.withdrawalLock = withdrawalLock;
    }

}

