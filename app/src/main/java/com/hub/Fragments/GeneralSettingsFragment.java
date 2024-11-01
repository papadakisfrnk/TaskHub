package com.hub.contextawaretaskmanagement.Fragments;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;

import com.hub.contextawaretaskmanagement.General.NotificationCenter;
import com.hub.contextawaretaskmanagement.R;

public class GeneralSettingsFragment extends Fragment {

    private Context context;
    private WifiManager wifiManager;
    private TextView wifiStatusTextView;
    private NotificationCenter notificationCenter = new NotificationCenter();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_settings, container, false);

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setCustomView(R.layout.action_bar_layout);
            View customView = actionBar.getCustomView();
            AppCompatTextView titleTextView = customView.findViewById(R.id.title_actionbar);
            titleTextView.setText("Settings");
        }
        setBrightness(view);
        setVolume(view);
        setWifi(view);
        toggleDoNotDisturb(view);

        return view;
    }

    private void setBrightness(View view) {
        SeekBar lightBar = view.findViewById(R.id.brightnessBar);
        context = requireContext();
        int brightness = Settings.System.getInt(context.getContentResolver(),
                Settings.System.SCREEN_BRIGHTNESS, 0);
        lightBar.setProgress(brightness);
        lightBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Settings.System.putInt(context.getContentResolver(),
                        Settings.System.SCREEN_BRIGHTNESS, progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    private void setVolume(View view) {
        SeekBar vlmBar = view.findViewById(R.id.volumeBar);
        AudioManager audioManager = (AudioManager) requireActivity().getSystemService(Context.AUDIO_SERVICE);

        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        vlmBar.setMax(maxVolume);

        int currVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        vlmBar.setProgress(currVolume);

        vlmBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, i, AudioManager.FLAG_SHOW_UI);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Do Nothing
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Do Nothing
            }
        });
    }

    private void setWifi(View view) {
        wifiManager = (WifiManager) requireContext().getSystemService(Context.WIFI_SERVICE);
        wifiStatusTextView = view.findViewById(R.id.wifi_switch);
        Switch wifiSwitch = view.findViewById(R.id.wifi_switch);

        if (wifiManager.isWifiEnabled()) {
            wifiStatusTextView.setText("Wi-Fi is ON");
            wifiSwitch.setChecked(true);
        } else {
            wifiStatusTextView.setText(("Wi-Fi is OFF"));
            wifiSwitch.setChecked(false);
        }

        wifiSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    wifiManager.setWifiEnabled(true);
                    wifiStatusTextView.setText("Wi-Fi is ON");
                    notificationCenter.notify(requireContext(), "Wi-Fi is ON");
                    Toast.makeText(requireContext(), "Wi-Fi may take a moment to Turn ON", Toast.LENGTH_LONG).show();
                } else {
                    wifiManager.setWifiEnabled(false);
                    notificationCenter.notify(requireContext(), "Wi-Fi is OFF");
                    wifiStatusTextView.setText("Wi-Fi is OFF");
                }
            }
        });
    }

    private void toggleDoNotDisturb(View view) {
        Switch doNotDisturbSwitch = view.findViewById(R.id.do_not_disturb_switch);

        doNotDisturbSwitch.setOnCheckedChangeListener((buttonView, isChecked) ->
                setDoNotDisturbMode(isChecked));

        boolean isDoNotDisturbEnabled = isDoNotDisturbEnabled();
        doNotDisturbSwitch.setChecked(isDoNotDisturbEnabled);
        setDoNotDisturbMode(isDoNotDisturbEnabled);
    }

    private boolean isDoNotDisturbEnabled() {
        NotificationManager notificationManager =
                (NotificationManager) requireContext().getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return notificationManager.getCurrentInterruptionFilter() == NotificationManager.INTERRUPTION_FILTER_NONE;
        }

        return false;
    }

    private void setDoNotDisturbMode(boolean enable) {
        NotificationManager notificationManager =
                (NotificationManager) requireContext().getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (notificationManager.isNotificationPolicyAccessGranted()) {
                if (enable) {
                    notificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_NONE);
                } else {
                    notificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_ALL);
                }
            } else {
                Intent intent = new Intent(android.provider.Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
                startActivity(intent);
            }
        }
    }


}
