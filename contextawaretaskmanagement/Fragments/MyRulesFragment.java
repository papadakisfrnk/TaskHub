package com.hub.contextawaretaskmanagement.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;

import com.hub.contextawaretaskmanagement.R;
import com.hub.contextawaretaskmanagement.Rules.Conditions.MyConditions;
import com.hub.contextawaretaskmanagement.Rules.Rule.CurrentRule;
import com.hub.contextawaretaskmanagement.Rules.Rule.GiveRuleName;
import com.hub.contextawaretaskmanagement.Rules.Rule.Rule;
import com.hub.contextawaretaskmanagement.Rules.Rule.ViewRules;
import com.hub.contextawaretaskmanagement.Rules.Rule.SharedPreferencesHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MyRulesFragment extends Fragment {

    private static final int REQUEST_CODE = 1;
    private ListView listView;
    private ViewRules adapter;

    private Rule rule;
    private ArrayList<Rule> rules;
    private SharedPreferencesHelper sharedPreferencesHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_myrules, container, false);

        listView = view.findViewById(R.id.listView);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setCustomView(R.layout.action_bar_layout);
            View customView = actionBar.getCustomView();
            AppCompatTextView titleTextView = customView.findViewById(R.id.title_actionbar);
            titleTextView.setText("My Rules");
        }

        sharedPreferencesHelper = new SharedPreferencesHelper(requireContext());
        rules = (ArrayList<Rule>) sharedPreferencesHelper.loadRules();

        if (CurrentRule.getCur_rule() != null && !CurrentRule.getCur_rule().isEmpty())
            sharedPreferencesHelper.updateRule(CurrentRule.getCur_rule());

        adapter = new ViewRules(requireContext(), rules);
        listView.setAdapter(adapter);



        listView.setOnItemClickListener((parent, view12, position, id) -> {
            Rule currule = sharedPreferencesHelper.getRulebyId(position);
            CurrentRule.setCur_rule(currule);
            Intent intent = new Intent(requireContext(), MyConditions.class);
            startActivity(intent);
        });

        FloatingActionButton syn = view.findViewById(R.id.floatingActionButton);
        syn.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), GiveRuleName.class);
            startActivityForResult(intent, REQUEST_CODE);
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == getActivity().RESULT_OK) {
            rule = CurrentRule.getCur_rule();
            rules.add(rule);
            Log.d("SAVERULES -MYRULES", rule.toString());

            sharedPreferencesHelper.saveRules(rules);

            adapter.notifyDataSetChanged();

            Intent intent = new Intent(requireContext(), MyConditions.class);
            intent.putExtra("name", rule.getName());
            startActivity(intent);
        }
    }


}
