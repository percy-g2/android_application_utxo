package com.androidevlinux.percy.UTXO.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;
import android.widget.TextView;

import com.androidevlinux.percy.UTXO.R;

/**
 * Created by percy on 19/11/17.
 */

public class SettingsFragment extends PreferenceFragmentCompat {

    private Activity mActivity;

    String TAG = "Settings Fragment";
    public static String refresh_btc_price_button_key = "enable_refresh_pref_keys";
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.pref, rootKey);
        // This static call will reset default values only on the first ever read
        PreferenceManager.setDefaultValues(mActivity, R.xml.pref, false);
        TextView Title = mActivity.findViewById(R.id.txtTitle);
        Title.setText(getResources().getString(R.string.action_settings));
        PreferenceManager.getDefaultSharedPreferences(mActivity);
    }

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
}
