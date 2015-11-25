package com.fedor.pavel.exchangerates;


import android.app.AlarmManager;
import android.app.PendingIntent;

import android.app.ProgressDialog;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;


import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


import com.fedor.pavel.exchangerates.data.DataArray;
import com.fedor.pavel.exchangerates.data.PreferencesManager;
import com.fedor.pavel.exchangerates.data.SQLiteManager;
import com.fedor.pavel.exchangerates.fragments.BanksListFragment;
import com.fedor.pavel.exchangerates.fragments.FavoriteBankFragment;
import com.fedor.pavel.exchangerates.listeners.DataUpdateListener;
import com.fedor.pavel.exchangerates.models.BankModel;
import com.fedor.pavel.exchangerates.network.NetManager;
import com.fedor.pavel.exchangerates.task.BankLoadTask;

import java.sql.SQLException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, DataUpdateListener {

    /*Params*/

    /*Views*/
    private AlarmManager alarmManager;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private BroadcastReceiver receiver;
    private ProgressDialog progressDialog;
    private SQLiteManager sqLiteManager;

    /*Listeners*/
    private DataUpdateListener dataUpdateListener;

    /*Data*/
    long timB;
    private DataArray data;

    /*Constants*/
    private static final int MAIN_CONTENT_FRAGMENT_CONTAINER_ID = R.id.main_activity_fragment_container;
    public static final String API = "http://resources.finance.ua/ru/public/currency-cash.json";
    private static final String LOG_TAG = "MainActivityTag";
    private static final String ACTION = "fedor.pavel.exchangerates.ACTION_UPDATE_BANK_LIST";
    public static final String ACTION_CONNECTION_FINISHED = "fedor.pavel.exchangerates.ACTION_CONNECTION_FINISHED";
    public static final String FIRST_START = "first_start";
    public static final String ACTION_CONNECTION_FAILED = "fedor.pavel.exchangerates.ACTION_CONNECTION_FAILED";

    /*Request*/
    private static final int REQUEST_CODE_SETTINGS = 100;

    /* Methods */
    /*Activity lifecycle*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        sqLiteManager = SQLiteManager.getInstance(getApplicationContext());

        try {
            sqLiteManager.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        createViews();
        createFinishConnectionReceiver();
        createProgressDialog();
        progressDialog.show();
        dataUpdateListener = this;

        BankLoadTask loadTask = new BankLoadTask(this, dataUpdateListener);
        timB = System.currentTimeMillis();
        loadTask.execute();
        prepareAlarmManager();

    }

    /**
     * Hear finds all views bind to this context
     */
    private void createViews() {


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);


    }

    private void prepareAlarmManager() {

        Intent intent = new Intent(this, BankListUpdateReceiver.class);
        intent.setAction(ACTION);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

        int timePosition = PreferencesManager.getInstance().loadSettings().getInt(SettingsActivity.UPDATE_FREQUENCY_KEY, 2);

        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() +
                        SettingsActivity.time[timePosition],
                SettingsActivity.time[timePosition],
                alarmIntent);

    }

    public void replaceFragment(Fragment fragment) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(MAIN_CONTENT_FRAGMENT_CONTAINER_ID, fragment);
        transaction.commit();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /*Work with menu*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);

       /* SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem item = menu.findItem(R.id.main_action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        searchView.setIconifiedByDefault(false);*/
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {

            startActivityForResult(new Intent(this, SettingsActivity.class), REQUEST_CODE_SETTINGS);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public DataArray getBankList() {
        return data;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_SETTINGS && resultCode == RESULT_OK) {
            prepareAlarmManager();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sqLiteManager.close();
        if (receiver != null) {
            unregisterReceiver(receiver);
        }

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // Handle navigation view item clicks here.
        switch (item.getItemId()) {
            case R.id.nav_favorite:
                replaceFragment(new FavoriteBankFragment());
                break;
            case R.id.nav_bank_list:
                replaceFragment(new BanksListFragment());
                break;

        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onUpdateBankList(DataArray data) {

        if (data != null) {

            this.data = data;

            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }

            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.main_activity_fragment_container);

            if (!(fragment instanceof BanksListFragment)) {

                replaceFragment(new BanksListFragment());
                Log.d(LOG_TAG, "" + (System.currentTimeMillis() - timB) / 1000);

            } else {
                ((BanksListFragment) fragment).cancelSwipeRefresh();
            }

        } else {
            Intent intent = new Intent(this, LoadBanksListService.class);
            intent.putExtra(FIRST_START, true);
            startService(intent);
        }

    }

    public void createFinishConnectionReceiver() {

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                if (intent.getAction().equals(ACTION_CONNECTION_FINISHED)) {
                    BankLoadTask task = new BankLoadTask(MainActivity.this, dataUpdateListener);
                    task.execute();
                }

                if (intent.getAction().equals(ACTION_CONNECTION_FAILED)) {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                        createSettingsAlert(intent.getIntExtra(NetManager.FAILED_CONNECTION_REASON, 1));
                    }
                    Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.main_activity_fragment_container);
                    if (fragment instanceof BanksListFragment) {
                        ((BanksListFragment) fragment).cancelSwipeRefresh();
                    }
                }
            }
        };

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_CONNECTION_FINISHED);
        intentFilter.addAction(ACTION_CONNECTION_FAILED);

        registerReceiver(receiver, intentFilter);

    }

    public void createSettingsAlert(int errorCode) {

        AlertDialog.Builder settingsAlert = new AlertDialog.Builder(this);

        settingsAlert.setTitle("Ошибка подключения");
        settingsAlert.setIcon(R.drawable.ic_alert);

        settingsAlert.setCancelable(false);

        switch (errorCode) {

            case 1:

                settingsAlert.setMessage("Отсутствует соединение с интернетом! Проверте настройки пожалуйста");

                settingsAlert.setPositiveButton("Параметры", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_SETTINGS);
                        startActivity(intent);
                        MainActivity.this.finish();
                    }
                });

                settingsAlert.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MainActivity.this.finish();
                    }
                });

                break;
            case 2:

                settingsAlert.setMessage("Сервер не отвечает!");
                settingsAlert.setPositiveButton("Повторить попытку", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!progressDialog.isShowing()) {
                            progressDialog.show();
                        }
                        Intent intent = new Intent(MainActivity.this, LoadBanksListService.class);
                        intent.putExtra(FIRST_START, true);
                        startService(intent);

                    }
                });

                settingsAlert.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MainActivity.this.finish();
                    }
                });
                break;

        }

        settingsAlert.show();
    }

    public void createProgressDialog() {

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Загрузка данных");
        progressDialog.setCancelable(false);

    }

    public void setDataUpdateListener(DataUpdateListener listener) {
        this.dataUpdateListener = listener;
    }

}
