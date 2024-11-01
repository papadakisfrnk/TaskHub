package com.hub.contextawaretaskmanagement.Rules.GeneralActions.Actions;

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
import com.hub.contextawaretaskmanagement.Rules.Rule.CurrentRule;
import com.hub.contextawaretaskmanagement.Rules.Rule.Rule;
import com.hub.contextawaretaskmanagement.Rules.Rule.RuleAction;
import com.hub.contextawaretaskmanagement.Rules.Rule.SharedPreferencesHelper;

import java.util.List;

public class ViewActions extends BaseAdapter {
    private Context context;
    private List<RuleAction> action_list;
    private Rule cur_rule = CurrentRule.getCur_rule();
    private SharedPreferencesHelper sharedPreferencesHelper;


    public ViewActions(Context context, List<RuleAction> action_list) {
        this.context = context;
        sharedPreferencesHelper = new SharedPreferencesHelper(context);
        this.action_list = action_list;
    }

    @Override
    public int getCount() {
        return action_list.size();
    }

    @Override
    public Object getItem(int position) {
        return action_list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_action, parent, false);
        }

        TextView actionTextView = convertView.findViewById(R.id.actionTextView);
        ImageView imageView = convertView.findViewById(R.id.iconActionList);
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
                                updateAction(position);
                                break;
                            case R.id.delete_cond:
                                removeAction(position);
                                break;
                        }
                        return true;
                    }
                });

                popupMenu.show();
            }
        });

        RuleAction ruleAction = action_list.get(position);
        String action = "";
        String extra = "";

        String display = "";
        if(ruleAction.getAction().equals("Set Call"))
        {
            action = "A Call will be Made to ";
            extra = ruleAction.getExtra();
            display = action + extra;
        }
        if(ruleAction.getAction().equals("Set Message"))
        {
            action = "A Message will be Sent to ";
            extra = ruleAction.getExtra();
            display = action + extra;
        }
        if(ruleAction.getAction().equals("Set Volume"))
        {
            action = "Τhe Audio Rate will reach ";
            extra = ruleAction.getExtra();
            display = action + extra;
        }
        if(ruleAction.getAction().equals("Set DND"))
        {
            action = "Do Not Disturb Mode will be ";
            extra = ruleAction.getExtra() + "d";
            display = action + extra;
        }
        if(ruleAction.getAction().equals("Set Wifi"))
        {
            action = "Internet Wifi will be ";
            extra = ruleAction.getExtra() + "d";
            display = action + extra;
        }
        if(ruleAction.getAction().equals("Set Brightness"))
        {
            action = "Τhe Brightness Rate will reach ";
            extra = ruleAction.getExtra() +"%";
            display = action + extra;
        }

        actionTextView.setText(display);

        return convertView;
    }

    public void removeAction(int position) {
        if (position >= 0 && position < action_list.size()) {
            action_list.remove(position);
            notifyDataSetChanged();
            cur_rule.setActionsList(action_list);
            LogStorage.setLog(context, "Action has been Deleted");
            sharedPreferencesHelper.updateRule(cur_rule);
            notifyDataSetChanged();

        }
    }
    public void updateAction(int position) {
        if (position >= 0 && position < action_list.size()) {
            RuleAction ruleAction = (RuleAction) getItem(position);
            Intent intent;

            if (ruleAction.getAction().equals("Set Call")) {
                intent = new Intent(context, SetCall.class);
                context.startActivity(intent);
                removeAction(position);
            } else if (ruleAction.getAction().equals("Set Message")) {
                intent = new Intent(context, SetMessage.class);
                context.startActivity(intent);
                removeAction(position);
            } else if (ruleAction.getAction().equals("Set Volume")) {
                intent = new Intent(context, SetVolume.class);
                context.startActivity(intent);
                removeAction(position);
            } else if (ruleAction.getAction().equals("Set DND")) {
                intent = new Intent(context, SetDnd.class);
                context.startActivity(intent);
                removeAction(position);
            } else if (ruleAction.getAction().equals("Set Wifi")) {
                intent = new Intent(context, SetWifi.class);
                context.startActivity(intent);
                removeAction(position);
            } else if (ruleAction.getAction().equals("Set Brightness")) {
                intent = new Intent(context, SetBrightness.class);
                context.startActivity(intent);
                removeAction(position);
            }
        }
    }
}