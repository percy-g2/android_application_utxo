package com.androidevlinux.percy.UTXO.ui.fragment.blocktrail;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidevlinux.percy.UTXO.R;
import com.androidevlinux.percy.UTXO.data.models.blocktrail.AddressBean;
import com.androidevlinux.percy.UTXO.data.models.blocktrail.BlockBean;
import com.androidevlinux.percy.UTXO.data.models.blocktrail.TransactionBean;
import com.androidevlinux.percy.UTXO.ui.base.BaseFragment;
import com.androidevlinux.percy.UTXO.utils.CustomProgressDialog;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
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
    @BindView(R.id.txt_response_data)
    AppCompatTextView txtResponseData;
    Unbinder unbinder;
    protected static String TAG = "BlockChainExplorerFragment";
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
        TextView Title = getActivity().findViewById(R.id.txtTitle);
        Title.setText(getResources().getString(R.string.block_chain_explorer));

        spinnerExplorer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Object item = adapterView.getItemAtPosition(i);
                Log.i(TAG, item.toString());
                if (item.toString().equalsIgnoreCase("Address")) {
                    edtDataToExplore.setHint(R.string.enter_address);
                } else if (item.toString().equalsIgnoreCase("Block")) {
                    edtDataToExplore.setHint(R.string.enter_block);
                } else if (item.toString().equalsIgnoreCase("Transaction")) {
                    edtDataToExplore.setHint(R.string.enter_transaction);
                }
                edtDataToExplore.getEditableText().clear();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.btn_get_data)
    public void onClick() {
        if (!edtDataToExplore.getText().toString().isEmpty()) {
            if (spinnerExplorer.getSelectedItem().toString().equalsIgnoreCase("Address")) {
                loadAddressData(spinnerExplorer.getSelectedItem().toString().toLowerCase(), edtDataToExplore.getText().toString());
            } else if (spinnerExplorer.getSelectedItem().toString().equalsIgnoreCase("Block")) {
                loadBlockData(spinnerExplorer.getSelectedItem().toString().toLowerCase(), edtDataToExplore.getText().toString());
            } else if (spinnerExplorer.getSelectedItem().toString().equalsIgnoreCase("Transaction")) {
                loadTransactionData(spinnerExplorer.getSelectedItem().toString().toLowerCase(), edtDataToExplore.getText().toString());
            }
        } else {
            Toast.makeText(getActivity(), spinnerExplorer.getSelectedItem().toString() + " Input Is Empty!", Toast.LENGTH_SHORT).show();
        }
    }

    void loadAddressData(String query, String data) {
        final Dialog dialogToSaveData = CustomProgressDialog.showCustomProgressDialog(getActivity(), "Please Wait Loading Data ...");
        blocktrailApiManager.getBlockTrailAddressData(query,data,new Callback<AddressBean>() {
            @Override
            public void onResponse(@NonNull Call<AddressBean> call, @NonNull Response<AddressBean> response) {
                if (response.body()!=null) {
                    txtResponseData.setText(new Gson().toJson(response.body()));
                } else {
                    Toast.makeText(getActivity(), "Check Your Input", Toast.LENGTH_SHORT).show();
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
        final Dialog dialogToSaveData = CustomProgressDialog.showCustomProgressDialog(getActivity(), "Please Wait Loading Data ...");
        blocktrailApiManager.getBlockTrailBlockData(query,data,new Callback<BlockBean>() {
            @Override
            public void onResponse(@NonNull Call<BlockBean> call, @NonNull Response<BlockBean> response) {
                if (response.body()!=null) {
                    txtResponseData.setText(new Gson().toJson(response.body()));
                } else {
                    Toast.makeText(getActivity(), "Check Your Input", Toast.LENGTH_SHORT).show();
                }
                if (dialogToSaveData != null) {
                    CustomProgressDialog.dismissCustomProgressDialog(dialogToSaveData);
                }
            }

            @Override
            public void onFailure(@NonNull Call<BlockBean> call, @NonNull Throwable t) {
                if (dialogToSaveData != null) {
                    CustomProgressDialog.dismissCustomProgressDialog(dialogToSaveData);
                }
            }
        });
    }

    void loadTransactionData(String query, String data) {
        final Dialog dialogToSaveData = CustomProgressDialog.showCustomProgressDialog(getActivity(), "Please Wait Loading Data ...");
        blocktrailApiManager.getBlockTrailTransactionData(query,data,new Callback<TransactionBean>() {
            @Override
            public void onResponse(@NonNull Call<TransactionBean> call, @NonNull Response<TransactionBean> response) {
                if (response.body()!=null) {
                    txtResponseData.setText(new Gson().toJson(response.body()));
                } else {
                    Toast.makeText(getActivity(), "Check Your Input", Toast.LENGTH_SHORT).show();
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
}
