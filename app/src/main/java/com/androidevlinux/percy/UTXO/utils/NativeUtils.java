package com.androidevlinux.percy.UTXO.utils;

/**
 * Created by percy on 22/11/17.
 */

public class NativeUtils {

    static {
        System.loadLibrary("utxo-jni");
    }

    public static native String getApiKey();

    public static native String getSecretKey();

    public static native String getBitfinexBaseUrl();
    public static native String getChangellyBaseUrl();

}
