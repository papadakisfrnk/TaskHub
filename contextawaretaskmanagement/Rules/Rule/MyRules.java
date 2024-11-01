package com.hub.contextawaretaskmanagement.Rules.Rule;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

import com.hub.contextawaretaskmanagement.General.MainActivity;
import com.hub.contextawaretaskmanagement.R;
import com.hub.contextawaretaskmanagement.Rules.Conditions.MyConditions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MyRules extends AppCompatActivity {
    private static final int REQUEST_CODE = 1;
    private ListView listView;
    private ViewRules adapter;
    private Rule rule;
    private ArrayList<Rule> rules;
    private SharedPreferencesHelper sharedPreferencesHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myrules);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setCustomView(R.layout.action_bar_layout);
            View customView = actionBar.getCustomView();
            AppCompatTextView titleTextView = customView.findViewById(R.id.title_actionbar);
            titleTextView.setText("My Rules");
        }
        listView = findViewById(R.id.listView);

        sharedPreferencesHelper = new SharedPreferencesHelper(this);
        rules = (ArrayList<Rule>) sharedPreferencesHelper.loadRules();

        if (CurrentRule.getCur_rule()!=null)
            if (!CurrentRule.getCur_rule().isEmpty())
                sharedPreferencesHelper.updateRule(CurrentRule.getCur_rule());

        adapter = new ViewRules(this, rules);
        listView.setAdapter(adapter);



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Rule currule =  sharedPreferencesHelper.getRulebyId(position);
                CurrentRule.setCur_rule(currule);
                Intent intent = new Intent(MyRules.this, MyConditions.class);
                startActivity(intent);
            }
        });

        FloatingActionButton syn = findViewById(R.id.floatingActionButton);
        syn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyRules.this, GiveRuleName.class);
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

                adapter.notifyDataSetChanged();

                Intent intent = new Intent(MyRules.this, MyConditions.class);
                intent.putExtra("name", rule.getName());
                startActivity(intent);

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
        intent.putExtra("username",MainActivity.username);
        intent.putExtra("email",MainActivity.email);
        startActivity(intent);
        finish();
    }

}
