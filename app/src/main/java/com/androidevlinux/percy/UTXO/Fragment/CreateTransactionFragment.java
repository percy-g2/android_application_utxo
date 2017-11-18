package com.androidevlinux.percy.UTXO.Fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.androidevlinux.percy.UTXO.Beans.GetCurrenciesResponseBean;
import com.androidevlinux.percy.UTXO.Beans.MainBodyBean;
import com.androidevlinux.percy.UTXO.Beans.ParamsBean;
import com.androidevlinux.percy.UTXO.Beans.TransactionBean;
import com.androidevlinux.percy.UTXO.R;
import com.androidevlinux.percy.UTXO.Retrofit.InterfaceAPI;
import com.androidevlinux.percy.UTXO.Retrofit.RetrofitBaseAPi;
import com.androidevlinux.percy.UTXO.Utils.Constants;
import com.androidevlinux.percy.UTXO.Utils.CustomProgressDialog;
import com.androidevlinux.percy.UTXO.Utils.Utils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by percy on 15/11/2017.
 */

public class CreateTransactionFragment extends Fragment {
    @BindView(R.id.spinner_from)
    AppCompatSpinner spinnerFrom;
    @BindView(R.id.spinner_to)
    AppCompatSpinner spinnerTo;
    @BindView(R.id.btn_get_amount)
    AppCompatButton btnGetAmount;
    @BindView(R.id.txt_min_amount)
    AppCompatTextView txtMinAmount;
    Unbinder unbinder;
    List<String> currenciesStringList;
    @BindView(R.id.edtAmount)
    AppCompatEditText edtAmount;
    @BindView(R.id.txt_info)
    AppCompatTextView txtInfo;
    @BindView(R.id.edtUserPayOutAddress)
    AppCompatEditText edtUserPayOutAddress;

    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        assert inflater != null;
        View view = inflater.inflate(R.layout.create_transaction_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;

    }

    @Override
    public void onViewCreated(@Nullable View view, @Nullable Bundle savedInstanceState) {
        assert view != null;
        super.onViewCreated(view, savedInstanceState);
        TextView Title = getActivity().findViewById(R.id.txtTitle);
        Title.setText(getResources().getString(R.string.create_transaction));
        txtInfo.setText(R.string.transaction_id);
        currenciesStringList = new ArrayList<>();
        Init();
    }

    private void MinAmount(String From, String To, String amount, String address) {
        MainBodyBean testbean = new MainBodyBean();
        testbean.setId(1);
        testbean.setJsonrpc("2.0");
        testbean.setMethod("createTransaction");
        ParamsBean params = new ParamsBean();
        params.setFrom(From);
        params.setTo(To);
        params.setAmount(amount);
        params.setAddress(address);
        testbean.setParams(params);
        String sign = null;
        try {
            sign = Utils.hmacDigest(new Gson().toJson(testbean), Constants.secret_key);
        } catch (Exception e) {
            e.printStackTrace();
        }


        InterfaceAPI service = RetrofitBaseAPi.getClient().create(InterfaceAPI.class);

        Log.i("DownloadFlagSuccess", sign);

        Call<TransactionBean> call = service.createTransaction("application/json", Constants.api_key, sign, testbean);

        final Dialog dialogToSaveData =  CustomProgressDialog.showCustomProgressDialog(getActivity(), "Please Wait Creating Transaction ...");
        call.enqueue(new Callback<TransactionBean>() {
            @Override
            public void onResponse(@NonNull Call<TransactionBean> call, @NonNull Response<TransactionBean> response) {
                Log.i("DownloadFlagSuccess", response.body().toString());
                txtMinAmount.setText(response.body().getResult().getId());
                if (dialogToSaveData != null) {
                    CustomProgressDialog.dismissCustomProgressDialog(dialogToSaveData);
                }
            }

            @Override
            public void onFailure(@NonNull Call<TransactionBean> call, @NonNull Throwable t) {
                Log.i("DownloadFlagSuccess", t.getMessage());
                if (dialogToSaveData != null) {
                    CustomProgressDialog.dismissCustomProgressDialog(dialogToSaveData);
                }
            }
        });
    }

    private void Init() {
        MainBodyBean testbean = new MainBodyBean();
        testbean.setId(1);
        testbean.setJsonrpc("2.0");
        testbean.setMethod("getCurrencies");
        ParamsBean params = new ParamsBean();
        testbean.setParams(params);
        String sign = null;
        try {
            sign = Utils.hmacDigest(new Gson().toJson(testbean), Constants.secret_key);
        } catch (Exception e) {
            e.printStackTrace();
        }


        InterfaceAPI service = RetrofitBaseAPi.getClient().create(InterfaceAPI.class);

        Log.i("DownloadFlagSuccess", sign);

        Call<GetCurrenciesResponseBean> call = service.getCurrencies("application/json", Constants.api_key, sign, testbean);

        final Dialog dialogToSaveData =  CustomProgressDialog.showCustomProgressDialog(getActivity(), "Please Wait Loading Currencies ...");
        call.enqueue(new Callback<GetCurrenciesResponseBean>() {
            @Override
            public void onResponse(@NonNull Call<GetCurrenciesResponseBean> call, @NonNull Response<GetCurrenciesResponseBean> response) {
                Log.i("DownloadFlagSuccess", response.body().getResult().toString());
                currenciesStringList = response.body().getResult();
                ArrayAdapter<String> karant_adapter = new ArrayAdapter<>(getActivity(),
                        android.R.layout.simple_spinner_item, currenciesStringList);
                karant_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerFrom.setAdapter(karant_adapter);
                spinnerTo.setAdapter(karant_adapter);
                if (dialogToSaveData != null) {
                    CustomProgressDialog.dismissCustomProgressDialog(dialogToSaveData);
                }
            }

            @Override
            public void onFailure(@NonNull Call<GetCurrenciesResponseBean> call, @NonNull Throwable t) {
                Log.i("DownloadFlagSuccess", t.getMessage());
                if (dialogToSaveData != null) {
                    CustomProgressDialog.dismissCustomProgressDialog(dialogToSaveData);
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.btn_get_amount)
    public void onClick() {
        MinAmount(spinnerFrom.getSelectedItem().toString(), spinnerTo.getSelectedItem().toString(), edtAmount.getText().toString(), edtUserPayOutAddress.getText().toString());
    }
}


