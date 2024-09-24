package com.hub.contextawaretaskmanagement.Rules.GeneralActions.Services;

import android.content.Context;
import android.provider.Settings;
import android.widget.Toast;

import com.hub.contextawaretaskmanagement.General.NotificationCenter;

public class BrightnessService {

    private NotificationCenter nc = new NotificationCenter();

    public void startBrightnessService(String brightness, Context context) {

        String numericPart = brightness.replaceAll("[^\\d.]", "");

        int brightnessPercentage = Integer.parseInt(numericPart);

        // Adjust brightness level
        int brightnessLevel = (int) (brightnessPercentage);

        try {
            // Set brightness level
            Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, brightnessLevel);

            // Notify user about the change
            Toast.makeText(context, "Screen brightness has been set to " + brightnessPercentage + "%", Toast.LENGTH_SHORT).show();
        } catch (SecurityException e) {
            // Catch SecurityException if permission to modify system settings is not granted
            Toast.makeText(context, "Permission denied to change brightness settings", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}
