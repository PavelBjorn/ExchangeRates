package com.fedor.pavel.exchangerates.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fedor.pavel.exchangerates.R;
import com.fedor.pavel.exchangerates.data.DataArray;
import com.fedor.pavel.exchangerates.models.BankModel;
import com.fedor.pavel.exchangerates.viewholder.BankListViewHolder;

import java.util.ArrayList;


public class BankListRVAdapter extends RecyclerView.Adapter<BankListViewHolder> {


    private LayoutInflater inflater;
    private DataArray data;
    private Context context;


    public BankListRVAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        this.context = context;
    }

    public BankListRVAdapter(Context context, DataArray data) {
        inflater = LayoutInflater.from(context);
        this.data = (data);
        this.context = context;
    }

    public void addAllModes(DataArray data) {

        this.data = data;
        notifyDataSetChanged();

    }

    public void clear() {
        data.clearAllData();
        notifyDataSetChanged();
    }

    public BankModel getBankModel(int position) {
        return data.getBank(position);
    }

    @Override
    public BankListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = inflater.inflate(R.layout.item_rv_bank_list, parent, false);
        BankListViewHolder viewHolder = new BankListViewHolder(v, context, data);


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(BankListViewHolder holder, int position) {

        BankModel bank = data.getBank(position);
        holder.setTvNameText(bank.getName());
        holder.setTvPhoneText(bank.getPhone());
        holder.setTvCityText(data.getCity(bank.getCityId()));

    }


    @Override
    public int getItemCount() {
        return data.getBanksNum();
    }
}
