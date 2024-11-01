package com.hub.contextawaretaskmanagement.Rules.GeneralActions.Services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;

public class TexterService extends BroadcastReceiver {

    private static boolean isSomeoneTexting = false;

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
                Object[] pdus = (Object[]) intent.getExtras().get("pdus");
                if (pdus != null) {
                    for (Object pdu : pdus) {
                        SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdu);
                        isSomeoneTexting = true;
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean isSomeoneTexting() {
        return isSomeoneTexting;
    }
}
