package com.hub.contextawaretaskmanagement.Rules.Rule;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

import com.hub.contextawaretaskmanagement.General.MainActivity;
import com.hub.contextawaretaskmanagement.Logs.LogStorage;
import com.hub.contextawaretaskmanagement.R;
import com.google.gson.Gson;

public class GiveRuleName extends AppCompatActivity {

    private EditText write_name;

    private Rule rule = new Rule();
    private Button save;
    private SharedPreferencesHelper sharedPreferencesHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_give_rule_name);
        sharedPreferencesHelper = new SharedPreferencesHelper(this);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setCustomView(R.layout.action_bar_layout);
            View customView = actionBar.getCustomView();
            AppCompatTextView titleTextView = customView.findViewById(R.id.title_actionbar);
            titleTextView.setText("Rule Name");
        }


        write_name = findViewById(R.id.editTextTextPersonName);
        save = findViewById(R.id.button_save);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = write_name.getText().toString();
                if (name.isEmpty())
                    Toast.makeText(getApplicationContext(), "Your Rule name must not be empty!", Toast.LENGTH_SHORT).show();
                else {
                    rule.setName(name);

                    CurrentRule.setCur_rule(rule);

                    String ruleJson = new Gson().toJson(rule);

                    LogStorage.setLog(GiveRuleName.this, "Rule Name (" + name + ") created");
                    // Pass the name back to the previous activity
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("name", name);
                    setResult(RESULT_OK, resultIntent);
                    finish();
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