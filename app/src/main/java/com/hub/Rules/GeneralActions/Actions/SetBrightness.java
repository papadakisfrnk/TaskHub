package com.hub.contextawaretaskmanagement.Rules.GeneralActions.Actions;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

import com.hub.contextawaretaskmanagement.Fragments.GeneralSettingsFragment;
import com.hub.contextawaretaskmanagement.Fragments.LogsFragment;
import com.hub.contextawaretaskmanagement.Fragments.MyRulesFragment;
import com.hub.contextawaretaskmanagement.Fragments.PhoneFragment;
import com.hub.contextawaretaskmanagement.Fragments.ShowAllFragment;
import com.hub.contextawaretaskmanagement.General.MainActivity;
import com.hub.contextawaretaskmanagement.Logs.LogStorage;
import com.hub.contextawaretaskmanagement.R;
import com.hub.contextawaretaskmanagement.Rules.Rule.CurrentRule;
import com.hub.contextawaretaskmanagement.Rules.Rule.RuleAction;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class SetBrightness extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private SeekBar seekBar;
    private Button button_ok;
    private BottomNavigationView bottomNavigationView;
    private List<RuleAction> actionList = new ArrayList<>();

    private TextView brightTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_brightness);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        brightTextView = findViewById(R.id.textViewBrightnessPercentage);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setCustomView(R.layout.action_bar_layout);
            View customView = actionBar.getCustomView();
            AppCompatTextView titleTextView = customView.findViewById(R.id.title_actionbar);
            titleTextView.setText("Set Brightness");
        }

        button_ok = findViewById(R.id.buttonOK);
        actionList = CurrentRule.getCur_rule().getActionsList();

        seekBar = findViewById(R.id.seekBarBrightness);

        int currentBrightness = Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, 0);
        seekBar.setProgress(currentBrightness);
        brightTextView.setText(getString(R.string.brightness_percentage, (currentBrightness * 100) / 100));

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                brightTextView.setText(getString(R.string.brightness_percentage, (progress * 100) / 100));
                CurrentRule.getCur_rule().setEmpty(false);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        button_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences prefsMode = getSharedPreferences("ValueMode", MODE_PRIVATE);
                int mode = prefsMode.getInt("modeKey", 0);
                if (mode == 3) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SetBrightness.this);
                    builder.setTitle("You are in Interactive Mode.")
                            .setMessage("Are you sure you want to perform the action?")
                            .setPositiveButton("Confirm", (dialog, which) -> {

                                int brightnessValue = seekBar.getProgress();

                                RuleAction ruleAction = new RuleAction("Set Brightness", String.valueOf(brightnessValue));
                                LogStorage.setLog(SetBrightness.this, "Action Set Brightness (" + brightnessValue + ") added to the Rule " + CurrentRule.getCur_rule().getName());
                                actionList.add(ruleAction);
                                navigateToMainActivity();


                            })
                            .setNegativeButton("Cancel", null)
                            .create()
                            .show();

                } else {
                    int brightnessValue = seekBar.getProgress();
                    RuleAction ruleAction = new RuleAction("Set Brightness", String.valueOf(brightnessValue));
                    LogStorage.setLog(SetBrightness.this, "Action Set Brightness (" + brightnessValue + ") added to the Rule " + CurrentRule.getCur_rule().getName());
                    actionList.add(ruleAction);

                   navigateToMainActivity();

                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_home_menu:
                navigateToMainActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("username", MainActivity.username);
        intent.putExtra("email", MainActivity.email);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.myrulesmenu:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.flFragment, new MyRulesFragment())
                        .commit();
                button_ok.setVisibility(View.GONE);
                return true;
            case R.id.phonemenu:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.flFragment, new PhoneFragment())
                        .commit();
                button_ok.setVisibility(View.GONE);
                return true;
            case R.id.appsmenu:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.flFragment, new ShowAllFragment())
                        .commit();
                button_ok.setVisibility(View.GONE);
                return true;
            case R.id.settingsmenu:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.flFragment, new GeneralSettingsFragment())
                        .commit();
                button_ok.setVisibility(View.GONE);
                return true;
            case R.id.logsmenu:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.flFragment, new LogsFragment())
                        .commit();
                button_ok.setVisibility(View.GONE);
                return true;
        }
        return false;
    }
}
