package com.hub.contextawaretaskmanagement.Rules.GeneralActions.Services;

import android.content.Context;
import android.media.AudioManager;
import android.os.Build;

import com.hub.contextawaretaskmanagement.General.NotificationCenter;

public class DoNotDisturbService {

    private NotificationCenter nc = new NotificationCenter();

    public void startDndService(String is, Context context) {
        boolean enable = false;
        if (is.equals("Enable"))
            enable = true;
        else if (is.equals("Disable"))
            enable = false;

        AudioManager audioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (audioManager != null) {

                audioManager.adjustStreamVolume(AudioManager.STREAM_NOTIFICATION, AudioManager.ADJUST_MUTE, 0);
                audioManager.adjustStreamVolume(AudioManager.STREAM_RING, AudioManager.ADJUST_MUTE, 0);
                audioManager.adjustStreamVolume(AudioManager.STREAM_ALARM, AudioManager.ADJUST_MUTE, 0);
                audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_MUTE, 0);
                audioManager.adjustStreamVolume(AudioManager.STREAM_VOICE_CALL, AudioManager.ADJUST_MUTE, 0);
                audioManager.setRingerMode(enable ? AudioManager.RINGER_MODE_SILENT : AudioManager.RINGER_MODE_NORMAL);
            }
        } else {
            nc.notify(context, "Do Not Disturb mode is not supported on this device.");
        }
    }
}