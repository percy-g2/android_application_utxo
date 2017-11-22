package com.androidevlinux.percy.UTXO.data.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by percy on 22/11/17.
 */


public abstract class BaseApiManager<T> {


    private static final long connectTimeOut = 120;
    private static final long writeTimeOut = 120;
    private static final long readTimeOut = 120;

    private final Gson gson = new GsonBuilder()
            .setLenient()
            .create();
    private final HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
    private String baseUrl;
    private Retrofit retrofit = null;

    private OkHttpClient clientOkHttp;

    protected  final String CONTENT_TYPE="application/json";

    protected void setBaseUrl(String url) {
        baseUrl = url;
    }

    protected final T getClient(Class<T> t) {
        if (retrofit == null) {
            // set your desired log level
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            clientOkHttp = new OkHttpClient.Builder()
                    .connectTimeout(connectTimeOut, TimeUnit.SECONDS)
                    .writeTimeout(writeTimeOut, TimeUnit.SECONDS)
                    .readTimeout(readTimeOut, TimeUnit.SECONDS).addInterceptor(logging)
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Interceptor.Chain chain) throws IOException {
                            Request.Builder requestBuilder = chain.request().newBuilder();
                            requestBuilder.header("Content-Type", "application/json");
                            return chain.proceed(requestBuilder.build());
                        }
                    })
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))

                    .client(clientOkHttp)
                    .build();
        }
        return retrofit.create(t);
    }
}

