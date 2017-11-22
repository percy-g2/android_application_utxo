//
// Created by mantis on 11/20/17.
//

#include <jni.h>

JNIEXPORT jstring JNICALL
Java_com_androidevlinux_percy_UTXO_utils_NativeUtils_getApiKey(JNIEnv *env, jclass type) {

    // TODO

    char * apiKey = "";
    return (*env)->NewStringUTF(env, apiKey);
}

JNIEXPORT jstring JNICALL
Java_com_androidevlinux_percy_UTXO_utils_NativeUtils_getSecretKey(JNIEnv *env, jclass type) {

    // TODO


    char * secretKey = "";
    return (*env)->NewStringUTF(env, secretKey);
}

JNIEXPORT jstring JNICALL
Java_com_androidevlinux_percy_UTXO_utils_NativeUtils_getBitfinexBaseUrl(JNIEnv *env, jclass type) {

    // TODO

    char * baseUrl ="https://api.bitfinex.com/";
    return (*env)->NewStringUTF(env, baseUrl);
}

JNIEXPORT jstring JNICALL
Java_com_androidevlinux_percy_UTXO_utils_NativeUtils_getChangellyBaseUrl(JNIEnv *env, jclass type) {

    // TODO

    char * baseUrl = "https://api.changelly.com";
    return (*env)->NewStringUTF(env, baseUrl);
}
