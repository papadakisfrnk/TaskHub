package com.hub.contextawaretaskmanagement.Rules.Rule;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.hub.contextawaretaskmanagement.General.MainActivity;
import com.hub.contextawaretaskmanagement.Rules.Conditions.Condition;
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SharedPreferencesHelper {

    private static final String PREF_NAME = "MyRulesPrefs";
    private static final String RULES = "rules";
    private SharedPreferences sharedPreferences;
    private Gson gson;

    public SharedPreferencesHelper(Context context) {
        sharedPreferences = context.getSharedPreferences(MainActivity.email, Context.MODE_PRIVATE);
        gson = new Gson();
    }

    public void saveRules(List<Rule> rules) {
        String rulesJson = gson.toJson(rules);
        Log.d("SAVERULES-shared", rulesJson);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(RULES, rulesJson);
        editor.apply();
    }



    public List<Rule> loadRules() {
        String rulesJson = sharedPreferences.getString(RULES, null);
        if (rulesJson != null) {
            Type type = new TypeToken<ArrayList<Rule>>() {}.getType();
            return gson.fromJson(rulesJson, type);
        }

        return new ArrayList<>();
    }

    public Rule getCertainRule(String name){
        List<Rule> ruleList = loadRules();
        for (Rule currule : ruleList){
            if (currule.getName().equals(name)){
                return currule;
            }
        }
        return new Rule();
    }

    public Rule getRulebyId(int pos){
        List<Rule> ruleList = loadRules();
        return ruleList.get(pos);
    }

    public boolean addConditionToRule(Condition new_condition, Rule rule) {
        List<Rule> ruleList = loadRules();
        for (Rule currule : ruleList){
            if (currule.getName().equals(rule.getName())){
                currule.addCondition(new_condition);
                saveRules(ruleList);
                return true;
            }
        }
        return false;

    }

    public void updateRule(Rule cur_rule) {
        List<Rule> ruleList = loadRules();
        for (int i=0; i<ruleList.size(); i++){
            if (ruleList.get(i).getName().equals(cur_rule.getName())) {
                ruleList.set(i, cur_rule);
                this.saveRules(ruleList);
            }
        }
    }
}
