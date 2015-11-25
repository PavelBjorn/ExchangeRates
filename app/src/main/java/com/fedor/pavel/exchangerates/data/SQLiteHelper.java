package com.fedor.pavel.exchangerates.data;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper extends SQLiteOpenHelper {

    private static SQLiteHelper instance;


    /*Data base properties*/
    public static final String DATA_BASE_NAME = "exchange_rates.db";
    public static final int DATA_BASE_VERSION = 1;

    /*Tables*/

    // - Banks  table
    public static final String TABLE_NAME_BANKS = "banks_list";
    public static final String BANKS_COLUMN_ID = "_id";
    public static final String BANKS_COLUMN_SERVER_ID = "bank_server_id";
    public static final String BANKS_COLUMN_NAME = "bank_name";
    public static final String BANKS_COLUMN_PHONE = "phone";
    public static final String BANKS_COLUMN_ADDRESS = "address";
    public static final String BANKS_COLUMN_CITY_ID = "city_id";
    public static final String BANKS_COLUMN_REGION_ID = "region_id";
    public static final String BANKS_COLUMN_ORG_TYPE = "organization_type";
    public static final String BANKS_COLUMN_LINK = "link";


    // - Currencies table
    public static final String TABLE_NAME_CURRENCIES = "currencies_list";
    public static final String CURRENCIES_COLUMN_ID = "_id";
    public static final String CURRENCIES_COLUMN_CODE = "code";
    public static final String CURRENCIES_COLUMN_FULL_NAME = "currency_full_name";

    //Organizations type table
    public static final String TABLE_NAME_ORG_TYPE = "org_type";
    public static final String ORG_TYPE_COLUMN_ID = "_id";
    public static final String ORG_TYPE_COLUMN_TYPE = "type";
    public static final String ORG_TYPE_COLUMN_NAME = "type_name";

    //Regions table
    public static final String TABLE_NAME_REGIONS = "regions_list";
    public static final String REGIONS_COLUMN_ID = "_id";
    public static final String REGION_COLUMN_SERVER_ID = "region_server_id";
    public static final String REGION_COLUMN_NAME = "region_name";

    //Cities table
    public static final String TABLE_NAME_CITIES = "cities_list";
    public static final String CITIES_COLUMN_ID = "_id";
    public static final String CITIES_COLUMN_SERVER_ID = "city_server_id";
    public static final String CITIES_COLUMN_NAME = "city_name";

    //Organization currencies table
    public static final String TABLE_NAME_ORGANIZATIONS_CURRENCIES = "organizations_currencies";
    public static final String ORGANIZATIONS_CURRENCIES_COLUMN_ID = "_id";
    public static final String ORGANIZATIONS_CURRENCIES_CODE = "code";
    public static final String ORGANIZATIONS_CURRENCIES_COLUMN_ORG_ID = "organization_id";
    public static final String ORGANIZATIONS_CURRENCIES_COLUMN_ASK = "ask";
    public static final String ORGANIZATIONS_CURRENCIES_COLUMN_BID = "bid";
    /*public static final String ORGANIZATIONS_CURRENCIES_COLUMN_DATE = "date";*/


    //- Commands
    // - Create:
    public static final String CREATE_TABLE_BANKS = "CREATE TABLE " + TABLE_NAME_BANKS + " ("
            + BANKS_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + BANKS_COLUMN_SERVER_ID + " TEXT NOT NULL, "
            + BANKS_COLUMN_ORG_TYPE + " INTEGER, "
            + BANKS_COLUMN_NAME + " TEXT NOT NULL, "
            + BANKS_COLUMN_REGION_ID + " TEXT, "
            + BANKS_COLUMN_CITY_ID + " TEXT, "
            + BANKS_COLUMN_PHONE + " TEXT, "
            + BANKS_COLUMN_LINK + " TEXT, "
            + BANKS_COLUMN_ADDRESS + " TEXT"
            + ");";

    public static final String CREATE_TABLE_CURRENCIES = "CREATE TABLE " + TABLE_NAME_CURRENCIES + " ("
            + CURRENCIES_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + CURRENCIES_COLUMN_CODE + " TEXT NOT NULL, "
            + CURRENCIES_COLUMN_FULL_NAME + " TEXT"
            + ");";

    public static final String CREATE_TABLE_ORG_TYPE = "CREATE TABLE " + TABLE_NAME_ORG_TYPE + " ("
            + ORG_TYPE_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + ORG_TYPE_COLUMN_TYPE + " INTEGER NOT NULL, "
            + ORG_TYPE_COLUMN_NAME + " TEXT"
            + ");";

    public static final String CREATE_TABLE_REGIONS_LIST = "CREATE TABLE " + TABLE_NAME_REGIONS + " ("
            + REGIONS_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + REGION_COLUMN_SERVER_ID + " TEXT NOT NULL, "
            + REGION_COLUMN_NAME + " TEXT"
            + ");";

    public static final String CREATE_TABLE_CITIES_LIST = "CREATE TABLE " + TABLE_NAME_CITIES + " ("
            + CITIES_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + CITIES_COLUMN_SERVER_ID + " TEXT NOT NULL, "
            + CITIES_COLUMN_NAME + " TEXT"
            + ");";

    public static final String CREATE_TABLE_ORG_CURRENCIES = "CREATE TABLE " + TABLE_NAME_ORGANIZATIONS_CURRENCIES + " ("
            + ORGANIZATIONS_CURRENCIES_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + ORGANIZATIONS_CURRENCIES_COLUMN_ORG_ID + " TEXT, "
            + ORGANIZATIONS_CURRENCIES_CODE + " TEXT, "
            + ORGANIZATIONS_CURRENCIES_COLUMN_ASK + " REAL, "
            + ORGANIZATIONS_CURRENCIES_COLUMN_BID + " REAL"
            + ");";

    // - Drop
    private static final String DROP_TABLE = "DROP TABLE ";


    private SQLiteHelper(Context context) {
        super(context, DATA_BASE_NAME, null, DATA_BASE_VERSION);
    }


    public static SQLiteHelper getInstance(Context context) {

        if (instance == null) {
            instance = new SQLiteHelper(context);
        }

        return instance;
    }



    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_TABLE_BANKS);
        db.execSQL(CREATE_TABLE_CURRENCIES);
        db.execSQL(CREATE_TABLE_REGIONS_LIST);
        db.execSQL(CREATE_TABLE_CITIES_LIST);
        db.execSQL(CREATE_TABLE_ORG_TYPE);
        db.execSQL(CREATE_TABLE_ORG_CURRENCIES);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL(DROP_TABLE + TABLE_NAME_BANKS);
        db.execSQL(DROP_TABLE + TABLE_NAME_CURRENCIES);
        db.execSQL(DROP_TABLE + TABLE_NAME_REGIONS);
        db.execSQL(DROP_TABLE + TABLE_NAME_CITIES);
        db.execSQL(DROP_TABLE + TABLE_NAME_ORG_TYPE);
        db.execSQL(DROP_TABLE + TABLE_NAME_ORGANIZATIONS_CURRENCIES);

        onCreate(db);

    }
}
