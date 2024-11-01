package com.hub.contextawaretaskmanagement.Rules.Rule;

public class CurrentRule {
    private static Rule cur_rule;
    private static int cur_Condition;


    public static Rule getCur_rule() {
        return cur_rule;
    }

    public static int getCur_Condition(){
        return cur_Condition;
    }

    public static void setCur_Condition(int cur_cond){
        CurrentRule.cur_Condition = cur_cond;
    }

    public static void setCur_rule(Rule cur_rule) {
        CurrentRule.cur_rule = cur_rule;
    }
}
