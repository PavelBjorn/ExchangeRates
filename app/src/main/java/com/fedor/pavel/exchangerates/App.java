package com.fedor.pavel.exchangerates;

import android.app.Application;

import com.fedor.pavel.exchangerates.data.PreferencesManager;

public class App extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        PreferencesManager.setContext(this);
    }

}
