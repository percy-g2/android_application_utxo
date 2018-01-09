package com.androidevlinux.percy.UTXO.ui.activity;

import com.androidevlinux.percy.UTXO.ui.base.BaseView;

public interface MainView extends BaseView {

    void showMinAmountFragment();
    void showExchangeAmountFragment();
    void showCreateTransactionFragment();
    void showGetStatusFragment();
    void showPriceCheckFragment();
    void showBitfinexChartFragment();
    void showBlockChainExplorerFragment();
    void showSettingsFragment();
}
