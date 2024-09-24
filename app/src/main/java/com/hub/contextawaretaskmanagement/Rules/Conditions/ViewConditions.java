package com.hub.contextawaretaskmanagement.Rules.Conditions;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.hub.contextawaretaskmanagement.Logs.LogStorage;
import com.hub.contextawaretaskmanagement.R;
import com.hub.contextawaretaskmanagement.Rules.Enviroments.BatteryLevel;
import com.hub.contextawaretaskmanagement.Rules.Enviroments.Date_Time;
import com.hub.contextawaretaskmanagement.Rules.Enviroments.MapLocation;
import com.hub.contextawaretaskmanagement.Rules.Enviroments.Someone_Calling_Texting;
import com.hub.contextawaretaskmanagement.Rules.Rule.CurrentRule;
import com.hub.contextawaretaskmanagement.Rules.Rule.Rule;
import com.hub.contextawaretaskmanagement.Rules.Rule.SharedPreferencesHelper;

import java.util.List;

public class ViewConditions extends BaseAdapter {
    private Context context;
    private List<Condition> condition_list;
    private Rule cur_rule = CurrentRule.getCur_rule();
    private SharedPreferencesHelper sharedPreferencesHelper;


    public ViewConditions(Context context, List<Condition> condList) {
        this.context = context;
        sharedPreferencesHelper = new SharedPreferencesHelper(context);
        this.condition_list = condList;
    }

    @Override
    public int getCount() {
        return condition_list.size();
    }

    @Override
    public Object getItem(int position) {
        return condition_list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_condition, parent, false);
        }

        TextView conditionTextView = convertView.findViewById(R.id.conditionTextView);
        ImageView imageView = convertView.findViewById(R.id.iconCondList);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(context, view);
                popupMenu.getMenuInflater().inflate(R.menu.menu_condition_settings, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.update_cond:
                                updateCondition(position);
                                break;
                            case R.id.delete_cond:
                                removeCondition(position);
                                break;
                        }
                        return true;
                    }

                    private void updateCondition(int position) {
                        Condition c = (Condition) getItem(position);
                        Intent intent;
                        if (c.getBatteryLevel() != 0) {
                            intent = new Intent(context, BatteryLevel.class);
                            if (c.getLogical_operator() != null)
                                intent.putExtra("operator", c.getLogical_operator());

                            context.startActivity(intent);
                            removeCondition(position);
                        } else if (c.getLocation() != null) {
                            intent = new Intent(context, MapLocation.class);
                            if (c.getLogical_operator() != null)
                                intent.putExtra("operator", c.getLogical_operator());
                            context.startActivity(intent);
                            removeCondition(position);
                        } else if (c.getTime() != null) {
                            intent = new Intent(context, Date_Time.class);
                            if (c.getLogical_operator() != null)
                                intent.putExtra("operator", c.getLogical_operator());
                            context.startActivity(intent);
                            removeCondition(position);
                        } else if (c.getWaitToText()) {
                            intent = new Intent(context, Someone_Calling_Texting.class);
                            if (c.getLogical_operator() != null)
                                intent.putExtra("operator", c.getLogical_operator());
                            context.startActivity(intent);
                            removeCondition(position);
                        } else if (c.getWaitToCall()) {
                            intent = new Intent(context, Someone_Calling_Texting.class);
                            if (c.getLogical_operator() != null)
                                intent.putExtra("operator", c.getLogical_operator());
                            context.startActivity(intent);
                            removeCondition(position);
                        }

                    }
                });
                popupMenu.show();
            }
        });

        String enviromentsView = "";

        String type = "";

        if (!condition_list.isEmpty()) {
            condition_list.get(0).setLogical_operator(null);
        }
        Condition cond = condition_list.get(position);
        if (cond.getLogical_operator() != null) {
            type = "-" + cond.getLogical_operator() + "- ";
        }
        if (cond.getTime() != null) {
            enviromentsView += "When the Date - Time Becomes: " + cond.getTime();
        }

        if (cond.getLocation() != null) {
            enviromentsView += "When the Location is : " + cond.getLocation();
        }
        if (cond.getBatteryLevel() != 0) {
            enviromentsView += "When the Battery Percentage drops at: " + cond.getBatteryLevel() + "%";

        }
        if (cond.getWaitToCall() == true) {
            enviromentsView += "When i receive a Call";
        }
        if (cond.getWaitToText() == true) {
            enviromentsView += "When i receive a Message";
        }


        conditionTextView.setText(type + enviromentsView);


        cur_rule.setDescription(enviromentsView);
        return convertView;
    }

    public void removeCondition(int position) {
        Condition c = (Condition) getItem(position);
        if (position >= 0 && position < condition_list.size()) {
            condition_list.remove(position);
            notifyDataSetChanged();
            LogStorage.setLog(context, "Condition (" + c.getId() + ") has been Deleted");
            cur_rule.setConditionsList(condition_list);
            sharedPreferencesHelper.updateRule(cur_rule);
            notifyDataSetChanged();

        }
    }
}