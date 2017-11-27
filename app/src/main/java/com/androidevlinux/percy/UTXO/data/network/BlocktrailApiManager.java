package com.androidevlinux.percy.UTXO.data.network;

import com.androidevlinux.percy.UTXO.data.models.blocktrail.AddressBean;
import com.androidevlinux.percy.UTXO.utils.NativeUtils;

import retrofit2.Callback;

/**
 * Created by percy on 27/11/17.
 */

public class BlocktrailApiManager extends BaseApiManager<BlocktrailAPI> {
    private BlocktrailAPI blocktrailAPI;
    private static BlocktrailApiManager blocktrailApiManager;
    private BlocktrailApiManager(){
        setBaseUrl(NativeUtils.getBlocktrailBaseUrl());
        blocktrailAPI = getClient(BlocktrailAPI.class);
    }

    public static BlocktrailApiManager getInstance(){
        if(blocktrailApiManager==null){
            blocktrailApiManager = new BlocktrailApiManager();
        }
        return blocktrailApiManager;
    }

    public void getBlockTrailAddressData(String query, String data, Callback<AddressBean> callback){
        blocktrailAPI.getBlockTrailData(query, data, NativeUtils.getBlocktrailApiKey()).enqueue(callback);
    }
}
