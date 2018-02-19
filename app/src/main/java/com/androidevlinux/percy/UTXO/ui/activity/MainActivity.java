package com.androidevlinux.percy.UTXO.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.androidevlinux.percy.UTXO.R;
import com.androidevlinux.percy.UTXO.ui.base.BaseActivity;
import com.androidevlinux.percy.UTXO.ui.fragment.PriceCheckFragment;
import com.androidevlinux.percy.UTXO.ui.fragment.SettingsFragment;
import com.androidevlinux.percy.UTXO.ui.fragment.blocktrail.BlockChainExplorerFragment;
import com.androidevlinux.percy.UTXO.ui.fragment.changelly.CreateTransactionFragment;
import com.androidevlinux.percy.UTXO.ui.fragment.changelly.ExchangeAmountFragment;
import com.androidevlinux.percy.UTXO.ui.fragment.changelly.GetStatusFragment;
import com.androidevlinux.percy.UTXO.ui.fragment.changelly.MinAmountFragment;
import com.androidevlinux.percy.UTXO.ui.fragment.charts.BitfinexCandleChartFragment;
import com.androidevlinux.percy.UTXO.utils.Constants;
import com.crashlytics.android.Crashlytics;
import com.mikepenz.aboutlibraries.Libs;
import com.mikepenz.aboutlibraries.LibsBuilder;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import io.fabric.sdk.android.Fabric;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, MainContract.MainView {

    @BindView(R.id.txtTitle)
    TextView txtTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.container)
    FrameLayout container;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.refresh_fab)
    FloatingActionButton refreshFab;
    private Fragment fragment;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        Toasty.Config.getInstance()
                .setErrorColor(ContextCompat.getColor(this, R.color.error))
                .setWarningColor(ContextCompat.getColor(this, R.color.warning))
                .setInfoColor(ContextCompat.getColor(this, R.color.information))
                .setSuccessColor(ContextCompat.getColor(this, R.color.success))
                .tintIcon(true)
                .apply();


        fab.setOnClickListener(view -> {
            showCreateTransactionFragment();
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navView.setNavigationItemSelectedListener(this);
        navView.setItemIconTintList(null);
        if (Constants.ACTION_SETTINGS.equalsIgnoreCase(getIntent().getAction())) {
            displaySelectedScreen(R.id.nav_settings);
        } else if (Constants.ACTION_BTC.equalsIgnoreCase(getIntent().getAction())) {
            displaySelectedScreen(R.id.nav_btc_price);
        } else if (Constants.ACTION_CREATE_TRANSACTION.equalsIgnoreCase(getIntent().getAction())) {
            displaySelectedScreen(R.id.nav_create_transaction);
        } else {
            displaySelectedScreen(R.id.nav_get_min_amount);
        }
        drawer.openDrawer(GravityCompat.START);
    }

    private void displaySelectedScreen(int itemId) {
        switch (itemId) {
            case R.id.nav_get_min_amount:
                fab.show();
                refreshFab.hide();
                showMinAmountFragment();
                break;
            case R.id.nav_exchange_amount:
                fab.show();
                refreshFab.hide();
                showExchangeAmountFragment();
                break;
            case R.id.nav_create_transaction:
                fab.show();
                refreshFab.hide();
                showCreateTransactionFragment();
                break;
            case R.id.nav_get_status:
                fab.show();
                refreshFab.hide();
                showGetStatusFragment();
                break;
            case R.id.nav_btc_price:
                fab.hide();
                refreshFab.show();
                showPriceCheckFragment();
                break;
            case R.id.nav_bitfinex_candle_chart:
                fab.hide();
                refreshFab.hide();
                showBitfinexCandleChartFragment();
                break;
            case R.id.nav_block_chain_explorer:
                fab.hide();
                refreshFab.hide();
                showBlockChainExplorerFragment();
                break;
            case R.id.nav_settings:
                fab.hide();
                refreshFab.hide();
                showSettingsFragment();
                break;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void replaceFragment(Fragment fragment) {
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.container, fragment);
            ft.addToBackStack(null).commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_settings:
                showSettingsFragment();
                break;
            case R.id.action_licenses:
                showLicenses();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showLicenses() {
        new LibsBuilder()
                //provide a style (optional) (LIGHT, DARK, LIGHT_DARK_TOOLBAR)
                .withActivityStyle(Libs.ActivityStyle.LIGHT_DARK_TOOLBAR)
                .withAboutIconShown(true)
                .withAboutVersionShown(true)
                .withAboutAppName(getString(R.string.app_name))
                //start the activity
                .start(this);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        displaySelectedScreen(item.getItemId());
        return true;
    }

    @Override
    public void showMinAmountFragment() {
        fragment = new MinAmountFragment();
        replaceFragment(fragment);
    }

    @Override
    public void showExchangeAmountFragment() {
        fragment = new ExchangeAmountFragment();
        replaceFragment(fragment);
    }

    @Override
    public void showCreateTransactionFragment() {
        fragment = new CreateTransactionFragment();
        replaceFragment(fragment);
    }

    @Override
    public void showGetStatusFragment() {
        fragment = new GetStatusFragment();
        replaceFragment(fragment);
    }

    @Override
    public void showPriceCheckFragment() {
        fragment = new PriceCheckFragment();
        replaceFragment(fragment);
    }

    @Override
    public void showBitfinexCandleChartFragment() {
        fragment = new BitfinexCandleChartFragment();
        replaceFragment(fragment);
    }

    @Override
    public void showBlockChainExplorerFragment() {
        fragment = new BlockChainExplorerFragment();
        replaceFragment(fragment);
    }

    @Override
    public void showSettingsFragment() {
        fragment = new SettingsFragment();
        replaceFragment(fragment);
    }
}
