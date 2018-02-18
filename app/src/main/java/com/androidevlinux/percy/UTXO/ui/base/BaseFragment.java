package com.androidevlinux.percy.UTXO.ui.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.androidevlinux.percy.UTXO.data.network.ApiManager;

/**
 * Created by percy on 22/11/17.
 */

public class BaseFragment extends Fragment implements BaseView {

    protected ApiManager apiManager;
    protected Activity mActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.mActivity = null;
    }

    @Override
    public void onStop() {
        super.onStop();
    }


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
