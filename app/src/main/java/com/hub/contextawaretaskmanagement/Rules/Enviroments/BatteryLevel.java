package com.hub.contextawaretaskmanagement.Rules.Enviroments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

import com.hub.contextawaretaskmanagement.General.MainActivity;
import com.hub.contextawaretaskmanagement.Logs.LogStorage;
import com.hub.contextawaretaskmanagement.R;
import com.hub.contextawaretaskmanagement.Rules.Conditions.Condition;
import com.hub.contextawaretaskmanagement.Rules.Conditions.MyConditions;
import com.hub.contextawaretaskmanagement.Rules.Rule.CurrentRule;
import com.hub.contextawaretaskmanagement.Rules.Rule.Rule;
import com.hub.contextawaretaskmanagement.Rules.Rule.SharedPreferencesHelper;

public class BatteryLevel extends AppCompatActivity {

    private Rule cur_rule = CurrentRule.getCur_rule();
    private String operator = null;
    private SharedPreferencesHelper sharedPreferencesHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battery_level);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setCustomView(R.layout.action_bar_layout);
            View customView = actionBar.getCustomView();
            AppCompatTextView titleTextView = customView.findViewById(R.id.title_actionbar);
            titleTextView.setText(cur_rule.getName());
        }

        operator = getIntent().getStringExtra("operator");
        sharedPreferencesHelper = new SharedPreferencesHelper(this);

        final GridLayout buttonGrid = findViewById(R.id.buttonGrid);
        final Button saveButton = findViewById(R.id.saveButton);
        final EditText editText = findViewById(R.id.text_btr_lvl);
        int initialPercentage = 100;

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < buttonGrid.getColumnCount(); j++) {
                int percentage = initialPercentage - (i * buttonGrid.getColumnCount() + j) * 5; // Decrease by 5%
                Button button = createButton(percentage);
                buttonGrid.addView(button);

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String buttonText = ((Button) view).getText().toString();

                        int clickedPercentage = Integer.parseInt(buttonText.replace("%", ""));

                        saveButton.setVisibility(View.VISIBLE);

                        saveButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Condition new_condition = new Condition();
                                new_condition.setLogical_operator(operator);
                                new_condition.setBatteryLevel(clickedPercentage);
                                Log.d("CurrentRule", CurrentRule.getCur_rule().getName());
                                CurrentRule.getCur_rule().setEmpty(false);
                                CurrentRule.getCur_rule().addCondition(new_condition);
                                LogStorage.setLog(BatteryLevel.this,"Rule ("+cur_rule.getName()+") created Battery Condition : " + clickedPercentage);

                                boolean updated = sharedPreferencesHelper.addConditionToRule(new_condition, cur_rule);
                                Log.d("ADDED CONDI", new_condition.toString());
                                if (!updated)
                                    Log.e("Not updated", "NOT");

                                Intent intent = new Intent(BatteryLevel.this, MyConditions.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                    }
                });

            }
        }
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveButton.setVisibility(View.VISIBLE);

                saveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int clickedPercentage = Integer.parseInt(String.valueOf(editText.getText()));
                        Condition new_condition = new Condition();
                        new_condition.setLogical_operator(operator);
                        new_condition.setBatteryLevel(clickedPercentage);
                        Log.d("CurrentRule", CurrentRule.getCur_rule().getName());
                        CurrentRule.getCur_rule().setEmpty(false);
                        CurrentRule.getCur_rule().addCondition(new_condition);
                        boolean updated = sharedPreferencesHelper.addConditionToRule(new_condition, cur_rule);
                        Log.d("ADDED CONDI", new_condition.toString());
                        if (!updated)
                            Log.e("Not updated", "NOT");

                        Intent intent = new Intent(BatteryLevel.this, MyConditions.class);
                        startActivity(intent);
                        finish();
                    }

                });
            }
        });
    }


    private Button createButton(int percentage) {
        Button button = new Button(this);
        button.setText(percentage + "%");
        GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams();
        layoutParams.width = 0;
        layoutParams.height = GridLayout.LayoutParams.WRAP_CONTENT;
        layoutParams.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
        button.setLayoutParams(layoutParams);
        return button;
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
        intent.putExtra("username",MainActivity.username);
        intent.putExtra("email",MainActivity.email);
        startActivity(intent);
        finish();
    }
}
