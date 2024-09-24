package com.hub.contextawaretaskmanagement.Rules.GeneralActions.Actions;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

import com.hub.contextawaretaskmanagement.Fragments.GeneralSettingsFragment;
import com.hub.contextawaretaskmanagement.Fragments.LogsFragment;
import com.hub.contextawaretaskmanagement.Fragments.MyRulesFragment;
import com.hub.contextawaretaskmanagement.Fragments.PhoneFragment;
import com.hub.contextawaretaskmanagement.Fragments.ShowAllFragment;
import com.hub.contextawaretaskmanagement.General.MainActivity;
import com.hub.contextawaretaskmanagement.R;
import com.hub.contextawaretaskmanagement.Rules.Conditions.MyConditions;
import com.hub.contextawaretaskmanagement.Rules.Rule.CurrentRule;
import com.hub.contextawaretaskmanagement.Rules.Rule.Rule;
import com.hub.contextawaretaskmanagement.Rules.Rule.RuleAction;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class MyAction extends AppCompatActivity implements BottomNavigationView
        .OnNavigationItemSelectedListener {

    private Rule cur_rule = CurrentRule.getCur_rule();

    private List<RuleAction> actionList;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actions);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setCustomView(R.layout.action_bar_layout);
            View customView = actionBar.getCustomView();
            AppCompatTextView titleTextView = customView.findViewById(R.id.title_actionbar);
            titleTextView.setText(cur_rule.getName());
        }
        bottomNavigationView
                = findViewById(R.id.bottomNavigationView);

        bottomNavigationView
                .setOnNavigationItemSelectedListener(this);

        actionList = CurrentRule.getCur_rule().getActionsList();


        Button buttonSetVolume = findViewById(R.id.buttonSetVolume);
        Button buttonEnableDND = findViewById(R.id.buttonEnableDND);
        Button buttonTurnOffWifi = findViewById(R.id.buttonTurnOffWifi);
        Button buttonMessage = findViewById(R.id.sendMessage);
        Button buttonCall = findViewById(R.id.callSomeone);
        Button buttonBright = findViewById(R.id.changeBrightness);


        buttonSetVolume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean exists = false;
                for (RuleAction ruleAction : actionList) {
                    if (ruleAction.getAction().equals("Set Volume")) {
                        exists = true;
                    }
                }
                if (exists == true) {
                    Toast.makeText(MyAction.this, "You cannot select System Volume Change because you have already added it!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MyAction.this, MyConditions.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(MyAction.this, SetVolume.class);
                    startActivity(intent);
                }

            }
        });

        buttonEnableDND.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean exists = false;
                for (RuleAction ruleAction : actionList) {
                    if (ruleAction.getAction().equals("Set DND")) {
                        exists = true;
                    }
                }
                if (exists == true) {
                    Toast.makeText(MyAction.this, "You cannot select DND Mode because you have already added it!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MyAction.this, MyConditions.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(MyAction.this, SetDnd.class);
                    startActivity(intent);
                }
            }

        });

        buttonTurnOffWifi.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                boolean exists = false;
                for (RuleAction ruleAction : actionList) {
                    if (ruleAction.getAction().equals("Set Wifi")) {
                        exists = true;
                    }
                }
                if (exists == true) {
                    Toast.makeText(MyAction.this, "You cannot select WiFi enable/disable because you have already added it!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MyAction.this, MyConditions.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(MyAction.this, SetWifi.class);
                    startActivity(intent);

                }
            }
        });

        buttonMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyAction.this, SetMessage.class);
                startActivity(intent);
            }
        });
        buttonCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean exists = false;
                for (RuleAction ruleAction : actionList) {
                    if (ruleAction.getAction().equals("Set Call")) {
                        exists = true;
                    }
                }
                if (exists == true) {
                    Toast.makeText(MyAction.this, "You cannot select Call Phone Number because you have already added it!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MyAction.this, MyConditions.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(MyAction.this, SetCall.class);
                    startActivity(intent);

                }
            }
        });
        buttonBright.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean exists = false;
                for (RuleAction ruleAction : actionList) {
                    if (ruleAction.getAction().equals("Set Brightness")) {
                        exists = true;
                    }
                }
                if (exists == true) {
                    Toast.makeText(MyAction.this, "You cannot select System Brightness Change because you have already added it!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MyAction.this, MyConditions.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(MyAction.this, SetBrightness.class);
                    startActivity(intent);

                }
            }
        });

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
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
                return true;

            case R.id.phonemenu:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.flFragment, new PhoneFragment())
                        .commit();
                return true;

            case R.id.appsmenu:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.flFragment, new ShowAllFragment())
                        .commit();
                return true;

            case R.id.settingsmenu:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.flFragment, new GeneralSettingsFragment())
                        .commit();
                return true;

            case R.id.logsmenu:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.flFragment, new LogsFragment())
                        .commit();
                return true;

        }
        return false;
    }
}


