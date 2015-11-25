package com.fedor.pavel.exchangerates.notification;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;

import com.fedor.pavel.exchangerates.MainActivity;
import com.fedor.pavel.exchangerates.R;
import com.fedor.pavel.exchangerates.SettingsActivity;
import com.fedor.pavel.exchangerates.data.PreferencesManager;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class NotificationSender {


    private Context context;
    private static final String SOUND_URL = "android.resource://com.fedor.pavel.exchangerates/raw/notification";

    public NotificationSender(Context context) {
        this.context = context;
    }

    public void sendNotification(String title, String text, boolean isFirstStart) {

        SharedPreferences pref = PreferencesManager.getInstance().loadSettings();

        if (!isFirstStart) {

            if (PreferencesManager.getInstance().loadSettings().getBoolean(SettingsActivity.NOTIFICATION_KEY, true)) {
                Intent intent = new Intent(context, MainActivity.class);
                PendingIntent pi = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

                Calendar calendar = new GregorianCalendar();


                long currentTimeMill = calendar.get(Calendar.MINUTE) * 1000 * 60
                        + calendar.get(Calendar.HOUR_OF_DAY) * 1000 * 60 * 60;

                long timeFromMill = pref.getInt(SettingsActivity.NOTIFICATION_MIN_FROM_KEY, 0) * 1000 * 60
                        + pref.getInt(SettingsActivity.NOTIFICATION_HOUR_FROM_KEY, 0) * 1000 * 60 * 60;

                long timeToMill = pref.getInt(SettingsActivity.NOTIFICATION_MIN_TO_KEY, 0) * 1000 * 60
                        + pref.getInt(SettingsActivity.NOTIFICATION_HOUR_TO_KEY, 0) * 1000 * 60 * 60;


                if ((timeFromMill <= currentTimeMill && currentTimeMill <= timeToMill)
                        || (timeFromMill >= currentTimeMill && currentTimeMill >= timeToMill)
                        || (timeFromMill == 0 && timeToMill == 0)
                        || (timeFromMill >= currentTimeMill && currentTimeMill <= timeToMill)) {

                    Notification.Builder builder = new Notification.Builder(context);
                    builder.setSmallIcon(R.mipmap.ic_launcher); // the status icon
                    builder.setTicker(text);// the status text
                    builder.setWhen(System.currentTimeMillis());// the time stamp
                    builder.setContentTitle(title); // the label of the entry
                    builder.setContentText(text);// the contents of the entry
                    builder.setContentIntent(pi);

                    if (pref.getBoolean(SettingsActivity.NOTIFICATION_SOUND_KEY, true)) {
                        builder.setSound(Uri.parse(SOUND_URL));
                    }
                    if (pref.getBoolean(SettingsActivity.NOTIFICATION_VIBRATION_KEY, true)) {
                        builder.setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});
                    }


                    Notification notification;
                    if (Build.VERSION.SDK_INT >= 16) {
                        notification = builder.build();
                    } else {
                        notification = builder.getNotification();
                    }

                    NotificationManager mNM = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    mNM.notify(1, notification);
                }
            }
        }
    }


}
