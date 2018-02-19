package com.androidevlinux.percy.UTXO.ui.fragment.changelly;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.androidevlinux.percy.UTXO.R;
import com.androidevlinux.percy.UTXO.data.models.changelly.GetCurrenciesResponseBean;
import com.androidevlinux.percy.UTXO.data.models.changelly.GetMinAmountReponseBean;
import com.androidevlinux.percy.UTXO.data.models.changelly.MainBodyBean;
import com.androidevlinux.percy.UTXO.data.models.changelly.ParamsBean;
import com.androidevlinux.percy.UTXO.ui.base.BaseFragment;
import com.androidevlinux.percy.UTXO.utils.Constants;
import com.androidevlinux.percy.UTXO.utils.CustomProgressDialog;
import com.androidevlinux.percy.UTXO.utils.Utils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

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

public class MinAmountFragment extends BaseFragment {

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

    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        assert inflater != null;
        View view = inflater.inflate(R.layout.min_amount_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;

    }

    @Override
    public void onViewCreated(@Nullable View view, @Nullable Bundle savedInstanceState) {
        assert view != null;
        super.onViewCreated(view, savedInstanceState);
        TextView Title = mActivity.findViewById(R.id.txtTitle);
        Title.setText(getResources().getString(R.string.min_amount_check));
        TextView txtInfo = view.findViewById(R.id.txt_info);
        txtInfo.setText(R.string.min_amount_info);
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
        FloatingActionButton fab = mActivity.findViewById(R.id.fab);
        FloatingActionButton refreshFab = mActivity.findViewById(R.id.refresh_fab);
        if (!fab.isShown()) {
            fab.show();
            refreshFab.hide();
        }
    }

    private void MinAmount(String From, String To) {
        MainBodyBean mainBodyBean = new MainBodyBean();
        mainBodyBean.setId(1);
        mainBodyBean.setJsonrpc("2.0");
        mainBodyBean.setMethod("getMinAmount");
        ParamsBean params = new ParamsBean();
        params.setFrom(From);
        params.setTo(To);
        mainBodyBean.setParams(params);
        String sign = null;
        try {
            sign = Utils.hmacDigest(new Gson().toJson(mainBodyBean), Constants.secret_key);
        } catch (Exception e) {
            e.printStackTrace();
        }

        final Dialog dialogToSaveData = CustomProgressDialog.showCustomProgressDialog(mActivity, "Please Wait Fetching Data ...");
        apiManager.getMinAmount(sign, mainBodyBean, new Callback<GetMinAmountReponseBean>() {
            @Override
            public void onResponse(@NonNull Call<GetMinAmountReponseBean> call, @NonNull Response<GetMinAmountReponseBean> response) {
                if (response.body() != null) {
                    if (response.body().getError() != null) {
                        Toasty.error(mActivity, response.body().getError().getMessage(), Toast.LENGTH_SHORT, true).show();
                    } else {
                        Toasty.success(mActivity, response.body().getResult(), Toast.LENGTH_SHORT, true).show();
                        txtMinAmount.setText(response.body().getResult());
                    }
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

        final Dialog dialogToSaveData = CustomProgressDialog.showCustomProgressDialog(mActivity, "Please Wait ...");
        apiManager.getCurrencies(sign, mainBodyBean, new Callback<GetCurrenciesResponseBean>() {
            @Override
            public void onResponse(@NonNull Call<GetCurrenciesResponseBean> call, @NonNull Response<GetCurrenciesResponseBean> response) {
                if (response.body()!=null) {
                    currenciesStringList = response.body().getResult();
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

    @OnClick(R.id.btn_get_amount)
    public void onClick() {
        if (Utils.isConnectingToInternet(getActivity())) {
            if (spinnerFrom.getSelectedItem() !=null && spinnerTo.getSelectedItem() !=null && spinnerFrom.getSelectedItem().toString() != null && spinnerFrom.getSelectedItem().toString() != null) {
                MinAmount(spinnerFrom.getSelectedItem().toString(), spinnerTo.getSelectedItem().toString());
            } else {
                Toasty.warning(mActivity, "Empty Fields Please Check", Toast.LENGTH_SHORT, true).show();
            }
        } else {
            Toasty.warning(mActivity, "No Internet", Toast.LENGTH_SHORT, true).show();
        }
    }
}
