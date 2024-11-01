package com.hub.contextawaretaskmanagement.General;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.hub.contextawaretaskmanagement.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ShowAll extends AppCompatActivity {

    private ListView listView;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> listItems;
    private PackageManager packageManager;
    private NotificationCenter notificationCenter = new NotificationCenter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showall);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        listView = findViewById(R.id.list_view);
        listItems = new ArrayList<>();
        adapter = new ArrayAdapter<String>(ShowAll.this, android.R.layout.simple_list_item_1, listItems);
        listView.setAdapter(adapter);
        packageManager = getPackageManager();

        loadInstalledApps();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                launchApp(listItems.get(position));
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                showPopupMenu(view, position);
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
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
        PopupMenu popup = new PopupMenu(this, view);
        popup.getMenuInflater().inflate(R.menu.showallappmenu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            String packageName = getPackageFromAppName(listItems.get(position));


            @Override
            public boolean onMenuItemClick(MenuItem item) {
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
            Toast.makeText(this, "Unable to launch app", Toast.LENGTH_SHORT).show();
        }
    }

    private void uninstallApp(String packageName) {
        Intent intent = new Intent(Intent.ACTION_DELETE);
        intent.setData(Uri.parse("package:" + packageName));
        startActivity(intent);
    }

    private int getProcessIdByPackageName(String packageName) {
        ActivityManager manager = (ActivityManager) getApplicationContext().getSystemService(getApplicationContext().ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> activityList = manager.getRunningAppProcesses();
        for (int i = 0; i < activityList.size(); i++) {

            Log.d("PROCESSNAME", activityList.get(i).processName);

            if (activityList.get(i).processName.contains(packageName)) {
                return activityList.get(i).pid;
            }
        }
        return 0;
    }

    public void quitApp(String packageName) throws IOException, InterruptedException {
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        if (activityManager != null) {
            activityManager.killBackgroundProcesses(packageName);
            int pid = getProcessIdByPackageName(packageName);

            Log.d("PACKAGE", packageName);
            android.os.Process.killProcess(android.os.Process.myPid());
            android.os.Process.killProcess(pid);
            android.os.Process.sendSignal(pid, android.os.Process.SIGNAL_KILL);
            Log.d("ID", String.valueOf(pid));
            notificationCenter.notify(ShowAll.this, "You have quit the " + packageName);
            return;
        }
        Toast.makeText(this, "App not found", Toast.LENGTH_SHORT).show();
    }
}
