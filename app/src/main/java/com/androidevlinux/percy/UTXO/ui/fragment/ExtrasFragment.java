package com.androidevlinux.percy.UTXO.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidevlinux.percy.UTXO.R;
import com.androidevlinux.percy.UTXO.ui.base.BaseFragment;
import com.androidevlinux.percy.UTXO.ui.fragment.bitfinex.PriceCheckFragment;
import com.androidevlinux.percy.UTXO.ui.fragment.blocktrail.BlockChainExplorerFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by percy on 18/11/17.
 */

public class ExtrasFragment extends BaseFragment {
    Unbinder unbinder;
    @BindView(R.id.btn_btc_price)
    AppCompatButton btn_btc_price;
    @BindView(R.id.btn_block_chain_explorer)
    AppCompatButton btnBlockChainExplorer;
    @BindView(R.id.btn_news)
    AppCompatButton btnNews;

    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        assert inflater != null;
        View view = inflater.inflate(R.layout.extras_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;

    }

    @Override
    public void onViewCreated(@Nullable View view, @Nullable Bundle savedInstanceState) {
        assert view != null;
        super.onViewCreated(view, savedInstanceState);
        TextView Title = getActivity().findViewById(R.id.txtTitle);
        Title.setText(getResources().getString(R.string.extras));
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.btn_btc_price, R.id.btn_block_chain_explorer, R.id.btn_news})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_btc_price:
                Fragment price_check = new PriceCheckFragment();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.container, price_check);
                ft.addToBackStack(null).commit();
                break;
            case R.id.btn_block_chain_explorer:
                Fragment btn_block_chain_explorer = new BlockChainExplorerFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container, btn_block_chain_explorer);
                fragmentTransaction.addToBackStack(null).commit();
                break;
            case R.id.btn_news:
                break;
        }
    }
}
