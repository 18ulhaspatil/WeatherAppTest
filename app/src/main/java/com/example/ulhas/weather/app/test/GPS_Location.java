package com.example.ulhas.weather.app.test;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by ULHAS on 1/15/2015.
 */
public class GPS_Location implements LocationListener {
    Context context;
    double Latitude, Longitude;
    protected LocationManager locationManager;

    GPS_Location(Context context) {
        this.context = context;
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        getGPSLocation(location);
    }

    private void getGPSLocation(Location location) {
        setLongitude(location.getLongitude());
        setLatitude(location.getLatitude());
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }

    public double getLatitude() {
        return Latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    @Override
    public void onLocationChanged(Location location) {
        setLongitude(location.getLongitude());
        setLatitude(location.getLatitude());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Latitude", "status");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("Latitude", "enable");
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("Latitude", "disable");
    }
}