package com.fedor.pavel.exchangerates.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import com.fedor.pavel.exchangerates.MainActivity;
import com.fedor.pavel.exchangerates.R;
import com.fedor.pavel.exchangerates.adapters.SpAdapter;

import org.json.JSONException;
import org.json.JSONObject;


public class FavoriteBankFragment extends Fragment {

    /*private MainActivity activity;
    private SpAdapter<BankModel> bankSpAdapter;
    private SpAdapter<CurrencyModel> currenciesSpAdapte;
    private SpAdapter<CurrencyModel> spFavoriteBankCurrenciesAdepter;
    private Spinner spBanks, spFavoriteBankCurrencyes, spMinMaxCurrencies;
    private TextView tvAsk, tvBid;

    private String favoriteBankName;
    private String favoriteCurrencyName;

    public static final String FAVORITE_BANK_NAME_KEY = "favoriteBankName";
    private static final String FAVORITE_CURRENCY_KEY = "favoriteCurrency";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        this.activity = (MainActivity) getActivity();

        View v = inflater.inflate(R.layout.fragment_favorite_bank, container, false);

        createView(v);


        return v;
    }

    private void createView(View view) {

        spFavoriteBankCurrenciesAdepter = new SpAdapter<>(activity);
        bankSpAdapter = new SpAdapter<>(activity, activity.getBankList());

        currenciesSpAdapte = new SpAdapter<>(activity, activity.getCurrencyList());

        spBanks = (Spinner) view.findViewById(R.id.fragment_sp_favorite_bank);
        spBanks.setAdapter(bankSpAdapter);

        spFavoriteBankCurrencyes = (Spinner) view.findViewById(R.id.fragment_favorite_bank_sp_currency);
        spFavoriteBankCurrencyes.setAdapter(spFavoriteBankCurrenciesAdepter);

        tvAsk = (TextView) view.findViewById(R.id.fragment_favorite_bank_tv_ask);
        tvBid = (TextView) view.findViewById(R.id.fragment_favorite_bank_tv_bid);

        spMinMaxCurrencies = (Spinner) view.findViewById(R.id.fragment_favorite_bank_sp_currencyMinMax);
       *//* spMinMaxCurrencies.setAdapter();*//*



        int bankPosition;
*//*
        if (loadedObject.isEmpty()) {
            bankPosition = 0;

        } else {
            try {
                JSONObject object = new JSONObject(loadedObject);
                String favoriteBankName = object.getString(FAVORITE_BANK_NAME_KEY);
                favoriteCurrencyName = object.getString(FAVORITE_CURRENCY_KEY);
                bankPosition = bankSpAdapter.getItemPositionByName(favoriteBankName);
            } catch (JSONException e) {
                e.printStackTrace();
                bankPosition = 0;
            }

        }

        spBanks.setSelection(bankPosition);*//*


        spBanks.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                spFavoriteBankCurrenciesAdepter.clear();
                *//*spFavoriteBankCurrenciesAdepter.addAll(bankSpAdapter.getItem(position));*//*

                favoriteBankName = bankSpAdapter.getItem(position).getName();
                spFavoriteBankCurrencyes.setSelection(spFavoriteBankCurrenciesAdepter.getItemPositionByName(favoriteCurrencyName));
                selecteFavoriteBank();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spFavoriteBankCurrencyes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                favoriteCurrencyName = spFavoriteBankCurrenciesAdepter.getItem(position).getName();

                selecteFavoriteBank();
                tvBid.setText("" + spFavoriteBankCurrenciesAdepter.getItem(position).getBid());
                tvAsk.setText("" + spFavoriteBankCurrenciesAdepter.getItem(position).getAsk());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private void selecteFavoriteBank() {

        JSONObject object = new JSONObject();
        try {

            object.put(FAVORITE_CURRENCY_KEY, favoriteCurrencyName);
            object.put(FAVORITE_BANK_NAME_KEY, favoriteBankName);

        } catch (JSONException e) {
            e.printStackTrace();
        }



    }*/

}
