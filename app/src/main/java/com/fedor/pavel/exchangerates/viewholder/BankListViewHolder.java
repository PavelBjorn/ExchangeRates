package com.fedor.pavel.exchangerates.viewholder;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fedor.pavel.exchangerates.BankInfoActivity;
import com.fedor.pavel.exchangerates.R;
import com.fedor.pavel.exchangerates.data.DataArray;
import com.fedor.pavel.exchangerates.models.BankModel;

import org.json.JSONException;

public class BankListViewHolder extends RecyclerView.ViewHolder {

    private TextView tvName, tvPhone, tvCity;
    private LinearLayout itemContainer;
    private Context context;


    public BankListViewHolder(View itemView, Context context, DataArray dataArray) {
        super(itemView);
        createView(itemView, dataArray);
        this.context = context;
    }


    private void createView(View view, final DataArray dataArray) {
        tvName = (TextView) view.findViewById(R.id.item_rv_bankList_name);
        tvPhone = (TextView) view.findViewById(R.id.item_rv_bankList_phone);
        tvCity = (TextView) view.findViewById(R.id.item_rv_bankList_city);
        itemContainer = (LinearLayout) view.findViewById(R.id.item_rv_bankList_container);
        itemContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    Intent intent = new Intent(context, BankInfoActivity.class);
                    intent.putExtra(BankModel.BANK_MODEL_JSON_KEY, dataArray.bankToBundle(getAdapterPosition()));
                    context.startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void setTvNameText(String text) {
        tvName.setText(text);
    }

    public void setTvPhoneText(String text) {
        tvPhone.setText(text);
    }

    public void setTvCityText(String text) {
        tvCity.setText(text);
    }


}
