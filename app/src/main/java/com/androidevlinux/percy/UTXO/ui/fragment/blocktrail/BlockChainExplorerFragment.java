package com.androidevlinux.percy.UTXO.ui.fragment.blocktrail;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidevlinux.percy.UTXO.R;
import com.androidevlinux.percy.UTXO.data.models.blocktrail.AddressBean;
import com.androidevlinux.percy.UTXO.data.models.blocktrail.TransactionBean;
import com.androidevlinux.percy.UTXO.ui.adapter.BlockChainExplorerAddressAdapter;
import com.androidevlinux.percy.UTXO.ui.adapter.BlockChainExplorerBlockAdapter;
import com.androidevlinux.percy.UTXO.ui.adapter.BlockChainExplorerTransactionAdapter;
import com.androidevlinux.percy.UTXO.ui.base.BaseFragment;
import com.androidevlinux.percy.UTXO.utils.CustomProgressDialog;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by percy on 27/11/17.
 */

public class BlockChainExplorerFragment extends BaseFragment {

    @BindView(R.id.spinner_explorer)
    AppCompatSpinner spinnerExplorer;
    @BindView(R.id.edt_data_to_explore)
    AppCompatEditText edtDataToExplore;
    @BindView(R.id.btn_get_data)
    AppCompatButton btnGetData;
    @BindView(R.id.response_data_recyclerView)
    RecyclerView response_data_recyclerView;
    Unbinder unbinder;
    @BindView(R.id.btn_scan_qr)
    AppCompatButton btnScanQr;
    //qr code scanner object
    private IntentIntegrator qrScan;
    private static final int PERMISSION_REQUEST_CODE = 1;
    protected static String TAG = "BlockChainExplorerFragment";
    protected ArrayList<AddressBean> addressBeanArrayList;
    protected ArrayList<TransactionBean> transactionBeanArrayList;
    private Activity mActivity;

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
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        assert inflater != null;
        View view = inflater.inflate(R.layout.block_chain_explorer_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;

    }

    @Override
    public void onViewCreated(@Nullable View view, @Nullable Bundle savedInstanceState) {
        assert view != null;
        super.onViewCreated(view, savedInstanceState);
        TextView Title = mActivity.findViewById(R.id.txtTitle);
        Title.setText(getResources().getString(R.string.block_chain_explorer));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        response_data_recyclerView.setLayoutManager(linearLayoutManager);
        addressBeanArrayList = new ArrayList<>();
        transactionBeanArrayList = new ArrayList<>();
        spinnerExplorer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Object item = adapterView.getItemAtPosition(i);
                Log.i(TAG, item.toString());
                if (item.toString().equalsIgnoreCase("Address")) {
                    edtDataToExplore.setHint(R.string.enter_address);
                    edtDataToExplore.setInputType(InputType.TYPE_CLASS_TEXT);
                } else if (item.toString().equalsIgnoreCase("Block")) {
                    edtDataToExplore.setHint(R.string.enter_block);
                    edtDataToExplore.setInputType(InputType.TYPE_CLASS_NUMBER);
                } else if (item.toString().equalsIgnoreCase("Transaction")) {
                    edtDataToExplore.setInputType(InputType.TYPE_CLASS_TEXT);
                    edtDataToExplore.setHint(R.string.enter_transaction);
                }
                edtDataToExplore.getEditableText().clear();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //intializing scan object
        qrScan = new IntentIntegrator(getActivity());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.btn_get_data, R.id.btn_scan_qr})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_get_data:
                if (!edtDataToExplore.getText().toString().isEmpty()) {
                    if (spinnerExplorer.getSelectedItem().toString().equalsIgnoreCase("Address")) {
                        loadAddressData(spinnerExplorer.getSelectedItem().toString().toLowerCase(), edtDataToExplore.getText().toString());
                    } else if (spinnerExplorer.getSelectedItem().toString().equalsIgnoreCase("Block")) {
                        loadBlockData(spinnerExplorer.getSelectedItem().toString().toLowerCase(), edtDataToExplore.getText().toString());
                    } else if (spinnerExplorer.getSelectedItem().toString().equalsIgnoreCase("Transaction")) {
                        loadTransactionData(spinnerExplorer.getSelectedItem().toString().toLowerCase(), edtDataToExplore.getText().toString());
                    }
                } else {
                    Toasty.warning(mActivity, spinnerExplorer.getSelectedItem().toString() + " Input Is Empty!", Toast.LENGTH_SHORT, true).show();
                }
                break;
            case R.id.btn_scan_qr:
                PermissionCheck();
                break;
        }
    }

    void loadAddressData(String query, String data) {
        final Dialog dialogToSaveData = CustomProgressDialog.showCustomProgressDialog(mActivity, "Please Wait Loading Data ...");
        apiManager.getBlockTrailAddressData(query, data, new Callback<AddressBean>() {
            @Override
            public void onResponse(@NonNull Call<AddressBean> call, @NonNull Response<AddressBean> response) {
                if (response.body() != null) {
                    Log.i(TAG, new Gson().toJson(response.body()));
                    addressBeanArrayList.add(response.body());
                    BlockChainExplorerAddressAdapter blockChainExplorerAddressAdapter = new BlockChainExplorerAddressAdapter(addressBeanArrayList, getActivity());
                    response_data_recyclerView.setAdapter(blockChainExplorerAddressAdapter);
                } else {
                    Toasty.error(mActivity, "Check Your Input", Toast.LENGTH_SHORT, true).show();
                }
                if (dialogToSaveData != null) {
                    CustomProgressDialog.dismissCustomProgressDialog(dialogToSaveData);
                }
            }

            @Override
            public void onFailure(@NonNull Call<AddressBean> call, @NonNull Throwable t) {
                if (dialogToSaveData != null) {
                    CustomProgressDialog.dismissCustomProgressDialog(dialogToSaveData);
                }
            }
        });
    }

    void loadBlockData(String query, String data) {
        final Dialog dialogToSaveData = CustomProgressDialog.showCustomProgressDialog(mActivity, "Please Wait Loading Data ...");
        apiManager.getBlockTrailBlockData(query, data, new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if (response.body() != null) {
                    Log.i(TAG, response.body().toString());
                    ArrayList<JsonObject> jsonObjectArrayList = new ArrayList<>();
                    jsonObjectArrayList.add(response.body());
                    BlockChainExplorerBlockAdapter blockChainExplorerBlockAdapter = new BlockChainExplorerBlockAdapter(jsonObjectArrayList, getActivity());
                    response_data_recyclerView.setAdapter(blockChainExplorerBlockAdapter);
                } else {
                    Toasty.error(mActivity, "Check Your Input", Toast.LENGTH_SHORT, true).show();
                }
                if (dialogToSaveData != null) {
                    CustomProgressDialog.dismissCustomProgressDialog(dialogToSaveData);
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                if (dialogToSaveData != null) {
                    CustomProgressDialog.dismissCustomProgressDialog(dialogToSaveData);
                }
            }
        });
    }

    void loadTransactionData(String query, String data) {
        final Dialog dialogToSaveData = CustomProgressDialog.showCustomProgressDialog(mActivity, "Please Wait Loading Data ...");
        apiManager.getBlockTrailTransactionData(query, data, new Callback<TransactionBean>() {
            @Override
            public void onResponse(@NonNull Call<TransactionBean> call, @NonNull Response<TransactionBean> response) {
                if (response.body() != null) {
                    Log.i(TAG, new Gson().toJson(response.body()));
                    transactionBeanArrayList.add(response.body());
                    BlockChainExplorerTransactionAdapter blockChainExplorerTransactionAdapter = new BlockChainExplorerTransactionAdapter(transactionBeanArrayList, getActivity());
                    response_data_recyclerView.setAdapter(blockChainExplorerTransactionAdapter);
                } else {
                    Toasty.error(mActivity, "Check Your Input", Toast.LENGTH_SHORT, true).show();
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
        ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(mActivity,
                            "Permission accepted", Toast.LENGTH_LONG).show();
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
                edtDataToExplore.setText(result.getContents());
                Toast.makeText(mActivity, result.getContents(), Toast.LENGTH_LONG).show();
            }
        }
    }
}
