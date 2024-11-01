package com.hub.contextawaretaskmanagement.Rules.GeneralActions.Actions;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

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

public class SetDnd extends AppCompatActivity implements BottomNavigationView
        .OnNavigationItemSelectedListener {
    private Button enable, disable;
    private BottomNavigationView bottomNavigationView;
    private List<RuleAction> actionList = new ArrayList<>();

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_dnd);

        enable = findViewById(R.id.enableDND);
        disable = findViewById(R.id.disableDND);

        actionList = CurrentRule.getCur_rule().getActionsList();


        bottomNavigationView
                = findViewById(R.id.bottomNavigationView);

        bottomNavigationView
                .setOnNavigationItemSelectedListener(this);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setCustomView(R.layout.action_bar_layout);
            View customView = actionBar.getCustomView();
            AppCompatTextView titleTextView = customView.findViewById(R.id.title_actionbar);
            titleTextView.setText("Enable or Disable DND");
        }

        enable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CurrentRule.getCur_rule().setEmpty(false);

                SharedPreferences prefsMode = getSharedPreferences("ValueMode", MODE_PRIVATE);
                int mode = prefsMode.getInt("modeKey", 0);
                if (mode == 3) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SetDnd.this);
                    builder.setTitle("You are in Interactive Mode.")
                            .setMessage("Are you sure you want to perform the action?")
                            .setPositiveButton("Confirm", (dialog, which) -> {
                                RuleAction ruleAction = new RuleAction("Set DND", "Enable");
                                actionList.add(ruleAction);
                                LogStorage.setLog(SetDnd.this, "Action Set DND (Enable) added to the Rule " + CurrentRule.getCur_rule().getName());
                                navigateToMainActivity();


                            })
                            .setNegativeButton("Cancel", null)
                            .create()
                            .show();
                } else {
                    RuleAction ruleAction = new RuleAction("Set DND", "Enable");
                    actionList.add(ruleAction);
                    LogStorage.setLog(SetDnd.this, "Action Set DND (Enable) added to the Rule " + CurrentRule.getCur_rule().getName());
                    navigateToMainActivity();

                }


            }
        });

        disable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CurrentRule.getCur_rule().setEmpty(false);

                RuleAction ruleAction = new RuleAction("Set DND", "Disable");
                actionList.add(ruleAction);


                LogStorage.setLog(SetDnd.this, "Action Set DND (Disable) added to the Rule " + CurrentRule.getCur_rule().getName());


                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.flFragment, new MyRulesFragment())
                        .commit();
                disable.setVisibility(View.GONE);
                enable.setVisibility(View.GONE);
            }
        });


    }


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
    public boolean
    onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.myrulesmenu:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.flFragment, new MyRulesFragment())
                        .commit();
                disable.setVisibility(View.GONE);
                enable.setVisibility(View.GONE);

                return true;

            case R.id.phonemenu:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.flFragment, new PhoneFragment())
                        .commit();
                disable.setVisibility(View.GONE);
                enable.setVisibility(View.GONE);

                return true;

            case R.id.appsmenu:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.flFragment, new ShowAllFragment())
                        .commit();
                disable.setVisibility(View.GONE);
                enable.setVisibility(View.GONE);

                return true;

            case R.id.settingsmenu:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.flFragment, new GeneralSettingsFragment())
                        .commit();
                disable.setVisibility(View.GONE);
                enable.setVisibility(View.GONE);

                return true;

            case R.id.logsmenu:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.flFragment, new LogsFragment())
                        .commit();
                disable.setVisibility(View.GONE);
                enable.setVisibility(View.GONE);

                return true;

        }
        return false;
    }

}


