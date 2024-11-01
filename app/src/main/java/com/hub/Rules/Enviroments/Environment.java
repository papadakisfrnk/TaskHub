package com.hub.contextawaretaskmanagement.Rules.Enviroments;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

import com.hub.contextawaretaskmanagement.General.MainActivity;
import com.hub.contextawaretaskmanagement.R;
import com.hub.contextawaretaskmanagement.Rules.Conditions.Condition;
import com.hub.contextawaretaskmanagement.Rules.Rule.CurrentRule;
import com.hub.contextawaretaskmanagement.Rules.Rule.Rule;

import java.util.ArrayList;
import java.util.List;


public class Environment extends AppCompatActivity {
    private ImageView imageViewDateTime;
    private ImageView imageViewBatteryLevel;
    private ImageView imageViewPhone;
    private ImageView imageViewLocation;
    private Rule cur_rule = CurrentRule.getCur_rule();

    private List<Condition> conditionList = new ArrayList<>();
    private String operator = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enviroment_rules);
        setTitle(cur_rule.getName());

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setCustomView(R.layout.action_bar_layout);
            View customView = actionBar.getCustomView();
            AppCompatTextView titleTextView = customView.findViewById(R.id.title_actionbar);
            titleTextView.setText("Environments");
        }
        operator = getIntent().getStringExtra("operator");

        conditionList = cur_rule.getConditionsList();
        imageViewDateTime = findViewById(R.id.image_date_time);
        imageViewBatteryLevel = findViewById(R.id.image_battery);
        imageViewPhone = findViewById(R.id.image_phone);
        //imageViewEmail = findViewById(R.id.image_email);
        imageViewLocation = findViewById(R.id.image_location);


        imageViewDateTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean thereis = false;
                if (!conditionList.isEmpty()) {
                    for (Condition cond : conditionList) {
                        if (cond.getTime() != null) {
                            if (operator.equals("AND") || operator.equals("NOT")) {
                                thereis = true;
                                Toast.makeText(getApplicationContext(), "You can not add the Same Enviroment with " + operator, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                    if (!thereis) {
                        Intent intent = new Intent(Environment.this, Date_Time.class);
                        intent.putExtra("operator", operator);
                        startActivity(intent);
                    }
                } else {
                    Intent intent = new Intent(Environment.this, Date_Time.class);
                    intent.putExtra("operator", operator);
                    startActivity(intent);
                }
            }
        });


        imageViewBatteryLevel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean thereis = false;
                if (!conditionList.isEmpty()) {
                    for (Condition cond : conditionList) {
                        if (cond.getBatteryLevel() != 0) {
                            if (operator.equals("AND") || operator.equals("NOT")) {
                                thereis = true;
                                Toast.makeText(getApplicationContext(), "You can not add the Same Enviroment with " + operator, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                    if (!thereis) {
                        Intent intent = new Intent(Environment.this, BatteryLevel.class);
                        intent.putExtra("operator", operator);
                        startActivity(intent);
                    }
                } else {
                    Intent intent = new Intent(Environment.this, BatteryLevel.class);
                    intent.putExtra("operator", operator);
                    startActivity(intent);
                }
            }
        });

        imageViewPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean thereis = false;
                if (!conditionList.isEmpty()) {
                    for (Condition cond : conditionList) {
                        if (cond.getPhoneId() != null) {
                            if (operator.equals("AND") || operator.equals("NOT")) {
                                thereis = true;
                                Toast.makeText(getApplicationContext(), "You can not add the Same Enviroment with " + operator, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                    if (!thereis) {
                        Intent intent = new Intent(Environment.this, Someone_Calling_Texting.class);
                        intent.putExtra("operator", operator);
                        startActivity(intent);
                    }
                } else {
                    Intent intent = new Intent(Environment.this, Someone_Calling_Texting.class);
                    intent.putExtra("operator", operator);
                    startActivity(intent);
                }
            }
        });

        imageViewLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean thereis = false;
                if (!conditionList.isEmpty()) {
                    for (Condition cond : conditionList) {
                        if (cond.getLocation() != null) {
                            if (operator.equals("AND") || operator.equals("NOT")) {
                                thereis = true;
                                Toast.makeText(getApplicationContext(), "You can not add the Same Enviroment with " + operator, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                    if (!thereis) {
                        Intent intent = new Intent(Environment.this, MapLocation.class);
                        intent.putExtra("operator", operator);
                        startActivity(intent);
                    }
                } else {
                    Intent intent = new Intent(Environment.this, MapLocation.class);
                    intent.putExtra("operator", operator);
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
}
