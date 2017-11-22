package com.androidevlinux.percy.UTXO.ui.base;

/**
 * Created by percy on 22/11/17.
 */

import android.app.Activity;
import android.os.Bundle;

public interface BaseView<T> {

    void displaySnack(String text);

    void displaySnack(int textId);

    void displaySnackWithAction();

    void showPreloader();

    void hidePreloader();

    void resetState();

    void setupViews(Bundle bundle);

    void showError(String error);

    void showError(int errorResId);

    void showDialogOKCancel(String text, Runnable positive, Runnable negative);

    void showDialogOKCancel(int textResId, Runnable positive, Runnable negative);

    Activity getAcitivtyAsContext();

    void showToast(String text);

    void showToast(String text, int duration);

    void showSuccessMsg();

    boolean checkOnline();

    boolean isShowingPreloader();
}
