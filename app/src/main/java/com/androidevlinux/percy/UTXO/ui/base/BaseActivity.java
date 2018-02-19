package com.androidevlinux.percy.UTXO.ui.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.androidevlinux.percy.UTXO.data.network.ApiManager;

public class BaseActivity extends AppCompatActivity implements BaseView {

    protected ApiManager apiManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiManager = ApiManager.getInstance();
    }

    @Override
    public void displaySnack(String text) {

    }

    @Override
    public void displaySnack(int textId) {

    }

    @Override
    public void displaySnackWithAction() {

    }

    @Override
    public void showPreloader() {

    }

    @Override
    public void hidePreloader() {

    }

    @Override
    public void resetState() {

    }

    @Override
    public void setupViews(Bundle bundle) {

    }

    @Override
    public void showError(String error) {

    }

    @Override
    public void showError(int errorResId) {

    }

    @Override
    public void showDialogOKCancel(String text, Runnable positive, Runnable negative) {

    }

    @Override
    public void showDialogOKCancel(int textResId, Runnable positive, Runnable negative) {

    }

    @Override
    public Activity getAcitivtyAsContext() {
        return null;
    }

    @Override
    public void showToast(String text) {

    }

    @Override
    public void showToast(String text, int duration) {

    }

    @Override
    public void showSuccessMsg() {

    }

    @Override
    public boolean checkOnline() {
        return false;
    }

    @Override
    public boolean isShowingPreloader() {
        return false;
    }
}
