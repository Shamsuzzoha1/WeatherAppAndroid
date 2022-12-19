package com.apicall.weatherapp.view;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.provider.Settings;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class PermissionUtils {

    public static Boolean isLocationEnabled(Context context){
        LocationManager locationManager;
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    public static void requestAccessFineLocationPermission(AppCompatActivity activity, int requestId) {
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, requestId);
    }

    public static Boolean checkAccessFineLocationGranted(Context context){
        return ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    public static void showAlertMessageLocationDisabled(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Device location access is disabled, Please enable them in Settings.");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", (dialogInterface, i) -> context.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)));

        builder.setNegativeButton("No", (dialogInterface, i) -> {
            dialogInterface.cancel();
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
