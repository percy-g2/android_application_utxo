package com.androidevlinux.percy.UTXO.ui.fragment.changelly;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.androidevlinux.percy.UTXO.R;
import com.androidevlinux.percy.UTXO.data.models.GetCurrenciesResponseBean;
import com.androidevlinux.percy.UTXO.data.models.MainBodyBean;
import com.androidevlinux.percy.UTXO.data.models.ParamsBean;
import com.androidevlinux.percy.UTXO.data.models.TransactionBean;
import com.androidevlinux.percy.UTXO.ui.base.BaseFragment;
import com.androidevlinux.percy.UTXO.utils.Constants;
import com.androidevlinux.percy.UTXO.utils.CustomProgressDialog;
import com.androidevlinux.percy.UTXO.utils.Utils;
import com.google.gson.Gson;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.CLIPBOARD_SERVICE;

/**
 * Created by percy on 15/11/2017.
 */

public class CreateTransactionFragment extends BaseFragment {
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
    @BindView(R.id.txt_pay_in_address)
    AppCompatTextView txtPayInAddress;
    @BindView(R.id.btn_scan_qr)
    AppCompatButton btnScanQr;
    //qr code scanner object
    private IntentIntegrator qrScan;
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
        registerForContextMenu(txtPayInAddress);
        //intializing scan object
        qrScan = new IntentIntegrator(getActivity());
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

        final Dialog dialogToSaveData = CustomProgressDialog.showCustomProgressDialog(getActivity(), "Please Wait Creating Transaction ...");
        changellyApiManager.createTransaction(sign, testbean, new Callback<TransactionBean>() {
            @Override
            public void onResponse(@NonNull Call<TransactionBean> call, @NonNull Response<TransactionBean> response) {
                if (response.body() != null) {
                    if (response.body().getError() != null) {
                        Toast.makeText(getActivity(), response.body().getError().getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        txtMinAmount.setText(response.body().getResult().getId());
                        txtPayInAddress.setText(response.body().getResult().getPayinAddress());
                    }
                    Log.i("Transaction", response.body().toString());
                }
                if (dialogToSaveData != null) {
                    CustomProgressDialog.dismissCustomProgressDialog(dialogToSaveData);
                }
            }

            @Override
            public void onFailure(@NonNull Call<TransactionBean> call, @NonNull Throwable t) {
                if (dialogToSaveData != null) {
                    CustomProgressDialog.dismissCustomProgressDialog(dialogToSaveData);
                }
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Pay In Address", txtPayInAddress.getText().toString());
        if (clipboard != null) {
            clipboard.setPrimaryClip(clip);
        }
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

        final Dialog dialogToSaveData = CustomProgressDialog.showCustomProgressDialog(getActivity(), "Please Wait Loading Currencies ...");
        changellyApiManager.getCurrencies(sign, testbean, new Callback<GetCurrenciesResponseBean>() {
            @Override
            public void onResponse(@NonNull Call<GetCurrenciesResponseBean> call, @NonNull Response<GetCurrenciesResponseBean> response) {
                if (response.body() != null) {
                    currenciesStringList = response.body().getResult();
                    ArrayAdapter<String> karant_adapter = new ArrayAdapter<>(getActivity(),
                            android.R.layout.simple_spinner_item, currenciesStringList);
                    karant_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerFrom.setAdapter(karant_adapter);
                    spinnerTo.setAdapter(karant_adapter);
                }
                if (dialogToSaveData != null) {
                    CustomProgressDialog.dismissCustomProgressDialog(dialogToSaveData);
                }
                if (response.code() == 401) {
                    Toast.makeText(getActivity(), "Unauthorized! Please Check Your Keys", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<GetCurrenciesResponseBean> call, @NonNull Throwable t) {
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

    @OnClick({R.id.btn_get_amount, R.id.btn_scan_qr})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_get_amount:
                if (Utils.isConnectingToInternet(getActivity())) {
                    if (spinnerFrom.getSelectedItem() != null && spinnerTo.getSelectedItem() != null && spinnerFrom.getSelectedItem().toString() != null && spinnerFrom.getSelectedItem().toString() != null && !edtAmount.getText().toString().isEmpty() && !edtUserPayOutAddress.getText().toString().isEmpty()) {
                        MinAmount(spinnerFrom.getSelectedItem().toString(), spinnerTo.getSelectedItem().toString(), edtAmount.getText().toString(), edtUserPayOutAddress.getText().toString());
                    } else {
                        Toast.makeText(getActivity(), "Empty Fields Please Check", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "No Internet", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.btn_scan_qr:
                qrScan.initiateScan();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                edtUserPayOutAddress.setText(result.getContents());
                Toast.makeText(getActivity(), result.getContents(), Toast.LENGTH_LONG).show();
            }
        }
    }
}


