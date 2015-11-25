package com.fedor.pavel.exchangerates;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.fedor.pavel.exchangerates.adapters.ViewPagerCurrenciesAdapter;
import com.fedor.pavel.exchangerates.animation.DepthPageTransformer;
import com.fedor.pavel.exchangerates.models.BankModel;
import com.fedor.pavel.exchangerates.models.CurrencyModel;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.ArrayList;


public class BankInfoActivity extends AppCompatActivity {

    private TextView tvRegion, tvCity, tvStreet, tvPhone, tvLink;
    private ViewPagerCurrenciesAdapter adapter;
    private ViewPager vpCurrenciesList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_info);


        prepareView();

    }

    private void prepareView() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tvRegion = (TextView) findViewById(R.id.activity_bankInfo_tv_region);
        tvCity = (TextView) findViewById(R.id.activity_bankInfo_tv_city);
        tvStreet = (TextView) findViewById(R.id.activity_bankInfo_tv_street);
        tvPhone = (TextView) findViewById(R.id.activity_bankInfo_tv_phone);
        tvLink = (TextView) findViewById(R.id.activity_bankInfo_tv_link);
        vpCurrenciesList = (ViewPager) findViewById(R.id.activity_bankInfo_vp_currencies);

        getDataFromIntent();

        vpCurrenciesList.setPageTransformer(true, new DepthPageTransformer());

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private void getDataFromIntent() {

        Bundle bundle = getIntent().getBundleExtra(BankModel.BANK_MODEL_JSON_KEY);

        ArrayList<CurrencyModel> models = new Gson().fromJson(bundle.getString(BankModel.CURRENCIES_JSON_KEY), ArrayList.class);// TODO: 15.11.2015 распарсить json
        adapter = new ViewPagerCurrenciesAdapter(getSupportFragmentManager(), models);

        vpCurrenciesList.setAdapter(adapter);

        adapter = new ViewPagerCurrenciesAdapter(getSupportFragmentManager(), models);

        getSupportActionBar().setTitle(bundle.getString(BankModel.TITLE_JSON_KEY));
        getSupportActionBar().setSubtitle(bundle.getString(BankModel.ORG_TYPE_JSON_KEY));

        tvRegion.setText(bundle.getString(BankModel.REGION_ID_JSON_KEY));
        tvCity.setText(bundle.getString(BankModel.CITY_ID_JSON_KEY));
        tvStreet.setText(bundle.getString(BankModel.ADDRESS_JSON_KEY));
        tvPhone.setText(bundle.getString(BankModel.PHONE_JSON_KEY));
        tvLink.setText(bundle.getString(BankModel.LINK_JSON_KEY).replace("http://", ""));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);

        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:

                this.finish();

                break;
        }

        return true;
    }

}
