package com.androidevlinux.percy.UTXO.ui.fragment.changelly;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.androidevlinux.percy.UTXO.R;
import com.androidevlinux.percy.UTXO.data.models.changelly.GetMinAmountReponseBean;
import com.androidevlinux.percy.UTXO.data.models.changelly.MainBodyBean;
import com.androidevlinux.percy.UTXO.data.models.changelly.ParamsBean;
import com.androidevlinux.percy.UTXO.ui.base.BaseFragment;
import com.androidevlinux.percy.UTXO.utils.Constants;
import com.androidevlinux.percy.UTXO.utils.CustomProgressDialog;
import com.androidevlinux.percy.UTXO.utils.Utils;
import com.google.gson.Gson;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by percy on 15/11/2017.
 */

public class GetStatusFragment  extends BaseFragment {
    @BindView(R.id.btn_get_amount)
    AppCompatButton btnGetAmount;
    @BindView(R.id.txt_min_amount)
    AppCompatTextView txtMinAmount;
    Unbinder unbinder;
    @BindView(R.id.txt_info)
    AppCompatTextView txtInfo;
    @BindView(R.id.edtTransactionId)
    AppCompatEditText edtTransactionId;

    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        assert inflater != null;
        View view = inflater.inflate(R.layout.get_status_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;

    }

    @Override
    public void onViewCreated(@Nullable View view, @Nullable Bundle savedInstanceState) {
        assert view != null;
        super.onViewCreated(view, savedInstanceState);
        TextView Title = mActivity.findViewById(R.id.txtTitle);
        Title.setText(getResources().getString(R.string.exchange_amount));
        txtInfo.setText(R.string.get_status_info);
        FloatingActionButton fab = mActivity.findViewById(R.id.fab);
        FloatingActionButton refreshFab = mActivity.findViewById(R.id.refresh_fab);
        if (!fab.isShown()) {
            fab.show();
            refreshFab.hide();
        }
    }

    private void MinAmount(String strTransactionId) {
        MainBodyBean mainBodyBean = new MainBodyBean();
        mainBodyBean.setId(1);
        mainBodyBean.setJsonrpc("2.0");
        mainBodyBean.setMethod("getStatus");
        ParamsBean params = new ParamsBean();
        params.setId(strTransactionId);
        mainBodyBean.setParams(params);
        String sign = null;
        try {
            sign = Utils.hmacDigest(new Gson().toJson(mainBodyBean), Constants.secret_key);
        } catch (Exception e) {
            e.printStackTrace();
        }

        final Dialog dialogToSaveData =  CustomProgressDialog.showCustomProgressDialog(mActivity, "Please Wait ...");

        apiManager.getMinAmount(sign, mainBodyBean, new Callback<GetMinAmountReponseBean>() {
            @Override
            public void onResponse(@NonNull Call<GetMinAmountReponseBean> call, @NonNull Response<GetMinAmountReponseBean> response) {
                if (response.body() != null) {
                    if (Objects.requireNonNull(response.body()).getError() != null) {
                        Toasty.error(mActivity, Objects.requireNonNull(response.body()).getError().getMessage(), Toast.LENGTH_SHORT, true).show();
                    } else {
                        Toasty.success(mActivity, Objects.requireNonNull(response.body()).getResult(), Toast.LENGTH_SHORT, true).show();
                        txtMinAmount.setText(Objects.requireNonNull(response.body()).getResult());
                    }
                }
                if (response.code() == 401) {
                    Toasty.error(mActivity, "Unauthorized! Please Check Your Keys", Toast.LENGTH_SHORT, true).show();
                }
                if (dialogToSaveData != null) {
                    CustomProgressDialog.dismissCustomProgressDialog(dialogToSaveData);
                }
            }

            @Override
            public void onFailure(@NonNull Call<GetMinAmountReponseBean> call, @NonNull Throwable t) {
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
        if (Utils.isConnectingToInternet(getActivity())) {
            if (!edtTransactionId.getText().toString().isEmpty()) {
                MinAmount(edtTransactionId.getText().toString());
            } else {
                Toasty.warning(mActivity, "Empty Fields Please Check", Toast.LENGTH_SHORT, true).show();
            }
        } else {
            Toasty.warning(mActivity, "No Internet", Toast.LENGTH_SHORT, true).show();
        }
    }
}


