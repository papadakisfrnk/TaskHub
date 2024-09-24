package com.hub.contextawaretaskmanagement.Rules.Enviroments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.app.ActivityCompat;

import com.hub.contextawaretaskmanagement.General.MainActivity;
import com.hub.contextawaretaskmanagement.Logs.LogStorage;
import com.hub.contextawaretaskmanagement.R;
import com.hub.contextawaretaskmanagement.Rules.Conditions.Condition;
import com.hub.contextawaretaskmanagement.Rules.Conditions.MyConditions;
import com.hub.contextawaretaskmanagement.Rules.Rule.CurrentRule;
import com.hub.contextawaretaskmanagement.Rules.Rule.Rule;
import com.hub.contextawaretaskmanagement.Rules.Rule.SharedPreferencesHelper;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;

public class MapLocation extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap myMap;
    private Rule cur_rule = CurrentRule.getCur_rule();
    private SharedPreferencesHelper sharedPreferencesHelper;
    private SearchView mapSearchView;
    private final int FINE_PERMISSION_CODE = 1;
    private Location currentLocation;

    private String latitude, longitude, operator = null, nameloc = null;
    private Button btn_loc;
    private FusedLocationProviderClient fusedLocationProviderClient;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_location);

        operator = getIntent().getStringExtra("operator");

        sharedPreferencesHelper = new SharedPreferencesHelper(this);

        mapSearchView = findViewById(R.id.mapSearch);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        getLastLocation();
        btn_loc = findViewById(R.id.button_map_loc);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setCustomView(R.layout.action_bar_layout);
            View customView = actionBar.getCustomView();
            AppCompatTextView titleTextView = customView.findViewById(R.id.title_actionbar);
            titleTextView.setText(cur_rule.getName());
        }
        btn_loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPreferencesHelper.updateRule(cur_rule);
                Condition new_condition = new Condition();
                new_condition.setLogical_operator(operator);
                if (nameloc == null) {
                    nameloc = "";
                }
                new_condition.setLocation(nameloc + " " + latitude + " " + longitude);
                Log.d("CurrentRule", CurrentRule.getCur_rule().getName());
                CurrentRule.getCur_rule().setEmpty(false);
                CurrentRule.getCur_rule().addCondition(new_condition);
                LogStorage.setLog(MapLocation.this, "Rule (" + cur_rule.getName() + ") created Location Condition : " + nameloc + "with Latidute and Longiture " + latitude + "," + longitude);

                boolean updated = sharedPreferencesHelper.addConditionToRule(new_condition, cur_rule);
                Log.d("ADDED CONDI", new_condition.toString());
                if (!updated)
                    Log.e("Not updated", "NOT");
                Intent intent = new Intent(MapLocation.this, MyConditions.class);
                startActivity(intent);
            }
        });
        mapSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String location = mapSearchView.getQuery().toString();
                List<Address> addressList = null;

                if (location != null) {
                    Geocoder geocoder = new Geocoder(MapLocation.this);

                    try {
                        addressList = geocoder.getFromLocationName(location, 1);
                    } catch (IOException e) {
                        Log.e("Geocoder Error", "Error getting location from name: " + e.getMessage());
                        e.printStackTrace();
                        return false;
                    }


                    Address address = addressList.get(0);
                    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                    nameloc = location;
                   /* MarkerOptions options = new MarkerOptions().position(latLng).title(location);
                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                    myMap.addMarker(options);*/
                    myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));

                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


    }

    private void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_PERMISSION_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentLocation = location;
                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                    mapFragment.getMapAsync(MapLocation.this);
                }
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        myMap = googleMap;

        LatLng myloc = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        MarkerOptions options = new MarkerOptions().position(myloc).title("My Location");
        options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
        myMap.addMarker(options);
        myMap.getUiSettings().setZoomControlsEnabled(true);
        myMap.getUiSettings().setCompassEnabled(true);

        myMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title(latLng.latitude + " " + latLng.longitude);
                DecimalFormat decimalFormat = new DecimalFormat("#.##");
                Double latitudeDouble = Double.parseDouble(String.valueOf(latLng.latitude));
                Double longitudeDouble = Double.parseDouble(String.valueOf(latLng.longitude));
                latitude = decimalFormat.format(latitudeDouble);
                longitude = decimalFormat.format(longitudeDouble);

                myMap.clear();
                myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                myMap.addMarker(markerOptions);
                btn_loc.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == FINE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            } else {
                Toast.makeText(this, "Location Permission is denied, please allow the persmission", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_location_map, menu);
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.mapNone) {
            myMap.setMapType(GoogleMap.MAP_TYPE_NONE);
        }
        if (id == R.id.mapNormal) {
            myMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        }
        if (id == R.id.mapSatellite) {
            myMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        }
        if (id == R.id.mapHybrid) {
            myMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        }
        if (id == R.id.mapTerrain) {
            myMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        }
        switch (item.getItemId()) {
            case R.id.action_home_menu:
                navigateToMainActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void navigateToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("username",MainActivity.username);
        intent.putExtra("email",MainActivity.email);
        startActivity(intent);
        finish();
    }
}
