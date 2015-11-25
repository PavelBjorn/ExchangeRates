package com.fedor.pavel.exchangerates.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.fedor.pavel.exchangerates.models.BankModel;
import com.fedor.pavel.exchangerates.models.CurrencyModel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import static com.fedor.pavel.exchangerates.data.SQLiteHelper.BANKS_COLUMN_ADDRESS;
import static com.fedor.pavel.exchangerates.data.SQLiteHelper.BANKS_COLUMN_CITY_ID;
import static com.fedor.pavel.exchangerates.data.SQLiteHelper.BANKS_COLUMN_ID;
import static com.fedor.pavel.exchangerates.data.SQLiteHelper.BANKS_COLUMN_LINK;
import static com.fedor.pavel.exchangerates.data.SQLiteHelper.BANKS_COLUMN_NAME;
import static com.fedor.pavel.exchangerates.data.SQLiteHelper.BANKS_COLUMN_ORG_TYPE;
import static com.fedor.pavel.exchangerates.data.SQLiteHelper.BANKS_COLUMN_PHONE;
import static com.fedor.pavel.exchangerates.data.SQLiteHelper.BANKS_COLUMN_REGION_ID;
import static com.fedor.pavel.exchangerates.data.SQLiteHelper.BANKS_COLUMN_SERVER_ID;
import static com.fedor.pavel.exchangerates.data.SQLiteHelper.CITIES_COLUMN_NAME;
import static com.fedor.pavel.exchangerates.data.SQLiteHelper.CITIES_COLUMN_SERVER_ID;
import static com.fedor.pavel.exchangerates.data.SQLiteHelper.CURRENCIES_COLUMN_CODE;
import static com.fedor.pavel.exchangerates.data.SQLiteHelper.CURRENCIES_COLUMN_FULL_NAME;
import static com.fedor.pavel.exchangerates.data.SQLiteHelper.ORGANIZATIONS_CURRENCIES_CODE;
import static com.fedor.pavel.exchangerates.data.SQLiteHelper.ORGANIZATIONS_CURRENCIES_COLUMN_ASK;
import static com.fedor.pavel.exchangerates.data.SQLiteHelper.ORGANIZATIONS_CURRENCIES_COLUMN_BID;
import static com.fedor.pavel.exchangerates.data.SQLiteHelper.ORGANIZATIONS_CURRENCIES_COLUMN_ORG_ID;
import static com.fedor.pavel.exchangerates.data.SQLiteHelper.ORG_TYPE_COLUMN_NAME;
import static com.fedor.pavel.exchangerates.data.SQLiteHelper.ORG_TYPE_COLUMN_TYPE;
import static com.fedor.pavel.exchangerates.data.SQLiteHelper.REGION_COLUMN_NAME;
import static com.fedor.pavel.exchangerates.data.SQLiteHelper.REGION_COLUMN_SERVER_ID;
import static com.fedor.pavel.exchangerates.data.SQLiteHelper.TABLE_NAME_BANKS;
import static com.fedor.pavel.exchangerates.data.SQLiteHelper.TABLE_NAME_CITIES;
import static com.fedor.pavel.exchangerates.data.SQLiteHelper.TABLE_NAME_CURRENCIES;
import static com.fedor.pavel.exchangerates.data.SQLiteHelper.TABLE_NAME_ORGANIZATIONS_CURRENCIES;
import static com.fedor.pavel.exchangerates.data.SQLiteHelper.TABLE_NAME_ORG_TYPE;
import static com.fedor.pavel.exchangerates.data.SQLiteHelper.TABLE_NAME_REGIONS;


public class SQLiteManager {


    private static SQLiteManager instance;
    private SQLiteHelper helper;
    private SQLiteDatabase sqLiteDatabase;
    private static Context context;

    /*Commands*/


    private SQLiteManager(Context context) {
        helper = SQLiteHelper.getInstance(context);
    }

    public static SQLiteManager getInstance(Context context) {

        if (instance == null) {
            SQLiteManager.context = context;
            instance = new SQLiteManager(context);
        }
        return instance;
    }

    public static SQLiteManager getInstance() {
        return instance;
    }


    public void open() throws SQLException {

        if (sqLiteDatabase == null) {
            this.sqLiteDatabase = helper.getWritableDatabase();
        }
    }

    public void close() {
        if (helper != null) {
            this.sqLiteDatabase.close();
        }
    }

    public long insertModel(String tableName, ContentValues values) {
        return sqLiteDatabase.insert(tableName, null, values);
    }

    public long deleteDataFromTable(String tableName) {

        return sqLiteDatabase.delete(tableName, null, null);

    }

    public ArrayList<BankModel> getBanksList() {

        Cursor cursor = sqLiteDatabase.query(TABLE_NAME_BANKS, null, null, null, null, null, null);

        ArrayList<BankModel> bankModels = new ArrayList<>();

        cursor.moveToFirst();

        if (cursor.getCount() > 0) {

            while (!cursor.isAfterLast()) {
                String serverId = cursor.getString(cursor.getColumnIndex(BANKS_COLUMN_SERVER_ID));
                bankModels.add(new BankModel(cursor.getLong(cursor.getColumnIndex(BANKS_COLUMN_ID)),
                        cursor.getInt(cursor.getColumnIndex(BANKS_COLUMN_ORG_TYPE)),
                        cursor.getString(cursor.getColumnIndex(BANKS_COLUMN_NAME)),
                        cursor.getString(cursor.getColumnIndex(BANKS_COLUMN_PHONE)),
                        cursor.getString(cursor.getColumnIndex(BANKS_COLUMN_ADDRESS)),
                        cursor.getString(cursor.getColumnIndex(BANKS_COLUMN_REGION_ID)),
                        cursor.getString(cursor.getColumnIndex(BANKS_COLUMN_CITY_ID)),
                        serverId,
                        cursor.getString(cursor.getColumnIndex(BANKS_COLUMN_LINK))
                        , getOrgCurrencies(serverId)));
                cursor.moveToNext();
            }

            cursor.close();

            return bankModels;

        } else {
            return null;
        }
    }

    public HashMap<String, CurrencyModel> getOrgCurrencies(String serverId) {

        Cursor cursor = sqLiteDatabase.query(TABLE_NAME_ORGANIZATIONS_CURRENCIES,
                null, ORGANIZATIONS_CURRENCIES_COLUMN_ORG_ID + " = '" + serverId + "'",
                null, null, null, null);

        cursor.moveToFirst();

        HashMap<String, CurrencyModel> models = new HashMap<>();

        while (!cursor.isAfterLast()) {
            String code = cursor.getString(cursor.getColumnIndex(ORGANIZATIONS_CURRENCIES_CODE));
            models.put(code, new CurrencyModel(code, "",
                    cursor.getDouble(cursor.getColumnIndex(ORGANIZATIONS_CURRENCIES_COLUMN_ASK)),
                    cursor.getDouble(cursor.getColumnIndex(ORGANIZATIONS_CURRENCIES_COLUMN_BID))));
            cursor.moveToNext();
        }

        cursor.close();

        return models;

    }

    public <T> HashMap<T, String> getData(String tableName, String columnOne, String columnTwo) {

        HashMap<T, String> map = new HashMap<>();

        Cursor cursor = sqLiteDatabase.query(tableName,
                new String[]{columnOne, columnTwo},
                null, null, null, null, null);

        cursor.moveToFirst();


        while (!cursor.isAfterLast()) {

            T key;

            if (!tableName.equals(TABLE_NAME_ORG_TYPE)) {
                String str = cursor.getString(cursor.getColumnIndex(columnOne));
                key = (T) str;
            } else {
                Integer integer = cursor.getInt(cursor.getColumnIndex(columnOne));
                key = (T) integer;
            }


            map.put(key, cursor.getString(cursor.getColumnIndex(columnTwo)));


            cursor.moveToNext();

        }
        cursor.close();
        return map;
    }

}


