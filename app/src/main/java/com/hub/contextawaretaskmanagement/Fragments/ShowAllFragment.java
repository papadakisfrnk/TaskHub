package com.hub.contextawaretaskmanagement.Fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;

import com.hub.contextawaretaskmanagement.General.NotificationCenter;
import com.hub.contextawaretaskmanagement.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ShowAllFragment extends Fragment {

    private ListView listView;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> listItems;
    private String packageName;
    private PackageManager packageManager;
    private NotificationCenter notificationCenter = new NotificationCenter();

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_showall, container, false);

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setCustomView(R.layout.action_bar_layout);
            View customView = actionBar.getCustomView();
            AppCompatTextView titleTextView = customView.findViewById(R.id.title_actionbar);
            titleTextView.setText("My Apps");
        }
        listView = view.findViewById(R.id.list_view);
        listItems = new ArrayList<>();
        adapter = new ArrayAdapter<>(requireContext(), R.layout.list_item_show_all, R.id.showAllTextView, listItems);
        listView.setAdapter(adapter);
        packageManager = requireContext().getPackageManager();
        loadInstalledApps();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view1, int position, long id) {
                showPopupMenu(view1, position);
            }
        });

        return view;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            requireActivity().finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadInstalledApps() {
        List<ApplicationInfo> packages = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);
        for (ApplicationInfo appInfo : packages) {
            if (packageManager.getLaunchIntentForPackage(appInfo.packageName) != null) {
                listItems.add(appInfo.loadLabel(packageManager).toString());
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void showPopupMenu(View view, final int position) {
        PopupMenu popup = new PopupMenu(requireContext(), view);
        popup.getMenuInflater().inflate(R.menu.showallappmenu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                packageName = getPackageFromAppName(listItems.get(position));

                switch (item.getItemId()) {
                    case R.id.menu_option_1:
                        launchApp(packageName);
                        return true;

                    case R.id.menu_option_3:
                        uninstallApp(packageName);
                        return true;
                    default:
                        return false;
                }
            }
        });
        popup.show();
    }

    private String getPackageFromAppName(String appName) {
        List<ApplicationInfo> packages = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);
        for (ApplicationInfo appInfo : packages) {
            if (appName.equals(appInfo.loadLabel(packageManager).toString())) {
                return appInfo.packageName;
            }
        }
        return null;
    }

    private void launchApp(String packageName) {
        Intent launchIntent = packageManager.getLaunchIntentForPackage(packageName);
        Log.d("LaunchApp", "Launch Intent: " + launchIntent);

        if (launchIntent != null) {
            startActivity(launchIntent);
        } else {
            Toast.makeText(requireContext(), "Unable to launch app", Toast.LENGTH_SHORT).show();
        }
    }

    private void uninstallApp(String packageName) {
        Intent intent = new Intent(Intent.ACTION_DELETE);
        intent.setData(Uri.parse("package:" + packageName));
        startActivity(intent);
    }

    private void quitApp(String packageName) throws IOException, InterruptedException {

    }
}


