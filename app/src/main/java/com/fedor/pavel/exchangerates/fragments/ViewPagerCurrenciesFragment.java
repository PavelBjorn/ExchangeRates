package com.fedor.pavel.exchangerates.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fedor.pavel.exchangerates.R;
import com.fedor.pavel.exchangerates.models.CurrencyModel;


public class ViewPagerCurrenciesFragment extends Fragment {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_view_pager_currencies, container, false);
        createView(view);


        return view;
    }

    private void createView(View view) {

        Bundle bundle = getArguments();
        TextView tvCode = (TextView) view.findViewById(R.id.fragment_vp_currencies_tvCode);
        TextView tvName = (TextView) view.findViewById(R.id.fragment_vp_currencies_tvName);
        TextView tvAsk = (TextView) view.findViewById(R.id.fragment_vp_currencies_tvAsk);
        TextView tvBid = (TextView) view.findViewById(R.id.fragment_vp_currencies_tvBid);

        tvCode.setText(bundle.getString(CurrencyModel.CODE_JSON_KEY));
        tvName.setText(bundle.getString(CurrencyModel.FULL_NAME_KEY).toUpperCase());
        tvAsk.setText("" + bundle.getDouble(CurrencyModel.ASK_JSON_KEY));
        tvBid.setText("" + bundle.getDouble(CurrencyModel.BID_JSON_KEY));

    }


}
