package com.apicall.weatherapp.view;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apicall.weatherapp.R;
import com.apicall.weatherapp.databinding.ActivityMainBinding;
import com.apicall.weatherapp.model.Constant;
import com.apicall.weatherapp.model.Forecast;
import com.apicall.weatherapp.model.Hour;
import com.apicall.weatherapp.model.Latlong;
import com.apicall.weatherapp.viewmodel.CityTempViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private FusedLocationProviderClient fusedLocationProviderClient;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public static Latlong devicesLocationLatLong;
    CityTempViewModel cityTempViewModel;

    /*
     * Flow is like this
     * onCreate (init everything and also setup view_model observe) ->
     * onResume (will check if fine location permission if taken => if yes -> will check if gps is enabled => if yes -> setLocationListener
     * onResume (will check if fine location permission if taken => if yes -> will check if gps is enabled => if no ->
     * onResume (will check if fine location permission if taken => if no -> take fine location permissions -> onRequestPermissionResult will run
     * onRequestPermissionResult will check if request code is same i.e. app is requesting for location permission only => if yes ->
     * setLocationListener from here => if no -> Show toast or message to user stating that location permission is denied
     * */

    /*
    * User type the city name and click on search
    * app will get the city name value from text_input
    *
    * */


    TilesAdapter tilesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        //Fused Location Provider Client
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        tilesAdapter = new TilesAdapter(this, new ArrayList<>());

        cityTempViewModel = new ViewModelProvider(this).get(CityTempViewModel.class);
        cityTempViewModel.getCityTempLiveData().observe(this, cityTemp -> {
            String cityNameText = cityTemp.getLocation().getName();
            String cityTempCelsiusText = cityTemp.getCurrent().getTempC().intValue() + "°C";
            String imageURL = "https:" + cityTemp.getCurrent().getCondition().getIcon();
            String cityConditionText = cityTemp.getCurrent().getCondition().getText();
            Integer isDay = cityTemp.getCurrent().getIsDay();
            tilesAdapter.setData(cityTemp.getForecast().getForecastday().get(0).getHour());
            binding.idTVCityName.setText(cityNameText);
            Log.d(Constant.LOG_TAG + "s", String.valueOf(cityTemp.getForecast().getForecastday().get(0).getHour().size()));

            binding.idTVTemperature.setText(cityTempCelsiusText);
            Util.loadImage(binding.idIVIcon, imageURL);
            binding.idTVCondition.setText(cityConditionText);
            binding.idRVWeather.setHasFixedSize(true);
            binding.idRVWeather.setAdapter(tilesAdapter);

            if (isDay == 1) {
                binding.idIVBack.setImageResource(R.drawable.day2);
            } else {
                binding.idIVBack.setImageResource(R.drawable.night);
            }

            binding.idRLHome.setVisibility(View.VISIBLE);
            binding.idPBLoading.setVisibility(View.GONE);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        binding.idIVSearch.setOnClickListener(view -> {
            Editable inputText = binding.idEdtCity.getText();
            assert inputText != null;
            String cityName = inputText.toString();
            cityTempViewModel.callWeatherApi(cityName);
        });

        //binding.idEdtCity.setOn
        getResources().getStringArray(R.array.)
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (PermissionUtils.checkAccessFineLocationGranted(this)) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                //Todo check into this why we need this
                return;
            }

            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(location -> {
                if (location != null) {
                    Log.d(Constant.LOG_TAG + "o", "Location is not null at this point");
                    setLocationListener();
                } else {
                    Log.d(Constant.LOG_TAG + "o", "Location = null gonna take permission then location");
                    if (PermissionUtils.isLocationEnabled(this)) {
                        Log.d(Constant.LOG_TAG + "a", "Provider Enabled");
                        setLocationListener();
                    } else {
                        PermissionUtils.showAlertMessageLocationDisabled(this);
                        Log.d(Constant.LOG_TAG + "b", "Provider Disabled");
                    }
                }
            });
        } else {
            Log.d(Constant.LOG_TAG + "y", "Requesting permission here");
            PermissionUtils.requestAccessFineLocationPermission(this, MY_PERMISSIONS_REQUEST_LOCATION);
        }
    }

    private void setLocationListener() {
        LocationRequest locationRequest = new LocationRequest.Builder(Priority.PRIORITY_BALANCED_POWER_ACCURACY, (long) Math.pow(10, 6))
                .setWaitForAccurateLocation(false)
                .setMinUpdateIntervalMillis(500)
                .setMaxUpdateDelayMillis((long) Math.pow(10, 6)).build();

        if (PermissionUtils.checkAccessFineLocationGranted(this)) {
            Log.d(Constant.LOG_TAG + "k", "this ran");

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Log.d(Constant.LOG_TAG + "z", "No permissions");
                return;
            }

            Log.d(Constant.LOG_TAG + "m", "this ran also");


            fusedLocationProviderClient.requestLocationUpdates(locationRequest, new LocationCallback() {
                @Override
                public void onLocationResult(@NonNull LocationResult locationResult) {
                    super.onLocationResult(locationResult);
                    //Log.d(Constant.LOG_TAG, devicesLocationLatLong.getLatitude().toString());
                    for (Location location : locationResult.getLocations()) {
                        devicesLocationLatLong = new Latlong(location.getLatitude(), location.getLongitude());
                        Log.d(Constant.LOG_TAG, devicesLocationLatLong.getLatitude().toString());
                        //for calling  api method present in view model
                        cityTempViewModel.callWeatherApi(devicesLocationLatLong);
                    }
                }
            }, Looper.myLooper()).addOnSuccessListener(unused -> {
                Log.d(Constant.LOG_TAG, "success idk");
            }).addOnFailureListener(e -> {
                Log.d(Constant.LOG_TAG, e.getMessage());
            });

        } else {
            Log.d(Constant.LOG_TAG + "y", "Requesting permission here");
            PermissionUtils.requestAccessFineLocationPermission(this, MY_PERMISSIONS_REQUEST_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_LOCATION) {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d(Constant.LOG_TAG + "t", Arrays.toString(grantResults));
                setLocationListener();
            } else {
                Toast.makeText(this, "Permission Denied, Please grant location access", Toast.LENGTH_SHORT).show();
            }
        } else {
            Log.d(Constant.LOG_TAG, "" + requestCode);
        }
    }
}