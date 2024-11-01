package com.hub.contextawaretaskmanagement.Rules.GeneralActions.Services;

import android.content.Context;
import android.telephony.SmsManager;

import com.hub.contextawaretaskmanagement.General.NotificationCenter;

public class MessageService {

    private NotificationCenter nc = new NotificationCenter();


    public void startMessageService(String phoneNumber, String message, Context context) {
        try {
            SmsManager sms = SmsManager.getDefault();
            sms.sendTextMessage(phoneNumber, null, message, null, null);
        } catch (Exception e) {
            e.printStackTrace();
            nc.notify(context, "Message to " + phoneNumber + "failed.");
        }
    }
}
