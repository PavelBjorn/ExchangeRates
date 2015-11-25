package com.fedor.pavel.exchangerates.models;


import android.content.ContentValues;

import com.fedor.pavel.exchangerates.data.SQLiteHelper;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CurrencyModel extends Model {

    @SerializedName(CODE_JSON_KEY)
    private String name;

    @SerializedName(FULL_NAME_KEY)
    private String fullName;

    @Expose
    @SerializedName("ask")
    private double ask;

    @Expose
    @SerializedName("bid")
    private double bid;

    /*Keys*/
    public static final String CODE_JSON_KEY = "code";
    public static final String FULL_NAME_KEY = "full_name";
    public static final String ASK_JSON_KEY = "ask";
    public static final String BID_JSON_KEY = "bid";


    public CurrencyModel() {
    }

    public CurrencyModel(String name, String fullName) {
        this.name = name;
        this.fullName = fullName;
    }

    public CurrencyModel(String name, String fullName, double ask, double bid) {
        this.name = name;
        this.ask = ask;
        this.bid = bid;
        this.fullName = fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setAsk(double ask) {
        this.ask = ask;
    }

    public void setBid(double bid) {
        this.bid = bid;
    }

    public String getFullName() {
        return fullName;
    }

    public double getAsk() {
        return ask;
    }

    public double getBid() {
        return bid;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        return name.equals(((CurrencyModel) o).getName()) || fullName.equals(((CurrencyModel) o).getFullName());
    }

    public ContentValues getContentValues() {

        ContentValues values = new ContentValues();

        values.put(SQLiteHelper.ORGANIZATIONS_CURRENCIES_CODE, name);
        values.put(SQLiteHelper.ORGANIZATIONS_CURRENCIES_COLUMN_ASK, ask);
        values.put(SQLiteHelper.ORGANIZATIONS_CURRENCIES_COLUMN_BID, bid);

        return values;

    }


}
