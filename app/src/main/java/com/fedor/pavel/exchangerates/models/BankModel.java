package com.fedor.pavel.exchangerates.models;


import android.content.ContentValues;

import com.fedor.pavel.exchangerates.data.DataArray;
import com.fedor.pavel.exchangerates.data.SQLiteHelper;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class BankModel extends Model {


    @Expose
    @SerializedName(TITLE_JSON_KEY)
    private String name;

    @Expose
    @SerializedName(PHONE_JSON_KEY)
    private String phone;

    @Expose
    @SerializedName(ADDRESS_JSON_KEY)
    private String address;

    @Expose
    @SerializedName(REGION_ID_JSON_KEY)
    private String regionId;

    @Expose
    @SerializedName(CITY_ID_JSON_KEY)
    private String cityId;

    @Expose
    @SerializedName(ORG_TYPE_JSON_KEY)
    private int orgType;

    @Expose
    @SerializedName(LINK_JSON_KEY)
    private String link;

    @Expose
    @SerializedName(SERVER_ID_JSON_KEY)
    private String serverId;

    @Expose
    @SerializedName(DataArray.CURRENCIES_JSON_KEY)
    private HashMap<String, CurrencyModel> currencies = new HashMap<>();

    /*Keys*/
    public static final String SERVER_ID_JSON_KEY = "id";
    public static final String ORG_TYPE_JSON_KEY = "orgType";
    public static final String TITLE_JSON_KEY = "title";
    public static final String REGION_ID_JSON_KEY = "regionId";
    public static final String CITY_ID_JSON_KEY = "cityId";
    public static final String ADDRESS_JSON_KEY = "address";
    public static final String PHONE_JSON_KEY = "phone";
    public static final String LINK_JSON_KEY = "link";

    public static final String CURRENCIES_JSON_KEY = "bank_currencies";
    public static final String BANK_MODEL_JSON_KEY = "bank_model";




    /*constructors*/
    public BankModel() {
    }

    public BankModel(int orgType, String name, String phone, String address, String regionId, String cityId
            , String serverId, String link, HashMap<String, CurrencyModel> currencies) {

        this.setName(name);
        this.setPhone(phone);
        this.setAddress(address);
        this.setOrgType(orgType);
        this.setRegionId(regionId);
        this.setCityId(cityId);
        this.setServerId(serverId);
        this.setLink(link);
        this.currencies.putAll(currencies);

    }

    public BankModel(long id, int orgType, String name, String phone, String address, String regionId,
                     String cityId, String serverId, String link, HashMap<String, CurrencyModel> currencie) {

        this.setName(name);
        this.setPhone(phone);
        this.setAddress(address);
        /*this.setId(id);*/
        this.setOrgType(orgType);
        this.setRegionId(regionId);
        this.setCityId(cityId);
        this.setServerId(serverId);
        this.setLink(link);

        this.currencies.putAll(currencies);

    }


    /*Setters*/
    public void setOrgType(int orgType) {
        this.orgType = orgType;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId.isEmpty() || cityId.equals("null") ? "-" : cityId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId.isEmpty() || regionId.equals("null") ? "-" : regionId;
    }

    public void setPhone(String phone) {
        this.phone = phone.isEmpty() || phone.equals("null") ? "-" : phone;
    }

    public void setAddress(String address) {
        this.address = address.isEmpty() || address.equals("null") ? "-" : address;
    }

    public void setLink(String link) {
        this.link = link;
    }

    /*Getters*/
    public int getOrgType() {
        return orgType;
    }

    public String getRegionId() {
        return regionId;
    }

    public String getCityId() {
        return cityId;
    }

    public String getPhone() {
        return phone;
    }

    public String getLink() {
        return link;
    }

    public String getAddress() {
        return address;
    }

    public String getServerId() {
        return serverId;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public int getCurrenciesNum() {
        return currencies.size();
    }

    public ArrayList<CurrencyModel> getCurrencies() {

        ArrayList<CurrencyModel> currencyModels = new ArrayList<>();

        Set<String> positions = currencies.keySet();

        for (String position : positions) {

            CurrencyModel currency = currencies.get(position);
            currency.setName(position);
            currencyModels.add(currency);

        }


        return currencyModels;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public ContentValues getContentValues() {

        ContentValues values = new ContentValues();

        values.put(SQLiteHelper.BANKS_COLUMN_SERVER_ID, serverId);
        values.put(SQLiteHelper.BANKS_COLUMN_NAME, name);
        values.put(SQLiteHelper.BANKS_COLUMN_PHONE, phone);
        values.put(SQLiteHelper.BANKS_COLUMN_ADDRESS, address);
        values.put(SQLiteHelper.BANKS_COLUMN_CITY_ID, cityId);
        values.put(SQLiteHelper.BANKS_COLUMN_REGION_ID, regionId);
        values.put(SQLiteHelper.BANKS_COLUMN_ORG_TYPE, orgType);
        values.put(SQLiteHelper.BANKS_COLUMN_LINK, link);


        return values;

    }


}
