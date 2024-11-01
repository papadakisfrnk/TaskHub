package com.hub.contextawaretaskmanagement.Rules.GeneralActions.Services;

import android.content.Context;
import android.net.wifi.WifiManager;

import com.hub.contextawaretaskmanagement.General.NotificationCenter;

public class WifiService {

    private NotificationCenter nc = new NotificationCenter();

    public void startWifiService(String is, Context context) {
        boolean enable = false;
        if (is.equals("Enable"))
            enable = true;
        else if (is.equals("Disable"))
            enable = false;

        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (wifiManager != null) {
            wifiManager.setWifiEnabled(enable);
        } else {
            nc.notify(context, "Wi-Fi control is not supported on this device.");
        }
    }
}
