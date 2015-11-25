package com.fedor.pavel.exchangerates.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.fedor.pavel.exchangerates.listeners.ConnectionListener;
import com.fedor.pavel.exchangerates.models.BankModel;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Pavel on 11.10.2015.
 */
public class NetManager {


    private Context context;
    private static final String LOG_TAG = "NetConnectionTag";
    public static final String FAILED_CONNECTION_REASON = "failed_connection_reason";
    private ConnectionListener listener;

    /*Constants*/
    public static final int REASON_CODE_NO_INTERNET = 1;
    public static final int REASON_CODE_NO_CONNECTION_TO_SERVER = 2;
    public static final int REASON_CODE_THREAD_WAS_INTERRUPTED = 3;


    public NetManager(Context context, String request, ConnectionListener listener) {

        this.context = context;
        this.listener = listener;

        if (haveInternet()) {
            listener.onFinishConnection(createConnection(request));
        } else {
            listener.onConnectionFailed(REASON_CODE_NO_INTERNET);
        }

    }

    private String createConnection(String request) {

        String response = "";

        try {

            listener.onStarConnection(request);
            URL url = new URL(request);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                String line;
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                while ((line = reader.readLine()) != null) {
                    response += line;
                }
            } else {
                listener.onConnectionFailed(REASON_CODE_NO_CONNECTION_TO_SERVER);
            }

        } catch (IOException e) {

            Log.d(LOG_TAG, "" + e);
            listener.onConnectionFailed(REASON_CODE_THREAD_WAS_INTERRUPTED);


        }


        return response;
    }



    private boolean haveInternet() {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();

        if (ni == null) {
            return false;
        } else {
            return true;
        }

    }


}
