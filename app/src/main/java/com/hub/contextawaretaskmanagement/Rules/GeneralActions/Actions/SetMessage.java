package com.hub.contextawaretaskmanagement.Rules.GeneralActions.Actions;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

public class SetMessage extends AppCompatActivity implements BottomNavigationView
        .OnNavigationItemSelectedListener {
    private EditText mobileno, message;
    private Button sendsms;
    private List<RuleAction> actionList = new ArrayList<>();
    private BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

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
            titleTextView.setText("Send Message");
        }

        actionList = CurrentRule.getCur_rule().getActionsList();


        mobileno = findViewById(R.id.messageMobile);
        message = findViewById(R.id.message);
        sendsms = findViewById(R.id.messageButton);


        sendsms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                String no = mobileno.getText().toString();
                String msg = message.getText().toString();

                SharedPreferences prefsMode = getSharedPreferences("ValueMode", MODE_PRIVATE);
                int mode = prefsMode.getInt("modeKey", 0);
                if (isValidPhoneNumber(no)) {
                    if (mode == 3) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(SetMessage.this);
                        builder.setTitle("You are in Interactive Mode.")
                                .setMessage("Are you sure you want to perform the action?")
                                .setPositiveButton("Confirm", (dialog, which) -> {
                                    CurrentRule.getCur_rule().setEmpty(false);
                                    RuleAction ruleAction = new RuleAction("Set Message", no, msg);
                                    actionList.add(ruleAction);
                                    LogStorage.setLog(SetMessage.this, "Action Set Message (" + no + ") and Message(" + msg + ") added to the Rule " + CurrentRule.getCur_rule().getName());
                                    navigateToMainActivity();

                                })
                                .setNegativeButton("Cancel", null)
                                .create()
                                .show();
                    } else {
                        CurrentRule.getCur_rule().setEmpty(false);
                        RuleAction ruleAction = new RuleAction("Set Message", no, msg);
                        actionList.add(ruleAction);
                        LogStorage.setLog(SetMessage.this, "Action Set Message (" + no + ") and Message(" + msg + ") added to the Rule " + CurrentRule.getCur_rule().getName());
                        navigateToMainActivity();

                    }


                } else {

                    Toast.makeText(SetMessage.this, "Invalid phone number. Please enter at least 10 digits.", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        String digitsOnly = phoneNumber.replaceAll("[^0-9]", "");

        return digitsOnly.length() >= 10;
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
                sendsms.setVisibility(View.GONE);

                return true;

            case R.id.phonemenu:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.flFragment, new PhoneFragment())
                        .commit();
                sendsms.setVisibility(View.GONE);

                return true;

            case R.id.appsmenu:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.flFragment, new ShowAllFragment())
                        .commit();
                sendsms.setVisibility(View.GONE);

                return true;

            case R.id.settingsmenu:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.flFragment, new GeneralSettingsFragment())
                        .commit();
                sendsms.setVisibility(View.GONE);

                return true;

            case R.id.logsmenu:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.flFragment, new LogsFragment())
                        .commit();
                sendsms.setVisibility(View.GONE);

                return true;

        }
        return false;
    }
}


