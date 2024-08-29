package com.example.divedeep;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class BroadcastReceiver extends android.content.BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.ACTION_BATTERY_LOW.equals(intent.getAction()))
        {
            Toast.makeText(context, "Low Battery...", Toast.LENGTH_LONG).show();
        }

        if(intent.ACTION_POWER_DISCONNECTED.equals(intent.getAction()))
        {
            Toast.makeText(context, "Power Disconnected...", Toast.LENGTH_LONG).show();
        }

        if(intent.ACTION_POWER_CONNECTED.equals(intent.getAction()))
        {
            Toast.makeText(context, "Power Connected...", Toast.LENGTH_LONG).show();
        }
    }
}
