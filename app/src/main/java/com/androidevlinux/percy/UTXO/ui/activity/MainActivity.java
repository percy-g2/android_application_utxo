package com.androidevlinux.percy.UTXO.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.androidevlinux.percy.UTXO.R;
import com.androidevlinux.percy.UTXO.ui.fragment.ExtrasFragment;
import com.androidevlinux.percy.UTXO.ui.fragment.GoogleSignInFragment;
import com.androidevlinux.percy.UTXO.ui.fragment.SettingsFragment;
import com.androidevlinux.percy.UTXO.ui.fragment.changelly.CreateTransactionFragment;
import com.androidevlinux.percy.UTXO.ui.fragment.changelly.ExchangeAmountFragment;
import com.androidevlinux.percy.UTXO.ui.fragment.changelly.GetStatusFragment;
import com.androidevlinux.percy.UTXO.ui.fragment.changelly.MinAmountFragment;
import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private Fragment fragment;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            fragment = new CreateTransactionFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.container, fragment);
            ft.addToBackStack(null).commit();
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);
        String ACTION_SETTINGS = "com.androidevlinux.percy.UTXO.ui.activity.SETTINGS";
        String ACTION_BTC = "com.androidevlinux.percy.UTXO.ui.activity.BTC";
        String ACTION_CREATE_TRANSACTION = "com.androidevlinux.percy.UTXO.ui.activity.CREATE_TRANSACTION";
        if (ACTION_SETTINGS.equalsIgnoreCase(getIntent().getAction())) {
            displaySelectedScreen(R.id.nav_settings);
        } else if (ACTION_BTC.equalsIgnoreCase(getIntent().getAction())) {
            displaySelectedScreen(R.id.nav_extras);
        } else if (ACTION_CREATE_TRANSACTION.equalsIgnoreCase(getIntent().getAction())) {
            displaySelectedScreen(R.id.nav_create_transaction);
        } else {
            displaySelectedScreen(R.id.nav_get_min_amount);
        }
    }

    private void displaySelectedScreen(int itemId) {
        switch (itemId) {
            case R.id.nav_sign_in:
                fragment = new GoogleSignInFragment();
                break;
            case R.id.nav_get_min_amount:
                fragment = new MinAmountFragment();
                break;
            case R.id.nav_exchange_amount:
                fragment = new ExchangeAmountFragment();
                break;
            case R.id.nav_create_transaction:
                fragment = new CreateTransactionFragment();
                break;
            case R.id.nav_get_status:
                fragment = new GetStatusFragment();
                break;
            case R.id.nav_extras:
                fragment = new ExtrasFragment();
                break;
            case R.id.nav_settings:
                fragment = new SettingsFragment();
                break;
        }
        //replacing the fragment
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.container, fragment);
            ft.addToBackStack(null).commit();
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
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            fragment = new SettingsFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.container, fragment);
            ft.addToBackStack(null).commit();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        displaySelectedScreen(item.getItemId());
        return true;
    }
}
