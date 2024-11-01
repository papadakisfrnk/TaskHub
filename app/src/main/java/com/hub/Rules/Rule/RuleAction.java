package com.hub.contextawaretaskmanagement.Rules.Rule;

public class RuleAction {

    private String action;
    private String extra;
    private String extra2;

    public RuleAction(String action,String extra) {
        this.action = action;
        this.extra = extra;
    }

    public RuleAction(String action,String extra , String extra2) {
        this.action = action;
        this.extra = extra;
        this.extra2 = extra2;
    }

    public RuleAction() {
    }


    @Override
    public String toString() {
        return "RuleAction{" +
                "action='" + action + '\'' +
                ", extra='" + extra + '\'' +
                ", extra2='" + extra2 + '\'' +
                '}';
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public String getExtra2() {
        return extra2;
    }

    public void setExtra2(String extra2) {
        this.extra2 = extra2;
    }
}
