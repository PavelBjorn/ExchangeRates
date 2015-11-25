package com.fedor.pavel.exchangerates.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.fedor.pavel.exchangerates.SettingsActivity;


public class PreferencesManager {


    private static PreferencesManager instance;
    private static Context context;

    private static final String APP_SETTINGS_FILE_NAME = "app_settings";


    private PreferencesManager() {

    }

    public static PreferencesManager getInstance() {

        if (instance == null) {
            instance = new PreferencesManager();
        }

        return instance;
    }


    public void saveSettings(
            boolean sendNotification,
            boolean havSound,
            boolean havVibration, int minFrom, int hourFrom, int minTo, int hourTo, int timeFrequency) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(APP_SETTINGS_FILE_NAME, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(SettingsActivity.NOTIFICATION_KEY, sendNotification);
        editor.putBoolean(SettingsActivity.NOTIFICATION_SOUND_KEY, havSound);
        editor.putBoolean(SettingsActivity.NOTIFICATION_VIBRATION_KEY, havVibration);

        editor.putInt(SettingsActivity.NOTIFICATION_MIN_FROM_KEY, minFrom);
        editor.putInt(SettingsActivity.NOTIFICATION_HOUR_FROM_KEY, hourFrom);

        editor.putInt(SettingsActivity.NOTIFICATION_MIN_TO_KEY, minTo);
        editor.putInt(SettingsActivity.NOTIFICATION_HOUR_TO_KEY, hourTo);

        editor.putInt(SettingsActivity.UPDATE_FREQUENCY_KEY, timeFrequency);

        editor.commit();
    }

    public void saveAppStatus(int appStatus) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(APP_SETTINGS_FILE_NAME, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(SettingsActivity.APP_STATUS_KEY, appStatus);

    }

    public SharedPreferences loadSettings() {
        return context.getSharedPreferences(APP_SETTINGS_FILE_NAME, context.MODE_PRIVATE);
    }

    public static void setContext(Context context) {
        PreferencesManager.context = context;
    }
}
