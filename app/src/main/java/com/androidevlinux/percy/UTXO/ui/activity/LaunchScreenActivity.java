package com.androidevlinux.percy.UTXO.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.androidevlinux.percy.UTXO.R;
import com.androidevlinux.percy.UTXO.ui.base.BaseActivity;

/**
 * Created by percy on 30/12/17.
 */

public class LaunchScreenActivity extends BaseActivity {
    private static final int SPLASH_TIME = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch_screen);
        new BackgroundTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @SuppressLint("StaticFieldLeak")
    private class BackgroundTask extends AsyncTask<String, String, String> {
        Intent intent;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            intent = new Intent(LaunchScreenActivity.this, MainActivity.class);
        }

        @Override
        protected String doInBackground(String... value) {
            /*
            * Use this method to load background
            * data that your app needs.
            * */
            try {
                Thread.sleep(SPLASH_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String value) {
            super.onPostExecute(value);
            startActivity(intent);
            finish();
        }
    }
}
