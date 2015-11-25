package com.fedor.pavel.exchangerates.listeners;

/**
 * Created by Pavel on 11.10.2015.
 */
public interface ConnectionListener {


     void onStarConnection(String request);

     void onConnectionFailed(int reason);

     void onFinishConnection(String response);



}
