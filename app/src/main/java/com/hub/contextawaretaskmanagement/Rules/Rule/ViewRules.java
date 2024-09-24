package com.hub.contextawaretaskmanagement.Rules.Rule;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.hub.contextawaretaskmanagement.Logs.LogStorage;
import com.hub.contextawaretaskmanagement.R;

import java.util.ArrayList;

public class ViewRules extends ArrayAdapter<Rule> {

    private ArrayList<Rule> rules;
    private SharedPreferencesHelper sharedPreferencesHelper;

    public ViewRules(Context context, ArrayList<Rule> rules) {
        super(context, 0, rules);
        sharedPreferencesHelper = new SharedPreferencesHelper(getContext());
        this.rules = rules;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_rule, parent, false);
        }

        Rule rule = getItem(position);
        if (rule != null) {
            TextView nameTextView = convertView.findViewById(R.id.nameTextView);

            nameTextView.setText("Name: " + rule.getName() + "\nDescription:\n" + rule.getDescription());

            ImageView iconRuleList = convertView.findViewById(R.id.iconRuleList);
            iconRuleList.setOnClickListener(view -> showPopupMenu(view, position));
        }

        return convertView;
    }

    private void showPopupMenu(View view, int position) {
        PopupMenu popupMenu = new PopupMenu(getContext(), view);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.menu_rule_settings, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.change_name:

                    changeRuleName(position);
                    return true;
                case R.id.delete_rule:

                    showDeleteConfirmationDialog(position);
                    return true;
                default:
                    return false;
            }
        });

        popupMenu.show();
    }

    private void changeRuleName(int position) {
        Rule rule = getItem(position);
        final EditText input = new EditText(getContext());
        input.setText(rule.getName());

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Edit Rule Name")
                .setView(input)
                .setPositiveButton("Save", (dialog, which) -> {
                    String newName = input.getText().toString().trim();
                    if (!newName.isEmpty()) {
                        LogStorage.setLog(getContext(),"Rule Name (" + rule.getName() +") renamed to : " + newName);
                        rule.setName(newName);
                        sharedPreferencesHelper.saveRules(rules);
                        notifyDataSetChanged();
                    } else {
                        Toast.makeText(getContext(), "Please enter a valid name", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }


    private void showDeleteConfirmationDialog(final int position) {
        Rule r = getItem(position);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Delete Confirmation")
                .setMessage("Are you sure you want to delete this rule?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    LogStorage.setLog(getContext(),"Rule ("+r.getName()+") has been deleted");
                    deleteRule(position);
                    Toast.makeText(getContext(), "Rule deleted", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private void deleteRule(int position) {
        rules.remove(position);
        sharedPreferencesHelper.saveRules(rules);
        notifyDataSetChanged();

    }
}
