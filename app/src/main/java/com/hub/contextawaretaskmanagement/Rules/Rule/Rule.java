package com.hub.contextawaretaskmanagement.Rules.Rule;

import android.os.Parcel;
import android.os.Parcelable;

import com.hub.contextawaretaskmanagement.Rules.Conditions.Condition;

import java.util.ArrayList;
import java.util.List;

public class Rule implements Parcelable {
    private String name;

    private String description;

    private List<Condition> conditionsList = new ArrayList<>();

    private List<RuleAction> actionsList = new ArrayList<>();


    private boolean empty = true;

    public Rule(String name) {
        this.name = name;
    }

    public Rule() {
    }

    ;

    protected Rule(Parcel in) {
        name = in.readString();
        ClassLoader classLoader = null;
    }


    public void addCondition(Condition condit) {
        this.conditionsList.add(condit);
    }

    public static final Creator<Rule> CREATOR = new Creator<Rule>() {
        @Override
        public Rule createFromParcel(Parcel in) {
            return new Rule(in);
        }

        @Override
        public Rule[] newArray(int size) {
            return new Rule[size];
        }
    };

    public String getName() {
        return name;
    }

    public String getDescription() {
        if (description == null)
            description = "No Condition yet.";
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Condition> getConditionsList() {
        return conditionsList;
    }

    public void setConditionsList(List<Condition> conditionsList) {
        this.conditionsList = conditionsList;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty(boolean empty) {
        this.empty = empty;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public List<RuleAction> getActionsList() {
        return actionsList;
    }

    public void setActionsList(List<RuleAction> actionsList) {
        this.actionsList = actionsList;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
    }


}
