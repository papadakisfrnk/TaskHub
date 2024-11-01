package com.hub.contextawaretaskmanagement.General;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.hub.contextawaretaskmanagement.R;

public class GeneralSettings extends AppCompatActivity {

    private AudioManager audioManager;
    private BroadcastReceiver interruptionFilterReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.System.canWrite(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                intent.setData(Settings.System.canWrite(this) ? null : Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, 200);
            }
        }

        setBrightness();
        setVolume();
        setDoNotDisturbSwitch();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Register receiver to update Do Not Disturb switch when interruption filter changes
        registerInterruptionFilterReceiver();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Unregister receiver to avoid leaks
        unregisterReceiver(interruptionFilterReceiver);
    }

    private void registerInterruptionFilterReceiver() {
        interruptionFilterReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                updateDoNotDisturbSwitch();
            }
        };
        IntentFilter filter = new IntentFilter(AudioManager.RINGER_MODE_CHANGED_ACTION);
        registerReceiver(interruptionFilterReceiver, filter);
    }

    private void setBrightness() {
        // Implement setBrightness method
    }

    private void setVolume() {
        // Implement setVolume method
    }

    private void setDoNotDisturbSwitch() {
        Switch doNotDisturbSwitch = findViewById(R.id.do_not_disturb_switch);
        boolean isDoNotDisturbEnabled = isDoNotDisturbEnabled();
        doNotDisturbSwitch.setChecked(isDoNotDisturbEnabled);
        doNotDisturbSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            setDoNotDisturbMode(isChecked);
            Toast.makeText(this, isChecked ? "Do Not Disturb enabled" : "Do Not Disturb disabled", Toast.LENGTH_SHORT).show();
        });
    }

    private void updateDoNotDisturbSwitch() {
        Switch doNotDisturbSwitch = findViewById(R.id.do_not_disturb_switch);
        boolean isDoNotDisturbEnabled = isDoNotDisturbEnabled();
        doNotDisturbSwitch.setChecked(isDoNotDisturbEnabled);
    }

    private boolean isDoNotDisturbEnabled() {
        return audioManager.getRingerMode() == AudioManager.RINGER_MODE_SILENT;
    }

    private void setDoNotDisturbMode(boolean enable) {
        if (enable) {
            audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
        } else {
            audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        }
    }
}
