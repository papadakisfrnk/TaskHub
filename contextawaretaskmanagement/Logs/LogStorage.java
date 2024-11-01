package com.hub.contextawaretaskmanagement.Logs;

import android.content.Context;
import android.content.SharedPreferences;

import com.hub.contextawaretaskmanagement.General.MainActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class LogStorage {

    private static final String KEY_LIST = "log-key";

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(MainActivity.email, Context.MODE_PRIVATE);
    }

    private static void saveList(Context context, List<String> myList) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        Gson gson = new Gson();
        String json = gson.toJson(myList);
        editor.putString(KEY_LIST, json);
        editor.apply();
    }

    public static List<String> getList(Context context) {
        SharedPreferences prefs = getSharedPreferences(context);
        String json = prefs.getString(KEY_LIST, null);
        Type type = new TypeToken<List<String>>() {}.getType();
        return new Gson().fromJson(json, type);
    }

    public static void setLog(Context context, String str) {
        List<String> log_list = getList(context);
        if (log_list == null) {
            log_list = new ArrayList<>();
        }
        log_list.add(str);
        saveList(context, log_list);
    }

    public static void deleteLog(Context context, int position) {
        List<String> log_list = getList(context);
        if (log_list != null && position >= 0 && position < log_list.size()) {
            log_list.remove(position);
            saveList(context, log_list);
        }
    }

    public static void renameLog(Context context, int position, String newName) {
        List<String> log_list = getList(context);
        if (log_list != null && position >= 0 && position < log_list.size()) {
            log_list.set(position, newName);
            saveList(context, log_list);
        }
    }
}
