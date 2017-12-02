package com.androidevlinux.percy.UTXO.utils;

/**
 * Created by percy on 22/11/17.
 */

public class NativeUtils {

    static {
        System.loadLibrary("utxo-jni");
    }
    public static native String getBitfinexBaseUrl();
    public static native String getBlocktrailBaseUrl();
    public static native String getBlocktrailApiKey();
    public static native String getCoinSecureBaseUrl();
    public static native String getChangellyApiKey();
    public static native String getChangellySecretKey();
    public static native String getChangellyBaseUrl();

}
