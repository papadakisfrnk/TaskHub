package com.hub.contextawaretaskmanagement.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;

import com.hub.contextawaretaskmanagement.General.MainActivity;
import com.hub.contextawaretaskmanagement.Logs.LogStorage;
import com.hub.contextawaretaskmanagement.R;

import java.util.Collections;
import java.util.List;

public class LogsFragment extends Fragment {

    private ListView listView;
    private ArrayAdapter<String> adapter;

    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_logs, container, false);

        setupActionBar();

        listView = view.findViewById(R.id.dataListView);

        displaySharedPreferencesData();

        registerForContextMenu(listView);

        listView.setOnItemClickListener((parent, listItemView, position, id) -> showContextMenu(listItemView, position));

        return view;
    }

    private void setupActionBar() {
        ActionBar actionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setCustomView(R.layout.action_bar_layout);
            View customView = actionBar.getCustomView();
            AppCompatTextView titleTextView = customView.findViewById(R.id.title_actionbar);
            titleTextView.setText("Logs");
        }
    }

    private void displaySharedPreferencesData() {
        List<String> log_list = LogStorage.getList(requireContext());

        if (log_list != null) {
            Collections.reverse(log_list);
            adapter = new ArrayAdapter<>(requireContext(), R.layout.list_item_show_all, R.id.showAllTextView, log_list);
            listView.setAdapter(adapter);
        } else {
        }
    }

    private void showContextMenu(View view, final int position) {
        PopupMenu popupMenu = new PopupMenu(requireContext(), view);
        popupMenu.getMenuInflater().inflate(R.menu.log_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.menu_delete:
                    deleteLog(position);
                    return true;
                case R.id.menu_rename:
                    showRenameDialog(position);
                    return true;
                default:
                    return false;
            }
        });

        popupMenu.show();
    }

    private void showRenameDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Rename Log");

        final EditText input = new EditText(requireContext());
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("OK", (dialog, which) -> {
            String newName = input.getText().toString();

            renameLog(position, newName);
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void renameLog(int position, String newName) {
        adapter.remove(adapter.getItem(position));
        adapter.insert(newName, position);

        LogStorage.renameLog(requireContext(), position, newName);

        adapter.notifyDataSetChanged();
    }



    private void deleteLog(int position) {
        adapter.remove(adapter.getItem(position));

        LogStorage.deleteLog(requireContext(), position);

        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                navigateToMainActivity();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(requireContext(), MainActivity.class);
        startActivity(intent);
        requireActivity().finish();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getActivity().getMenuInflater().inflate(R.menu.log_menu, menu);
    }
}
