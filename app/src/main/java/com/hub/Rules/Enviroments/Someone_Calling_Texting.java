package com.hub.contextawaretaskmanagement.Rules.Enviroments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

import com.hub.contextawaretaskmanagement.General.MainActivity;
import com.hub.contextawaretaskmanagement.R;
import com.hub.contextawaretaskmanagement.Rules.Conditions.Condition;
import com.hub.contextawaretaskmanagement.Rules.Conditions.MyConditions;
import com.hub.contextawaretaskmanagement.Rules.Rule.CurrentRule;
import com.hub.contextawaretaskmanagement.Rules.Rule.Rule;
import com.hub.contextawaretaskmanagement.Rules.Rule.SharedPreferencesHelper;

import java.util.ArrayList;
import java.util.List;

public class Someone_Calling_Texting extends AppCompatActivity {

    private Button callingButton, textingButton;
    private Rule cur_rule = CurrentRule.getCur_rule();
    private SharedPreferencesHelper sharedPreferencesHelper;
    private String operator = "null";
    private List<Condition> conditionList = new ArrayList<>();
    private  boolean againCall,againText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_someone_calling_texting);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setCustomView(R.layout.action_bar_layout);
            View customView = actionBar.getCustomView();
            AppCompatTextView titleTextView = customView.findViewById(R.id.title_actionbar);
            titleTextView.setText(cur_rule.getName());
        }

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        operator = getIntent().getStringExtra("operator");

        sharedPreferencesHelper = new SharedPreferencesHelper(this);

        callingButton = findViewById(R.id.callerButton);
        textingButton = findViewById(R.id.textingButton);

        conditionList = cur_rule.getConditionsList();
        for(Condition cond : conditionList)
        {
            if(cond.getWaitToCall())
                againCall = true;
            if(cond.getWaitToText())
                againText = true;
        }

        callingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!conditionList.isEmpty()) {
                    if (operator.equals("AND") && againCall) {
                        Toast.makeText(getApplicationContext(), "You have already add this.", Toast.LENGTH_SHORT).show();
                    } else {
                        Condition new_condition = new Condition();

                        new_condition.setWaitToCall(true);
                        new_condition.setLogical_operator(operator);
                        CurrentRule.getCur_rule().setEmpty(false);
                        CurrentRule.getCur_rule().addCondition(new_condition);
                        editor.putBoolean("Wait_To_Call", true);
                        editor.apply();
                        boolean updated = sharedPreferencesHelper.addConditionToRule(new_condition, cur_rule);
                        if (!updated)
                            Log.e("Not updated", "NOT");
                        Intent intent = new Intent(Someone_Calling_Texting.this, MyConditions.class);
                        startActivity(intent);
                    }
                }else
                {
                    Condition new_condition = new Condition();

                    new_condition.setWaitToCall(true);
                    new_condition.setLogical_operator(operator);
                    CurrentRule.getCur_rule().setEmpty(false);
                    CurrentRule.getCur_rule().addCondition(new_condition);
                    editor.putBoolean("Wait_To_Call", true);
                    editor.apply();
                    boolean updated = sharedPreferencesHelper.addConditionToRule(new_condition, cur_rule);
                    if (!updated)
                        Log.e("Not updated", "NOT");
                    Intent intent = new Intent(Someone_Calling_Texting.this, MyConditions.class);
                    startActivity(intent);
                }

            }

        });
        textingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!conditionList.isEmpty()) {
                    if (operator.equals("AND") && againText) {
                        Toast.makeText(getApplicationContext(), "You have already add this.", Toast.LENGTH_SHORT).show();
                    } else {
                        Condition new_condition = new Condition();

                        new_condition.setWaitToText(true);
                        new_condition.setLogical_operator(operator);
                        CurrentRule.getCur_rule().setEmpty(false);
                        CurrentRule.getCur_rule().addCondition(new_condition);
                        editor.putBoolean("Wait_To_Text", true);
                        editor.apply();
                        boolean updated = sharedPreferencesHelper.addConditionToRule(new_condition, cur_rule);
                        if (!updated)
                            Log.e("Not updated", "NOT");
                        Intent intent = new Intent(Someone_Calling_Texting.this, MyConditions.class);
                        startActivity(intent);
                    }
                }else
                {
                    Condition new_condition = new Condition();

                    new_condition.setWaitToText(true);
                    new_condition.setLogical_operator(operator);
                    CurrentRule.getCur_rule().setEmpty(false);
                    CurrentRule.getCur_rule().addCondition(new_condition);
                    editor.putBoolean("Wait_To_Text", true);
                    editor.apply();
                    boolean updated = sharedPreferencesHelper.addConditionToRule(new_condition, cur_rule);
                    if (!updated)
                        Log.e("Not updated", "NOT");
                    Intent intent = new Intent(Someone_Calling_Texting.this, MyConditions.class);
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
        intent.putExtra("username", MainActivity.username);
        intent.putExtra("email", MainActivity.email);
        startActivity(intent);
        finish();
    }
}
