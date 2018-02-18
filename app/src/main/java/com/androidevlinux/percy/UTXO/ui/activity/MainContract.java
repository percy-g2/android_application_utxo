package com.androidevlinux.percy.UTXO.ui.activity;

import com.androidevlinux.percy.UTXO.ui.base.BasePresenter;
import com.androidevlinux.percy.UTXO.ui.base.BaseView;

public interface MainContract {

     interface MainView extends BaseView {

        void showMinAmountFragment();
        void showExchangeAmountFragment();
        void showCreateTransactionFragment();
        void showGetStatusFragment();
        void showPriceCheckFragment();
        void showBlockChainExplorerFragment();
        void showSettingsFragment();
        void showBitfinexCandleChartFragment();
    }

    interface MainPresenter<V extends  BaseView> extends BasePresenter{

    }

}
