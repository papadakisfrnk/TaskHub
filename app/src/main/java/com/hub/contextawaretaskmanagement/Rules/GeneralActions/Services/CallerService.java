package com.hub.contextawaretaskmanagement.Rules.GeneralActions.Services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;

public class CallerService extends BroadcastReceiver {

    private static boolean isSomeoneCalling = false;

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
            if (state != null && state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                System.out.println(true);
                isSomeoneCalling = true;
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean isSomeoneCalling() {
        return isSomeoneCalling;
    }
}
