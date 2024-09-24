package com.hub.contextawaretaskmanagement.Rules.GeneralActions.Services;

import android.content.Context;
import android.media.AudioManager;
import android.widget.Toast;

import com.hub.contextawaretaskmanagement.General.NotificationCenter;

public class VolumeService{

    private NotificationCenter nc = new NotificationCenter();

    public void startVolumeService(String volume, Context context) {

        String numericPart = volume.replaceAll("[^\\d.]", "");

        int volumePercentage = Integer.parseInt(numericPart);
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

        if (audioManager != null) {
            int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
            int volumeLevel = (volumePercentage * maxVolume) / 100;
            volumeLevel = Math.min(Math.max(volumeLevel, 0), maxVolume);

            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volumeLevel, AudioManager.FLAG_SHOW_UI);

            Toast.makeText(context, "Phone Volume has been set to " + volumePercentage + "%", Toast.LENGTH_SHORT).show();
        }
    }
}
