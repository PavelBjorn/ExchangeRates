package com.fedor.pavel.exchangerates.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fedor.pavel.exchangerates.LoadBanksListService;
import com.fedor.pavel.exchangerates.MainActivity;
import com.fedor.pavel.exchangerates.R;
import com.fedor.pavel.exchangerates.adapters.BankListRVAdapter;
import com.fedor.pavel.exchangerates.data.DataArray;
import com.fedor.pavel.exchangerates.listeners.DataUpdateListener;

import java.util.ArrayList;


public class BanksListFragment extends Fragment implements DataUpdateListener {

    private MainActivity activity;
    private RecyclerView rvBankList;
    private BankListRVAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        activity = (MainActivity) getActivity();
        activity.setDataUpdateListener(this);
        View v = inflater.inflate(R.layout.fragment_bank_list, container, false);
        createView(v);
        return v;

    }

    private void createView(View v) {

        adapter = new BankListRVAdapter(activity, activity.getBankList());

        rvBankList = (RecyclerView) v.findViewById(R.id.fragment_bankList_rv);
        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.fragment_bankList_swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                Intent intent = new Intent(activity, LoadBanksListService.class);
                intent.putExtra(MainActivity.FIRST_START, true);
                activity.startService(intent);

            }
        });
        rvBankList.setAdapter(adapter);
        rvBankList.setLayoutManager(new LinearLayoutManager(activity));


    }

    @Override
    public void onUpdateBankList(DataArray dataArray) {
        if (dataArray != null) {
            adapter.clear();
            adapter.addAllModes(dataArray);
        }
    }


    public void cancelSwipeRefresh() {
        swipeRefreshLayout.setRefreshing(false);
    }
}
