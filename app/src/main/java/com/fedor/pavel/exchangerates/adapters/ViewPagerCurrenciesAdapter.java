package com.fedor.pavel.exchangerates.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.fedor.pavel.exchangerates.fragments.ViewPagerCurrenciesFragment;
import com.fedor.pavel.exchangerates.models.CurrencyModel;

import java.util.ArrayList;

/**
 * Created by Pavel on 07.11.2015.
 */
public class ViewPagerCurrenciesAdapter extends FragmentStatePagerAdapter {

    private ArrayList<CurrencyModel> currencies = new ArrayList<>();


    public ViewPagerCurrenciesAdapter(FragmentManager fm, ArrayList<CurrencyModel> currencies) {
        super(fm);
        this.currencies.addAll(currencies);
    }

    @Override
    public Fragment getItem(int position) {

        CurrencyModel currency = currencies.get(position);
        ViewPagerCurrenciesFragment fragment = new ViewPagerCurrenciesFragment();
        Bundle bundle = new Bundle();
        bundle.putString(CurrencyModel.CODE_JSON_KEY, currency.getName());
        bundle.putString(CurrencyModel.FULL_NAME_KEY, currency.getFullName());
        bundle.putDouble(CurrencyModel.ASK_JSON_KEY, currency.getAsk());
        bundle.putDouble(CurrencyModel.BID_JSON_KEY, currency.getBid());
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public int getCount() {
        return currencies.size();
    }
}
