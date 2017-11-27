package com.androidevlinux.percy.UTXO.data.models.blocktrail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class BlockBean implements Serializable
{

    @SerializedName("hash")
    @Expose
    private String hash;
    @SerializedName("height")
    @Expose
    private Integer height;
    @SerializedName("block_time")
    @Expose
    private String blockTime;
    @SerializedName("difficulty")
    @Expose
    private Integer difficulty;
    @SerializedName("merkleroot")
    @Expose
    private String merkleroot;
    @SerializedName("is_orphan")
    @Expose
    private Boolean isOrphan;
    @SerializedName("prev_block")
    @Expose
    private String prevBlock;
    @SerializedName("next_block")
    @Expose
    private String nextBlock;
    @SerializedName("byte_size")
    @Expose
    private Integer byteSize;
    @SerializedName("confirmations")
    @Expose
    private Integer confirmations;
    @SerializedName("transactions")
    @Expose
    private Integer transactions;
    @SerializedName("value")
    @Expose
    private Integer value;
    @SerializedName("miningpool_name")
    @Expose
    private String miningpoolName;
    @SerializedName("miningpool_url")
    @Expose
    private String miningpoolUrl;
    @SerializedName("miningpool_slug")
    @Expose
    private String miningpoolSlug;
    private final static long serialVersionUID = -1128019367607514045L;

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public String getBlockTime() {
        return blockTime;
    }

    public void setBlockTime(String blockTime) {
        this.blockTime = blockTime;
    }

    public Integer getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Integer difficulty) {
        this.difficulty = difficulty;
    }

    public String getMerkleroot() {
        return merkleroot;
    }

    public void setMerkleroot(String merkleroot) {
        this.merkleroot = merkleroot;
    }

    public Boolean getIsOrphan() {
        return isOrphan;
    }

    public void setIsOrphan(Boolean isOrphan) {
        this.isOrphan = isOrphan;
    }

    public String getPrevBlock() {
        return prevBlock;
    }

    public void setPrevBlock(String prevBlock) {
        this.prevBlock = prevBlock;
    }

    public String getNextBlock() {
        return nextBlock;
    }

    public void setNextBlock(String nextBlock) {
        this.nextBlock = nextBlock;
    }

    public Integer getByteSize() {
        return byteSize;
    }

    public void setByteSize(Integer byteSize) {
        this.byteSize = byteSize;
    }

    public Integer getConfirmations() {
        return confirmations;
    }

    public void setConfirmations(Integer confirmations) {
        this.confirmations = confirmations;
    }

    public Integer getTransactions() {
        return transactions;
    }

    public void setTransactions(Integer transactions) {
        this.transactions = transactions;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getMiningpoolName() {
        return miningpoolName;
    }

    public void setMiningpoolName(String miningpoolName) {
        this.miningpoolName = miningpoolName;
    }

    public String getMiningpoolUrl() {
        return miningpoolUrl;
    }

    public void setMiningpoolUrl(String miningpoolUrl) {
        this.miningpoolUrl = miningpoolUrl;
    }

    public String getMiningpoolSlug() {
        return miningpoolSlug;
    }

    public void setMiningpoolSlug(String miningpoolSlug) {
        this.miningpoolSlug = miningpoolSlug;
    }

}