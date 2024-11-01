package com.hub.contextawaretaskmanagement.Rules.GeneralActions.Actions;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
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

import com.google.android.material.bottomnavigation.BottomNavigationView;
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

import java.util.ArrayList;
import java.util.List;

public class SetCall extends AppCompatActivity implements BottomNavigationView
        .OnNavigationItemSelectedListener {
    private EditText call_id;
    private Button button1;
    private BottomNavigationView bottomNavigationView;
    private List<RuleAction> actionList = new ArrayList<>();
    private static final int PICK_CONTACT = 1;

    private Button pickContactButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_call);

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
            titleTextView.setText("Set Your Call");
        }
        actionList = CurrentRule.getCur_rule().getActionsList();

        call_id = findViewById(R.id.callText);
        button1 = findViewById(R.id.callButton);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = call_id.getText().toString();
                SharedPreferences prefsMode = getSharedPreferences("ValueMode", MODE_PRIVATE);
                int mode = prefsMode.getInt("modeKey", 0);
                if (isValidPhoneNumber(number)) {
                    if(mode == 3) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(SetCall.this);
                        builder.setTitle("You are in Interactive Mode.")
                                .setMessage("Are you sure you want to perform the action?")
                                .setPositiveButton("Confirm", (dialog, which) -> {
                                    CurrentRule.getCur_rule().setEmpty(false);
                                    RuleAction ruleAction = new RuleAction("Set Call", number);
                                    actionList.add(ruleAction);
                                    LogStorage.setLog(SetCall.this, "Action Set Call (" + number + ") added to the Rule " + CurrentRule.getCur_rule().getName());
                                    navigateToMainActivity();

                                })
                                .setNegativeButton("Cancel", null)
                                .create()
                                .show();
                    }
                    else {
                        CurrentRule.getCur_rule().setEmpty(false);
                        RuleAction ruleAction = new RuleAction("Set Call", number);
                        actionList.add(ruleAction);
                        LogStorage.setLog(SetCall.this, "Action Set Call (" + number + ") added to the Rule " + CurrentRule.getCur_rule().getName());
                        navigateToMainActivity();

                    }




                } else {

                    Toast.makeText(SetCall.this, "Invalid phone number. Please enter at least 10 digits.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        pickContactButton = findViewById(R.id.pickContactButton);

        pickContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an intent to pick a contact
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
                startActivityForResult(intent, PICK_CONTACT);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_CONTACT && resultCode == RESULT_OK) {
            Uri contactUri = data.getData();
            String[] projection = new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER};

            Cursor cursor = getContentResolver().query(contactUri, projection, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                String number = cursor.getString(numberIndex);
                cursor.close();

                call_id.setText(number);
            }
        }
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
                button1.setVisibility(View.GONE);
                call_id.setVisibility(View.GONE);
                pickContactButton.setVisibility(View.GONE);


                return true;

            case R.id.phonemenu:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.flFragment, new PhoneFragment())
                        .commit();
                button1.setVisibility(View.GONE);
                call_id.setVisibility(View.GONE);
                pickContactButton.setVisibility(View.GONE);

                return true;

            case R.id.appsmenu:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.flFragment, new ShowAllFragment())
                        .commit();
                button1.setVisibility(View.GONE);
                call_id.setVisibility(View.GONE);
                pickContactButton.setVisibility(View.GONE);

                return true;

            case R.id.settingsmenu:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.flFragment, new GeneralSettingsFragment())
                        .commit();
                button1.setVisibility(View.GONE);
                call_id.setVisibility(View.GONE);
                pickContactButton.setVisibility(View.GONE);

                return true;

            case R.id.logsmenu:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.flFragment, new LogsFragment())
                        .commit();
                button1.setVisibility(View.GONE);
                call_id.setVisibility(View.GONE);
                pickContactButton.setVisibility(View.GONE);

                return true;

        }
        return false;
    }
}


