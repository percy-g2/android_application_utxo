package com.androidevlinux.percy.UTXO.ui.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v4.app.Fragment;

import com.androidevlinux.percy.UTXO.R;
import com.androidevlinux.percy.UTXO.data.network.BitfinexApiManager;
import com.androidevlinux.percy.UTXO.data.network.ChangellyApiManager;

/**
 * Created by percy on 22/11/17.
 */

public class BaseFragment extends Fragment implements BaseView {

    protected BitfinexApiManager bitfinexApiManager;
    protected ChangellyApiManager changellyApiManager;

    @VisibleForTesting
    public ProgressDialog mProgressDialog;

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        hideProgressDialog();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bitfinexApiManager = BitfinexApiManager.getInstance();
        changellyApiManager = ChangellyApiManager.getInstance();
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
