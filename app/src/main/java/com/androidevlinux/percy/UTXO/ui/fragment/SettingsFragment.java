package com.androidevlinux.percy.UTXO.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.widget.TextView;

import com.androidevlinux.percy.UTXO.R;

/**
 * Created by percy on 19/11/17.
 */

public class SettingsFragment extends PreferenceFragmentCompat {
    Activity mActivity;
    String TAG = "Settings Fragment";
    public static String refresh_btc_price_button_key = "enable_refresh_pref_keys";
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.pref, rootKey);
        // This static call will reset default values only on the first ever read
        PreferenceManager.setDefaultValues(getActivity(), R.xml.pref, false);
        TextView Title = getActivity().findViewById(R.id.txtTitle);
        Title.setText(getResources().getString(R.string.action_settings));
        try {
            mActivity = getActivity();
            PreferenceManager.getDefaultSharedPreferences(mActivity);
        } catch (Exception e) {
            Log.i(TAG, e.getMessage());
        }
    }
}
