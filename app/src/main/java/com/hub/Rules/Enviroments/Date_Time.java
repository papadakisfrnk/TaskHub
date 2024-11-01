package com.hub.contextawaretaskmanagement.Rules.Enviroments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Date_Time extends AppCompatActivity implements View.OnClickListener {

    Button btnDatePicker, btnTimePicker, btnSave;
    EditText txtDate, txtTime;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private TextView currentTV;
    private String selectedTime, selectedDate, operator = null;
    private Rule cur_rule = CurrentRule.getCur_rule();

    private SharedPreferencesHelper sharedPreferencesHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_time);

        operator = getIntent().getStringExtra("operator");

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setCustomView(R.layout.action_bar_layout);
            View customView = actionBar.getCustomView();
            AppCompatTextView titleTextView = customView.findViewById(R.id.title_actionbar);
            titleTextView.setText(cur_rule.getName());
        }
        btnDatePicker = findViewById(R.id.btn_date);
        btnTimePicker = findViewById(R.id.btn_time);
        txtDate = findViewById(R.id.in_date);
        txtTime = findViewById(R.id.in_time);
        currentTV = findViewById(R.id.idTVCurrent);
        btnSave = findViewById(R.id.btn_save);


        btnDatePicker.setOnClickListener(this);
        btnTimePicker.setOnClickListener(this);
        btnSave.setVisibility(View.GONE);

        sharedPreferencesHelper = new SharedPreferencesHelper(this);

        SimpleDateFormat sdf = new SimpleDateFormat("'Date\n'dd-MM-yyyy '\n\nand\n\nTime\n'HH:mm:ss z");
        String currentDateAndTime = sdf.format(new Date());
        currentTV.setText(currentDateAndTime);


        txtDate.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                checkDateTimeSelection();
            }
        });

        txtTime.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                checkDateTimeSelection();
            }
        });

        btnSave.setOnClickListener(v -> saveButtonClicked());


    }

    @Override
    public void onClick(View v) {
        if (v == btnDatePicker) {
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    (view, year, monthOfYear, dayOfMonth) -> {

                        if (monthOfYear > 8 && monthOfYear <= 12) {
                            txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                            selectedDate = txtDate.getText().toString();
                            checkDateTimeSelection();

                        } else {
                            txtDate.setText(dayOfMonth + "-" + "0" + (monthOfYear + 1) + "-" + year);
                            selectedDate = txtDate.getText().toString();
                            checkDateTimeSelection();
                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }

        if (v == btnTimePicker) {
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    (view, hourOfDay, minute) -> {
                        if (hourOfDay < 10) {
                            txtTime.setText("0" + hourOfDay + ":" + minute);
                            selectedTime = txtTime.getText().toString();
                            checkDateTimeSelection();
                        } else {
                            txtTime.setText(hourOfDay + ":" + minute);
                            selectedTime = txtTime.getText().toString();
                            checkDateTimeSelection();
                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        }
    }

    private void checkDateTimeSelection() {
        String date = txtDate.getText().toString().trim();
        String time = txtTime.getText().toString().trim();

        if (!date.isEmpty() && !time.isEmpty()) {
            btnSave.setVisibility(View.VISIBLE);
        } else {
            btnSave.setVisibility(View.GONE);
        }
    }

    private void saveButtonClicked() {
        Condition new_condition = new Condition();
        new_condition.setLogical_operator(operator);
        String selectedDateTime = selectedDate + " " + selectedTime;
        new_condition.setTime(selectedDateTime);
        CurrentRule.getCur_rule().setEmpty(false);
        CurrentRule.getCur_rule().addCondition(new_condition);

        LogStorage.setLog(Date_Time.this, "Rule (" + cur_rule.getName() + ") created Date/Time Condition : " + selectedDateTime);
        boolean updated = sharedPreferencesHelper.addConditionToRule(new_condition, cur_rule);
        if (!updated)
            Log.e("Not updated", "NOT");
        sharedPreferencesHelper.loadRules();
        Intent intent = new Intent(this, MyConditions.class);
        startActivity(intent);

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
