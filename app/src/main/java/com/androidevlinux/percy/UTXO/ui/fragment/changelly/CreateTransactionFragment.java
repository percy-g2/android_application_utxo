package com.androidevlinux.percy.UTXO.ui.fragment.changelly;

import android.Manifest;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
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
import com.androidevlinux.percy.UTXO.data.models.changelly.GetCurrenciesResponseBean;
import com.androidevlinux.percy.UTXO.data.models.changelly.MainBodyBean;
import com.androidevlinux.percy.UTXO.data.models.changelly.ParamsBean;
import com.androidevlinux.percy.UTXO.data.models.changelly.TransactionBean;
import com.androidevlinux.percy.UTXO.ui.base.BaseFragment;
import com.androidevlinux.percy.UTXO.utils.Constants;
import com.androidevlinux.percy.UTXO.utils.CustomProgressDialog;
import com.androidevlinux.percy.UTXO.utils.Utils;
import com.google.gson.Gson;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import es.dmoral.toasty.Toasty;
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
    private static final int PERMISSION_REQUEST_CODE = 1;

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
        TextView Title = mActivity.findViewById(R.id.txtTitle);
        Title.setText(getResources().getString(R.string.create_transaction));
        txtInfo.setText(R.string.transaction_id);
        currenciesStringList = new ArrayList<>();
        if (Constants.currenciesStringList == null || Constants.currenciesStringList.size() == 0) {
            Init();
        } else {
            ArrayAdapter<String> karant_adapter = new ArrayAdapter<>(mActivity,
                    android.R.layout.simple_spinner_item, Constants.currenciesStringList);
            karant_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerFrom.setAdapter(karant_adapter);
            spinnerTo.setAdapter(karant_adapter);
        }
        registerForContextMenu(txtPayInAddress);
        //intializing scan object
        qrScan = new IntentIntegrator(getActivity());
    }

    private void MinAmount(String From, String To, String amount, String address) {
        MainBodyBean mainBodyBean = new MainBodyBean();
        mainBodyBean.setId(1);
        mainBodyBean.setJsonrpc("2.0");
        mainBodyBean.setMethod("createTransaction");
        ParamsBean params = new ParamsBean();
        params.setFrom(From);
        params.setTo(To);
        params.setAmount(amount);
        params.setAddress(address);
        mainBodyBean.setParams(params);
        String sign = null;
        try {
            sign = Utils.hmacDigest(new Gson().toJson(mainBodyBean), Constants.secret_key);
        } catch (Exception e) {
            e.printStackTrace();
        }

        final Dialog dialogToSaveData = CustomProgressDialog.showCustomProgressDialog(mActivity, "Please Wait ...");
        apiManager.createTransaction(sign, mainBodyBean, new Callback<TransactionBean>() {
            @Override
            public void onResponse(@NonNull Call<TransactionBean> call, @NonNull Response<TransactionBean> response) {
                if (response.body() != null) {
                    if (Objects.requireNonNull(response.body()).getError() != null) {
                        Toasty.error(mActivity, Objects.requireNonNull(response.body()).getError().getMessage(), Toast.LENGTH_SHORT, true).show();
                    } else {
                        txtMinAmount.setText(Objects.requireNonNull(response.body()).getResult().getId());
                        txtPayInAddress.setText(Objects.requireNonNull(response.body()).getResult().getPayinAddress());
                    }
                   // Log.i("Transaction", response.body().toString());
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
        ClipboardManager clipboard = (ClipboardManager) mActivity.getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Pay In Address", txtPayInAddress.getText().toString());
        if (clipboard != null) {
            clipboard.setPrimaryClip(clip);
        }
    }

    private void Init() {
        MainBodyBean mainBodyBean = new MainBodyBean();
        mainBodyBean.setId(1);
        mainBodyBean.setJsonrpc("2.0");
        mainBodyBean.setMethod("getCurrencies");
        ParamsBean params = new ParamsBean();
        mainBodyBean.setParams(params);
        String sign = null;
        try {
            sign = Utils.hmacDigest(new Gson().toJson(mainBodyBean), Constants.secret_key);
        } catch (Exception e) {
            e.printStackTrace();
        }

        final Dialog dialogToSaveData = CustomProgressDialog.showCustomProgressDialog(mActivity, "Please Wait Loading Currencies ...");
        apiManager.getCurrencies(sign, mainBodyBean, new Callback<GetCurrenciesResponseBean>() {
            @Override
            public void onResponse(@NonNull Call<GetCurrenciesResponseBean> call, @NonNull Response<GetCurrenciesResponseBean> response) {
                if (response.body() != null) {
                    currenciesStringList = Objects.requireNonNull(response.body()).getResult();
                    Constants.currenciesStringList = currenciesStringList;
                    ArrayAdapter<String> karant_adapter = new ArrayAdapter<>(mActivity,
                            android.R.layout.simple_spinner_item, currenciesStringList);
                    karant_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerFrom.setAdapter(karant_adapter);
                    spinnerTo.setAdapter(karant_adapter);
                }
                if (dialogToSaveData != null) {
                    CustomProgressDialog.dismissCustomProgressDialog(dialogToSaveData);
                }
                if (response.code() == 401) {
                    Toasty.error(mActivity, "Unauthorized! Please Check Your Keys", Toast.LENGTH_SHORT, true).show();
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
                if (Utils.isConnectingToInternet(mActivity)) {
                    if (spinnerFrom.getSelectedItem() != null && spinnerTo.getSelectedItem() != null && spinnerFrom.getSelectedItem().toString() != null && spinnerFrom.getSelectedItem().toString() != null && !edtAmount.getText().toString().isEmpty() && !edtUserPayOutAddress.getText().toString().isEmpty()) {
                        MinAmount(spinnerFrom.getSelectedItem().toString(), spinnerTo.getSelectedItem().toString(), edtAmount.getText().toString(), edtUserPayOutAddress.getText().toString());
                    } else {
                        Toasty.warning(mActivity, "Empty Fields Please Check", Toast.LENGTH_SHORT, true).show();
                    }
                } else {
                    Toasty.warning(mActivity, "No Internet", Toast.LENGTH_SHORT, true).show();
                }
                break;
            case R.id.btn_scan_qr:
                PermissionCheck();
                break;
        }
    }

    void PermissionCheck() {
        if (Build.VERSION.SDK_INT >= 23) {
            //Check whether your app has access to the READ permission//
            if (checkPermission()) {
                //If your app has access to the device’s camera, then print the following message to Android Studio’s Logcat//
                qrScan.initiateScan();
                Log.i("permission", "Permission already granted.");
            } else {
                //If your app does not have permission to access camera, then call requestPermission//
                requestPermission();
            }
        } else {
            qrScan.initiateScan();
        }
    }

    private boolean checkPermission() {
        //Check for CAMERA access, using ContextCompat.checkSelfPermission()//
        int result = ContextCompat.checkSelfPermission(mActivity, Manifest.permission.CAMERA);
        //If the app does have this permission, then return true//
        if (result == PackageManager.PERMISSION_GRANTED) {
            Log.i(mActivity.getClass().getName(), "Permission Granted");
            return true;
        } else {
            requestPermission();
            return false;
        }
    }

    private void requestPermission() {
        requestPermissions(new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(mActivity,
                            "Permission accepted", Toast.LENGTH_LONG).show();
                    qrScan.initiateScan();
                } else {
                    Toast.makeText(mActivity,
                            "Permission denied", Toast.LENGTH_LONG).show();
                }
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
                Toast.makeText(mActivity, result.getContents(), Toast.LENGTH_LONG).show();
            }
        }
    }
}


