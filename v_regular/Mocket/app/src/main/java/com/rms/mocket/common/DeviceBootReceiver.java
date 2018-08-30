package com.rms.mocket.common;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.rms.mocket.activities.MainActivity;

/**
 * @author Nilanchala
 *         <p/>
 *         Broadcast reciever, starts when the device gets starts.
 *         Start your repeating alarm here.
 */
public class DeviceBootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            MainActivity.startAlarm(context);
        }
    }
}