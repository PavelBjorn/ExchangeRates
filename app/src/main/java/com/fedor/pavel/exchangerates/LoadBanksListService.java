package com.fedor.pavel.exchangerates;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.fedor.pavel.exchangerates.data.SQLiteManager;
import com.fedor.pavel.exchangerates.listeners.ConnectionListener;
import com.fedor.pavel.exchangerates.data.DataArray;
import com.fedor.pavel.exchangerates.network.NetManager;
import com.fedor.pavel.exchangerates.notification.NotificationSender;
import com.google.gson.GsonBuilder;

import java.sql.SQLException;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class LoadBanksListService extends IntentService implements ConnectionListener {


    private static final String LOG_TAG = "BanksServiceTag";
    private SQLiteManager sqLiteManager;
    private boolean startFromApp;


    public LoadBanksListService() {
        super("LoadBanksListService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(LOG_TAG, "onHandleIntent");
        startFromApp = intent.getBooleanExtra(MainActivity.FIRST_START, false);
        NetManager netManager = new NetManager(this, MainActivity.API, this);

    }

    @Override
    public void onStarConnection(String request) {
        Log.d(LOG_TAG, "onStarConnection");
    }

    @Override
    public void onConnectionFailed(int reason) {

        Log.d(LOG_TAG, "onConnectionFailed");

        if (startFromApp) {
            Intent intent = new Intent(MainActivity.ACTION_CONNECTION_FAILED);
            intent.putExtra(NetManager.FAILED_CONNECTION_REASON, reason);
            sendBroadcast(intent);
        }

    }

    @Override
    public void onFinishConnection(String response) {

        sqLiteManager = SQLiteManager.getInstance(getApplicationContext());
        try {
            sqLiteManager.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        DataArray data = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().fromJson(response, DataArray.class);
        data.saveDataToDb();

        if (!startFromApp) {
            sqLiteManager.close();
        }

        Intent intent = new Intent(MainActivity.ACTION_CONNECTION_FINISHED);
        sendBroadcast(intent);

        NotificationSender sender = new NotificationSender(this);
        sender.sendNotification(getResources().getText(R.string.app_name).toString(), "Данные обновлены", startFromApp);

        Log.d(LOG_TAG, "onFinishConnection");

    }
}
