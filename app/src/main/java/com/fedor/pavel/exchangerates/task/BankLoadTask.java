package com.fedor.pavel.exchangerates.task;

import android.os.AsyncTask;

import com.fedor.pavel.exchangerates.MainActivity;
import com.fedor.pavel.exchangerates.data.DataArray;
import com.fedor.pavel.exchangerates.listeners.DataUpdateListener;


public class BankLoadTask extends AsyncTask<Void, Void, DataArray> {


    private MainActivity activity;
    private DataUpdateListener listener;


    /*Constants*/
    private final String LOG_TAG = "BankLoadTaskTag";



    public BankLoadTask(MainActivity activity, DataUpdateListener listener) {
        this.activity = activity;
        this.listener = listener;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected DataArray doInBackground(Void... params) {

        return new DataArray().loadDataFromDb();
    }

    @Override
    protected void onPostExecute(DataArray dataSet) {
        super.onPostExecute(dataSet);
        listener.onUpdateBankList(dataSet);
    }
}
