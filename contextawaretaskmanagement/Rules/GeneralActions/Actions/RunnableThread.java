package com.hub.contextawaretaskmanagement.Rules.GeneralActions.Actions;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.BatteryManager;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.hub.contextawaretaskmanagement.General.NotificationCenter;
import com.hub.contextawaretaskmanagement.Rules.Conditions.Condition;
import com.hub.contextawaretaskmanagement.Rules.GeneralActions.Services.BrightnessService;
import com.hub.contextawaretaskmanagement.Rules.GeneralActions.Services.CallerService;
import com.hub.contextawaretaskmanagement.Rules.GeneralActions.Services.CallingService;
import com.hub.contextawaretaskmanagement.Rules.GeneralActions.Services.DoNotDisturbService;
import com.hub.contextawaretaskmanagement.Rules.GeneralActions.Services.MessageService;
import com.hub.contextawaretaskmanagement.Rules.GeneralActions.Services.TexterService;
import com.hub.contextawaretaskmanagement.Rules.GeneralActions.Services.VolumeService;
import com.hub.contextawaretaskmanagement.Rules.GeneralActions.Services.WifiService;
import com.hub.contextawaretaskmanagement.Rules.Rule.CurrentRule;
import com.hub.contextawaretaskmanagement.Rules.Rule.RuleAction;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RunnableThread extends Service {
    private static final String TODO = null;
    private final LocalBinder mBinder = new LocalBinder();
    private Handler handler;
    private String latitude = "0", longitude = "0";

    private List<Condition> conditionList = new ArrayList<>();

    private List<RuleAction> actionList = new ArrayList<>();

    private Context context;
    private VolumeService volumeService = new VolumeService();

    private BrightnessService brightnessService = new BrightnessService();
    private MessageService messageService = new MessageService();
    private CallingService callingService = new CallingService();
    private CallerService callerService = new CallerService();
    private TexterService texterService = new TexterService();
    private DoNotDisturbService doNotDisturbService = new DoNotDisturbService();

    private WifiService wifiService = new WifiService();

    private boolean shouldContinue = true;

    private FusedLocationProviderClient fusedLocationClient;

    private NotificationCenter nc = new NotificationCenter();


    public class LocalBinder extends Binder {
        public RunnableThread getService() {
            return RunnableThread.this;
        }
    }


    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handler = new Handler();

        shouldContinue = true;
        handler.post(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                conditionList = CurrentRule.getCur_rule().getConditionsList();
                actionList = CurrentRule.getCur_rule().getActionsList();
                Condition joinedCondition = new Condition();
                Condition deviceCondition = new Condition();

                for (Condition cond : conditionList) {
                    if (cond.getTime() != null) {
                        joinedCondition.setTime(cond.getTime());
                    }
                    if (cond.getBatteryLevel() != 0) {
                        joinedCondition.setBatteryLevel(cond.getBatteryLevel());
                    }
                    if (cond.getPhoneId() != null) {
                        joinedCondition.setPhoneId(cond.getPhoneId());
                    }
                    if (cond.getWaitToCall() == true) {
                        joinedCondition.setWaitToCall(cond.getWaitToCall());
                    }
                    if (cond.getWaitToText() == true) {
                        joinedCondition.setWaitToText(cond.getWaitToText());
                    }
                    if (cond.getLocation() != null) {
                        Pattern pattern = Pattern.compile("(-?\\d+\\.\\d+)\\s+(-?\\d+\\.\\d+)");
                        Matcher matcher = pattern.matcher(cond.getLocation());
                        String loc = null;
                        if (matcher.find()) {
                            String latitudeStr = matcher.group(1);
                            String longitudeStr = matcher.group(2);

                            loc = latitudeStr + ", " + longitudeStr;
                        }
                        joinedCondition.setLocation(loc);
                    }

                    if (cond.getLogical_operator() != null) {
                        joinedCondition.setLogical_operator(cond.getLogical_operator());
                    }
                }
                for (Condition cond : conditionList) {
                    if (cond.getTime() != null) {
                        deviceCondition.setTime(getCurrentDateAndTime());
                    }
                    if (cond.getBatteryLevel() != 0) {
                        deviceCondition.setBatteryLevel(getBatteryLevel());
                    }
                    if (cond.getWaitToCall()) {
                        deviceCondition.setWaitToCall(callerService.isSomeoneCalling());
                    }
                    if (cond.getWaitToText()) {
                        deviceCondition.setWaitToText(texterService.isSomeoneTexting());
                    }
                    if (cond.getLocation() != null) {
                        deviceCondition.setLocation(getLastLocation());
                    }
                }

                String logical = joinedCondition.getLogical_operator();

                if (!conditionList.isEmpty() && !actionList.isEmpty()) {
                    if (deviceCondition.equalsAND(joinedCondition)) {
                        SharedPreferences prefsMode = getSharedPreferences("ValueMode", MODE_PRIVATE);
                        int mode = prefsMode.getInt("modeKey", 0);
                        if (mode == 2) {
                            nc.notify(context, "You are in Informative mode. \nThe Rule was not Executed.");
                            System.out.println("hi");
                            stopHandler();
                        } else if (logical != null) {
                            if (logical.equals("AND")) {
                                System.out.println(deviceCondition.equalsAND(joinedCondition));
                                if (deviceCondition.equalsAND(joinedCondition)) {
                                    System.out.println("AND");
                                    checkConditions(joinedCondition);
                                }
                            } else if (logical.equals("OR")) {
                                if (deviceCondition.equalsOR(joinedCondition)) {
                                    System.out.println("OR");
                                    checkConditions(joinedCondition);
                                }
                            } else if (logical.equals("NOT")) {
                                if (deviceCondition.equalsNOT(joinedCondition)) {
                                    System.out.println("NOT");
                                    checkConditions(joinedCondition);
                                }
                            } else if (logical.equals("XOR")) {
                                if (deviceCondition.equalsXOR(joinedCondition)) {
                                    System.out.println("XOR");
                                    checkConditions(joinedCondition);
                                }
                            }
                        } else
                            checkConditions(joinedCondition);
                    }
                }
                if (shouldContinue) {
                    System.out.println("I am checking");
                    handler.postDelayed(this, 5000);
                }
                Log.d("Device Condition", String.valueOf(deviceCondition));
                Log.d("Joined Condition", String.valueOf(joinedCondition));

            }
        });
        return Service.START_STICKY;
    }

    public void stopHandler() {
        shouldContinue = false;
        handler.removeCallbacksAndMessages(null);
    }

    private void checkConditions(Condition joinedCondition) {

        Log.d("Condition Check", " Working");

        ArrayList<String> notification_list = new ArrayList<>();

        for (RuleAction ruleAction : actionList) {
            if (ruleAction.getAction().equals("Set Call")) {
                System.out.println("Check 1");
                callingService.startCallingService(ruleAction.getExtra(), context);
                notification_list.add("Calling " + ruleAction.getAction());
                stopHandler();
            }
            if (ruleAction.getAction().equals("Set Message")) {
                System.out.println("Check 2");
                messageService.startMessageService(ruleAction.getExtra(), ruleAction.getExtra2(), context);
                notification_list.add("Message sent to " + ruleAction.getExtra());
                stopHandler();
            }
            if (ruleAction.getAction().equals("Set Volume")) {
                System.out.println("Check 3");
                volumeService.startVolumeService(ruleAction.getExtra(), context);
                notification_list.add("Volume changed to " + ruleAction.getExtra());
                stopHandler();
            }
            if (ruleAction.getAction().equals("Set DND")) {
                System.out.println("Check 4");
                doNotDisturbService.startDndService(ruleAction.getExtra(), context);
                notification_list.add("DND Mode: " + ruleAction.getExtra());
                stopHandler();
            }
            if (ruleAction.getAction().equals("Set Wifi")) {
                System.out.println("Check 5");
                wifiService.startWifiService(ruleAction.getExtra(), context);
                notification_list.add("Wifi: " + ruleAction.getExtra());
                stopHandler();
            }
            if (ruleAction.getAction().equals("Set Brightness")) {
                System.out.println("Check 6");
                brightnessService.startBrightnessService(ruleAction.getExtra(), context);
                notification_list.add("Brightness changed to " + ruleAction.getExtra() + "%");
                stopHandler();
            }
            nc.notify(context, String.valueOf(notification_list));
        }
    }


    private int getBatteryLevel() {
        Intent batteryStatus = registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        if (level >= 0 && scale > 0) {
            float batteryPercentage = (level / (float) scale) * 100.0f;
            return (int) batteryPercentage;
        } else {
            return -1;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String getCurrentDateAndTime() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d-MM-yyyy HH:m");
        String curr = currentDateTime.format(formatter);
        return curr;
    }


    private String getLastLocation() {

        String coordinates;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return TODO;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            latitude = String.valueOf(location.getLatitude());
                            longitude = String.valueOf(location.getLongitude());

                        } else {
                            System.out.println("Location is null");
                        }
                    }
                });
        DecimalFormat decimalFormat = new DecimalFormat("#.##");

        Double latiduteDouble = Double.parseDouble(latitude);
        Double longitudeDouble = Double.parseDouble(longitude);
        String latitudeStr = decimalFormat.format(latiduteDouble);
        String longitudeStr = decimalFormat.format(longitudeDouble);


        coordinates = latitudeStr + ", " + longitudeStr;
        return coordinates;
    }


}
