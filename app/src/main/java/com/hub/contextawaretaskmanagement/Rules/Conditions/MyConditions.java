package com.hub.contextawaretaskmanagement.Rules.Conditions;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hub.contextawaretaskmanagement.General.MainActivity;
import com.hub.contextawaretaskmanagement.Logs.LogStorage;
import com.hub.contextawaretaskmanagement.R;
import com.hub.contextawaretaskmanagement.Rules.Enviroments.Environment;
import com.hub.contextawaretaskmanagement.Rules.GeneralActions.Actions.MyAction;
import com.hub.contextawaretaskmanagement.Rules.GeneralActions.Actions.RunnableThread;
import com.hub.contextawaretaskmanagement.Rules.GeneralActions.Actions.ViewActions;
import com.hub.contextawaretaskmanagement.Rules.Rule.CurrentRule;
import com.hub.contextawaretaskmanagement.Rules.Rule.Rule;
import com.hub.contextawaretaskmanagement.Rules.Rule.RuleAction;
import com.hub.contextawaretaskmanagement.Rules.Rule.SharedPreferencesHelper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MyConditions extends AppCompatActivity {
    private String lastCondClickedID;
    private Rule cur_rule = CurrentRule.getCur_rule();
    private ListView conditionListView, actionListView;
    private ViewConditions conditions_adapter;
    private ViewActions actions_adapter;
    private SharedPreferencesHelper sharedPreferencesHelper;
    private LinearLayout buttonsLayout;
    private Button buttonOr, buttonAnd, buttonNot, buttonXor;
    private List<Condition> condition_list = new ArrayList<>();
    private List<RuleAction> action_list = new ArrayList<>();

    private String ruleName, operator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conditions);
        sharedPreferencesHelper = new SharedPreferencesHelper(this);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setCustomView(R.layout.action_bar_layout);
            View customView = actionBar.getCustomView();
            AppCompatTextView titleTextView = customView.findViewById(R.id.title_actionbar);
            SharedPreferences prefsMode = getSharedPreferences("ValueMode", MODE_PRIVATE);
            int mode = prefsMode.getInt("modeKey", 0);
            String strMode = null;
            if (mode == 1)
                strMode = "Normal";
            else if (mode == 2)
                strMode = "Informative";
            else if (mode == 3)
                strMode = "Interactive";

            titleTextView.setText(cur_rule.getName() + " (" + strMode + ")");
        }

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            ruleName = cur_rule.getName();
        } else {
            ruleName = new String((String) extras.get("name"));
        }

        conditionListView = findViewById(R.id.conditionlistView);
        actionListView = findViewById(R.id.actionlistView);

        condition_list = new ArrayList<>(
                sharedPreferencesHelper.getCertainRule(ruleName)
                        .getConditionsList());

        action_list = new ArrayList<>(
                sharedPreferencesHelper.getCertainRule(ruleName)
                        .getActionsList());


        conditions_adapter = new ViewConditions(this, condition_list);
        actions_adapter = new ViewActions(this, action_list);
        conditionListView.setAdapter(conditions_adapter);
        actionListView.setAdapter(actions_adapter);

        //starts the threads
        if (!condition_list.isEmpty()) {
            Intent serviceIntent = new Intent(this, RunnableThread.class);
            startService(serviceIntent);
        }
        buttonsLayout = findViewById(R.id.buttonsLayout);


        //syn button
        FloatingActionButton floatingActionButton = findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (condition_list.isEmpty()) {
                    Intent intent = new Intent(MyConditions.this, Environment.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(MyConditions.this, MyAction.class);
                    startActivity(intent);
                }
            }
        });


        conditionListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CurrentRule.setCur_Condition(position);
                lastCondClickedID = condition_list.get(position).getId();
            }
        });


        buttonOr = findViewById(R.id.buttonOr);
        buttonAnd = findViewById(R.id.buttonAnd);
        buttonNot = findViewById(R.id.buttonNot);
        buttonXor = findViewById(R.id.buttonXor);

        buttonOr.setClickable(false);
        buttonAnd.setClickable(false);
        buttonNot.setClickable(false);
        buttonXor.setClickable(false);
        if (!condition_list.isEmpty()) {
            changeColor();
            buttonOr.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    for (Condition cond : condition_list) {
                        if (cond.getId().equals(lastCondClickedID)) {
                            cond.setLogical_operator("OR");
                        }
                    }
                    LogStorage.setLog(MyConditions.this, "Rule (" + cur_rule.getName() + ") pressed the OR Operator");
                    Intent intent = new Intent(MyConditions.this, Environment.class);
                    operator = "OR";
                    intent.putExtra("operator", operator);
                    startActivity(intent);
                }
            });

            buttonAnd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    for (Condition cond : condition_list) {
                        if (cond.getId().equals(lastCondClickedID)) {
                            cond.setLogical_operator("AND");
                        }
                    }
                    LogStorage.setLog(MyConditions.this, "Rule (" + cur_rule.getName() + ") pressed the AND Operator");

                    Intent intent = new Intent(MyConditions.this, Environment.class);
                    operator = "AND";
                    intent.putExtra("operator", operator);
                    startActivity(intent);
                }
            });

            buttonNot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    for (Condition cond : condition_list) {
                        if (cond.getId().equals(lastCondClickedID)) {
                            cond.setLogical_operator("NOT");
                        }
                    }
                    LogStorage.setLog(MyConditions.this, "Rule (" + cur_rule.getName() + ") pressed the NOT Operator");

                    Intent intent = new Intent(MyConditions.this, Environment.class);
                    operator = "NOT";
                    intent.putExtra("operator", operator);
                    startActivity(intent);
                }
            });

            buttonXor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    for (Condition cond : condition_list) {
                        if (cond.getId().equals(lastCondClickedID)) {
                            cond.setLogical_operator("XOR");
                        }
                    }
                    LogStorage.setLog(MyConditions.this, "Rule (" + cur_rule.getName() + ") pressed the XOR Operator");

                    Intent intent = new Intent(MyConditions.this, Environment.class);
                    operator = "XOR";
                    intent.putExtra("operator", operator);
                    startActivity(intent);
                }
            });
        }


    }


    private void changeColor() {
        buttonOr.setClickable(true);
        buttonOr.setTextColor(Color.WHITE);
        buttonAnd.setClickable(true);
        buttonAnd.setTextColor(Color.WHITE);
        buttonNot.setClickable(true);
        buttonNot.setTextColor(Color.WHITE);
        buttonXor.setClickable(true);
        buttonXor.setTextColor(Color.WHITE);
    }

    private void updateService() {
        if (!condition_list.isEmpty()) {
            Intent serviceIntent = new Intent(this, RunnableThread.class);
            serviceIntent.putExtra("conditions_key", (Serializable) condition_list);
            stopService(serviceIntent); // Stop the service
            startService(serviceIntent); // Start the service with the updated condition list
        }
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