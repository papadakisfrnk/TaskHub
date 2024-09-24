package com.hub.contextawaretaskmanagement.General;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.hub.contextawaretaskmanagement.Fragments.GeneralSettingsFragment;
import com.hub.contextawaretaskmanagement.Fragments.LogsFragment;
import com.hub.contextawaretaskmanagement.Fragments.MyRulesFragment;
import com.hub.contextawaretaskmanagement.Fragments.PhoneFragment;
import com.hub.contextawaretaskmanagement.Fragments.ShowAllFragment;
import com.hub.contextawaretaskmanagement.General.Account.AuthenticationLogin;
import com.hub.contextawaretaskmanagement.R;
import com.hub.contextawaretaskmanagement.Rules.Conditions.MyConditions;
import com.hub.contextawaretaskmanagement.Rules.Rule.CurrentRule;
import com.hub.contextawaretaskmanagement.Rules.Rule.GiveRuleName;
import com.hub.contextawaretaskmanagement.Rules.Rule.Rule;
import com.hub.contextawaretaskmanagement.Rules.Rule.SharedPreferencesHelper;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements BottomNavigationView
        .OnNavigationItemSelectedListener {
    private static final int REQUEST_CODE = 1;
    private Button management;
    private BottomNavigationView bottomNavigationView;
    private Rule rule;
    private ArrayList<Rule> rules;
    private SharedPreferencesHelper sharedPreferencesHelper;
    private NotificationCenter nc = new NotificationCenter();
    public static String email, username;


    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.accounts_menu, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView startTextView = findViewById(R.id.startTextView);

        Intent intent = getIntent();
        username = intent.getExtras().getString("username");

        startTextView.setText("Let's Start " + username);


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
            titleTextView.setText("TaskHub");
        }

        sharedPreferencesHelper = new SharedPreferencesHelper(this);
        rules = (ArrayList<Rule>) sharedPreferencesHelper.loadRules();

        if (CurrentRule.getCur_rule() != null)
            if (!CurrentRule.getCur_rule().isEmpty())
                sharedPreferencesHelper.updateRule(CurrentRule.getCur_rule());


        management = findViewById(R.id.buttonRuleManagement);

        management.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GiveRuleName.class);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {

            rule = CurrentRule.getCur_rule();
            rules.add(rule);
            Log.d("SAVERULES -MYRULES", rule.toString());

            sharedPreferencesHelper.saveRules(rules);

            Intent intent = new Intent(MainActivity.this, MyConditions.class);
            intent.putExtra("name", rule.getName());
            startActivity(intent);

        }
    }


    MyRulesFragment myRulesFragment = new MyRulesFragment();
    PhoneFragment phoneFragment = new PhoneFragment();
    ShowAllFragment showAllFragment = new ShowAllFragment();
    GeneralSettingsFragment generalSettingsFragment = new GeneralSettingsFragment();
    LogsFragment logsFragment = new LogsFragment();


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        SharedPreferences.Editor savedMode = getSharedPreferences("ValueMode", MODE_PRIVATE).edit();

        if (id == R.id.normal_account) {

            savedMode.putInt("modeKey", 1);
            nc.notify(MainActivity.this, "You are in Normal Mode.");
        }
        if (id == R.id.informative_account) {
            savedMode.putInt("modeKey", 2);
            nc.notify(MainActivity.this, "You are in Informative Mode.");
        }
        if (id == R.id.interactive_account) {
            savedMode.putInt("modeKey", 3);
            nc.notify(MainActivity.this, "You are in Interactive Mode.");

        }
        if (id == R.id.logout_account) {
            Intent intent = new Intent(MainActivity.this, AuthenticationLogin.class);
            startActivity(intent);
            nc.notify(MainActivity.this, "You have been Logout.");
        }
        savedMode.apply();

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
                        .replace(R.id.flFragment, myRulesFragment)
                        .commit();
                management.setVisibility(View.GONE);
                return true;

            case R.id.phonemenu:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.flFragment, phoneFragment)
                        .commit();
                management.setVisibility(View.GONE);
                return true;

            case R.id.appsmenu:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.flFragment, showAllFragment)
                        .commit();
                management.setVisibility(View.GONE);
                return true;

            case R.id.settingsmenu:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.flFragment, generalSettingsFragment)
                        .commit();
                management.setVisibility(View.GONE);
                return true;

            case R.id.logsmenu:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.flFragment, logsFragment)
                        .commit();
                management.setVisibility(View.GONE);
                return true;

        }
        return false;
    }
}