package com.fedor.pavel.exchangerates.data;


import android.content.ContentValues;
import android.os.Bundle;


import com.fedor.pavel.exchangerates.models.BankModel;
import com.fedor.pavel.exchangerates.models.CurrencyModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;


public class DataArray {

    public static final String ORG_JSON_KEY = "organizations";
    public static final String ORG_TYPE_JSON_KEY = "orgTypes";
    public static final String REGIONS_JSON_KEY = "regions";
    public static final String CURRENCIES_JSON_KEY = "currencies";
    public static final String CITIES_JSON_KEY = "cities";


    @Expose
    @SerializedName(ORG_JSON_KEY)
    private ArrayList<BankModel> banks;

    @Expose
    @SerializedName(ORG_TYPE_JSON_KEY)
    private HashMap<Integer, String> orgTypes;

    @Expose
    @SerializedName(REGIONS_JSON_KEY)
    private HashMap<String, String> regions;

    @Expose
    @SerializedName(CITIES_JSON_KEY)
    private HashMap<String, String> cities;

    @Expose
    @SerializedName(CURRENCIES_JSON_KEY)
    private HashMap<String, String> currencies;

    public DataArray() {

    }

    public void clearAllData() {

        banks.clear();
        orgTypes.clear();
        regions.clear();
        cities.clear();
        currencies.clear();
    }

    public BankModel getBank(int position) {
        return banks.get(position);
    }

    public String getCurrencyFullName(String code) {
        return currencies.get(code);
    }

    public String getOrgType(int typeId) {
        return orgTypes.get(typeId);
    }

    public String getRegion(String serverId) {
        return regions.get(serverId);
    }

    public String getCity(String serverId) {
        return cities.get(serverId);
    }

    public int getBanksNum() {
        return banks.size();
    }

    public ArrayList<BankModel> getAllBanks() {
        return new ArrayList<>(banks);
    }

    public Bundle bankToBundle(int position) throws JSONException {

        BankModel bank = banks.get(position);

        Bundle bundle = new Bundle();

        bundle.putString(BankModel.TITLE_JSON_KEY, bank.getName());
        bundle.putString(BankModel.REGION_ID_JSON_KEY, regions.get(bank.getRegionId()));
        bundle.putString(BankModel.CITY_ID_JSON_KEY, cities.get(bank.getCityId()));
        bundle.putString(BankModel.ADDRESS_JSON_KEY, bank.getAddress());
        bundle.putString(BankModel.PHONE_JSON_KEY, bank.getPhone());
        bundle.putString(BankModel.LINK_JSON_KEY, bank.getLink());
        bundle.putString(BankModel.ORG_TYPE_JSON_KEY, orgTypes.get(bank.getOrgType()));


        ArrayList<CurrencyModel> currencyModels = bank.getCurrencies();
        JSONArray array = new JSONArray();

        for (CurrencyModel currency : currencyModels) {
            JSONObject object = new JSONObject();
            object.put(CurrencyModel.CODE_JSON_KEY, currency.getName());
            object.put(CurrencyModel.FULL_NAME_KEY, this.currencies.get(currency.getName()));
            object.put(CurrencyModel.ASK_JSON_KEY, currency.getAsk());
            object.put(CurrencyModel.BID_JSON_KEY, currency.getBid());
            array.put(object);
        }

        bundle.putString(BankModel.CURRENCIES_JSON_KEY, array.toString());

        return bundle;
    }

    public void saveDataToDb() {

        SQLiteManager sqLiteManager = SQLiteManager.getInstance();

        saveBanks(sqLiteManager);
        saveORGTypes(sqLiteManager);
        saveRegions(sqLiteManager);
        saveCities(sqLiteManager);
        saveCurrencies(sqLiteManager);

    }

    private void saveBanks(SQLiteManager sqLiteManager) {

        sqLiteManager.deleteDataFromTable(SQLiteHelper.TABLE_NAME_BANKS);
        sqLiteManager.deleteDataFromTable(SQLiteHelper.TABLE_NAME_ORGANIZATIONS_CURRENCIES);

        for (BankModel bank : banks) {

            sqLiteManager.insertModel(SQLiteHelper.TABLE_NAME_BANKS, bank.getContentValues());

            ArrayList<CurrencyModel> currencyModels = bank.getCurrencies();

            for (CurrencyModel currency : currencyModels) {
                ContentValues values = currency.getContentValues();
                values.put(SQLiteHelper.ORGANIZATIONS_CURRENCIES_COLUMN_ORG_ID, bank.getServerId());
                sqLiteManager.insertModel(SQLiteHelper.TABLE_NAME_ORGANIZATIONS_CURRENCIES, values);
            }
        }

    }

    private void saveORGTypes(SQLiteManager sqLiteManager) {

        sqLiteManager.deleteDataFromTable(SQLiteHelper.TABLE_NAME_ORG_TYPE);
        Set<Integer> keys = orgTypes.keySet();

        for (int key : keys) {

            ContentValues values = new ContentValues();
            values.put(SQLiteHelper.ORG_TYPE_COLUMN_TYPE, key);
            values.put(SQLiteHelper.ORG_TYPE_COLUMN_NAME, orgTypes.get(key));
            sqLiteManager.insertModel(SQLiteHelper.TABLE_NAME_ORG_TYPE, values);

        }


    }

    private void saveRegions(SQLiteManager sqLiteManager) {

        sqLiteManager.deleteDataFromTable(SQLiteHelper.TABLE_NAME_REGIONS);
        Set<String> keys = regions.keySet();

        for (String key : keys) {

            ContentValues values = new ContentValues();
            values.put(SQLiteHelper.REGION_COLUMN_SERVER_ID, key);
            values.put(SQLiteHelper.REGION_COLUMN_NAME, regions.get(key));
            sqLiteManager.insertModel(SQLiteHelper.TABLE_NAME_REGIONS, values);

        }


    }

    private void saveCities(SQLiteManager sqLiteManager) {

        sqLiteManager.deleteDataFromTable(SQLiteHelper.TABLE_NAME_CITIES);
        Set<String> keys = cities.keySet();

        for (String key : keys) {

            ContentValues values = new ContentValues();
            values.put(SQLiteHelper.CITIES_COLUMN_SERVER_ID, key);
            values.put(SQLiteHelper.CITIES_COLUMN_NAME, cities.get(key));
            sqLiteManager.insertModel(SQLiteHelper.TABLE_NAME_CITIES, values);

        }


    }

    private void saveCurrencies(SQLiteManager sqLiteManager) {

        sqLiteManager.deleteDataFromTable(SQLiteHelper.TABLE_NAME_CURRENCIES);
        Set<String> keys = currencies.keySet();

        for (String key : keys) {

            ContentValues values = new ContentValues();
            values.put(SQLiteHelper.CURRENCIES_COLUMN_CODE, key);
            values.put(SQLiteHelper.CURRENCIES_COLUMN_FULL_NAME, currencies.get(key));
            sqLiteManager.insertModel(SQLiteHelper.TABLE_NAME_CURRENCIES, values);

        }


    }

    public DataArray loadDataFromDb() {

        SQLiteManager manager = SQLiteManager.getInstance();

        banks = manager.getBanksList();

        if (banks == null) {
            return null;
        }

        currencies = manager.<String>getData(SQLiteHelper.TABLE_NAME_CURRENCIES,
                SQLiteHelper.CURRENCIES_COLUMN_CODE,
                SQLiteHelper.CURRENCIES_COLUMN_FULL_NAME);

        regions = manager.<String>getData(SQLiteHelper.TABLE_NAME_REGIONS,
                SQLiteHelper.REGION_COLUMN_SERVER_ID,
                SQLiteHelper.REGION_COLUMN_NAME);

        cities = manager.<String>getData(SQLiteHelper.TABLE_NAME_CITIES,
                SQLiteHelper.CITIES_COLUMN_SERVER_ID,
                SQLiteHelper.CITIES_COLUMN_NAME);

        orgTypes = manager.<Integer>getData(SQLiteHelper.TABLE_NAME_ORG_TYPE,
                SQLiteHelper.ORG_TYPE_COLUMN_TYPE, SQLiteHelper.ORG_TYPE_COLUMN_NAME);


        return this;
    }


}
