package com.fedor.pavel.exchangerates;


import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;


import com.fedor.pavel.exchangerates.data.PreferencesManager;


public class SettingsActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, AdapterView.OnItemSelectedListener {


    /*Params*/
    private TextView tvTimeFrom, tvTimeTo, tvSound, tvVibration, tvFrom, tvTo, tvNotifTime;
    private CheckBox chbSound, chbVibration;
    private SwitchCompat sNotification;
    private Spinner spFrequency;
    private LinearLayout llNotifContainer;
    private ArrayAdapter<String> spFrequencyAdapter;

    /*Pref*/
    private int minFrom = 0;
    private int hourFrom = 0;
    private int minTo = 0;
    private int hourTo = 0;
    private boolean sendNotification = true;
    private boolean haveSound = true;
    private boolean haveVibration = true;
    private int updateFrequency = 2;
    public static final String[] frequencyTime = {"30 cek", "10 мин", "20 мин", "30 мин", "45 мин", "1 ч", "2 ч", "3 ч", "1 д"};
    public static final long[] time = {30 * 1000, 10 * 1000 * 60, 20 * 1000 * 60,
            30 * 1000 * 60, 45 * 1000 * 60, 1000 * 60 * 60,
            1000 * 60 * 60, 2 * 1000 * 60 * 60,
            3 * 1000 * 60 * 60, 1000 * 60 * 60 * 24};

    /* Flag*/
    private int timeDialogFlag = 0;

    // Constants
    private final String LOG_TAG = "SettingsActTag";

    public static final int APP_STATUS_WORKING = 1;
    public static final int APP_STATUS_STOPED = 2;
    public static final int APP_STATUS_DESTROED = 3;

    // Keys
    public static final String APP_STATUS_KEY = "app_status";
    public static final String NOTIFICATION_KEY = "notification";
    public static final String NOTIFICATION_SOUND_KEY = "sound_notification";
    public static final String NOTIFICATION_VIBRATION_KEY = "vibration_notification";
    public static final String NOTIFICATION_MIN_FROM_KEY = "min_from_notification";
    public static final String NOTIFICATION_HOUR_FROM_KEY = "hour_from_notification";
    public static final String NOTIFICATION_MIN_TO_KEY = "min_to_notification";
    public static final String NOTIFICATION_HOUR_TO_KEY = "hour_to_notification";
    public static final String UPDATE_FREQUENCY_KEY = "time_frequency";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        spFrequencyAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, frequencyTime);
        setContentView(R.layout.activity_settings);
        loadPreferences();
        createView();

    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    private void createView() {


        prepareToolBar();

        tvFrom = (TextView) findViewById(R.id.activity_settings_tv_from);
        tvTo = (TextView) findViewById(R.id.activity_settings_tv_to);
        tvNotifTime = (TextView) findViewById(R.id.activity_settings_tv_time);
        tvVibration = (TextView) findViewById(R.id.activity_settings_tv_vibration);
        tvSound = (TextView) findViewById(R.id.activity_settings_tv_sound);
        tvTimeFrom = (TextView) findViewById(R.id.activity_settings_tv_timeFrom);
        tvTimeTo = (TextView) findViewById(R.id.activity_settings_tv_timeTo);
        chbSound = (CheckBox) findViewById(R.id.activity_settings_chb_sound);
        chbVibration = (CheckBox) findViewById(R.id.activity_settings_chb_vibration);
        sNotification = (SwitchCompat) findViewById(R.id.activity_settings_s_notification);
        spFrequency = (Spinner) findViewById(R.id.activity_settings_sp_frequency);
        spFrequency.setAdapter(spFrequencyAdapter);
        llNotifContainer = (LinearLayout) findViewById(R.id.activity_settings_notifSetings_container);

        isNotifSettingsEnable();

        tvTimeFrom.setText("" + (hourFrom == 0 || hourFrom < 10 ? "0" + hourFrom : hourFrom)
                + ":" + (minFrom == 0 || minFrom < 10 ? "0" + minFrom : minFrom));

        tvTimeTo.setText("" + (hourTo == 0 || hourTo < 10 ? "0" + hourTo : hourTo)
                + ":" + (minTo == 0 || minTo < 10 ? "0" + minTo : minTo));


        tvTimeFrom.setOnClickListener(this);
        tvTimeTo.setOnClickListener(this);
        spFrequency.setOnItemSelectedListener(this);

        chbSound.setOnCheckedChangeListener(this);
        chbVibration.setOnCheckedChangeListener(this);
        sNotification.setOnCheckedChangeListener(this);

        chbSound.setChecked(haveSound);
        chbVibration.setChecked(haveVibration);
        sNotification.setChecked(sendNotification);
        spFrequency.setSelection(updateFrequency);

    }

    private void prepareToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Настройки");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void createTimePickDialog(int min, int hour) {


        TimePickerDialog.OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                if (timeDialogFlag == 0) {

                    tvTimeFrom.setText("" + (hourOfDay == 0 || hourOfDay < 10 ? "0" + hourOfDay : hourOfDay)
                            + ":" + (minute == 0 || minute < 10 ? "0" + minute : minute));
                    minFrom = minute;
                    hourFrom = hourOfDay;
                } else {

                    tvTimeTo.setText("" + (hourOfDay == 0 || hourOfDay < 10 ? "0" + hourOfDay : hourOfDay)
                            + ":" + (minute == 0 || minute < 10 ? "0" + minute : minute));
                    minTo = minute;
                    hourTo = hourOfDay;
                }

                savePreferences();
            }
        };

        TimePickerDialog tpd = new TimePickerDialog(this, listener, hour, min, true);
        tpd.show();

    }

    private void loadPreferences() {

        SharedPreferences pref = PreferencesManager.getInstance().loadSettings();

        sendNotification = pref.getBoolean(NOTIFICATION_KEY, sendNotification);
        haveSound = pref.getBoolean(NOTIFICATION_SOUND_KEY, haveSound);
        haveVibration = pref.getBoolean(NOTIFICATION_VIBRATION_KEY, haveVibration);
        minFrom = pref.getInt(NOTIFICATION_MIN_FROM_KEY, minFrom);
        hourFrom = pref.getInt(NOTIFICATION_HOUR_FROM_KEY, hourFrom);
        minTo = pref.getInt(NOTIFICATION_MIN_TO_KEY, minTo);
        hourTo = pref.getInt(NOTIFICATION_HOUR_TO_KEY, hourTo);
        updateFrequency = pref.getInt(UPDATE_FREQUENCY_KEY, updateFrequency);

    }

    private void savePreferences() {

        PreferencesManager.getInstance().saveSettings(sendNotification,
                haveSound,
                haveVibration,
                minFrom,
                hourFrom,
                minTo,
                hourTo,
                updateFrequency);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.activity_settings_tv_timeFrom:

                timeDialogFlag = 0;
                createTimePickDialog(minFrom, hourFrom);

                break;

            case R.id.activity_settings_tv_timeTo:

                timeDialogFlag = 1;
                createTimePickDialog(minTo, hourTo);

                break;

        }


    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        switch (buttonView.getId()) {

            case R.id.activity_settings_chb_sound:

                haveSound = isChecked;
                savePreferences();
                break;

            case R.id.activity_settings_chb_vibration:

                haveVibration = isChecked;
                savePreferences();
                break;

            case R.id.activity_settings_s_notification:

                sendNotification = isChecked;
                savePreferences();
                isNotifSettingsEnable();

                break;


        }

    }

    private void isNotifSettingsEnable() {


        tvSound.setEnabled(sendNotification);
        tvVibration.setEnabled(sendNotification);
        chbSound.setEnabled(sendNotification);
        chbVibration.setEnabled(sendNotification);
        tvNotifTime.setEnabled(sendNotification);
        tvTimeTo.setEnabled(sendNotification);
        tvTimeFrom.setEnabled(sendNotification);
        tvFrom.setEnabled(sendNotification);
        tvTo.setEnabled(sendNotification);


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        updateFrequency = position;
        savePreferences();
        setResult(RESULT_OK);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            this.finish();
        }

        return true;
    }


}
