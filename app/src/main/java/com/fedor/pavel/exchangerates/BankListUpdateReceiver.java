package com.fedor.pavel.exchangerates;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;

public class BankListUpdateReceiver extends BroadcastReceiver {




    public BankListUpdateReceiver() {


    }

    @Override
    public void onReceive(Context context, Intent intent) {

        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,"");
        wl.acquire();
        context.startService(new Intent(context, LoadBanksListService.class));
        wl.release();

    }
}
