package com.example.soomin.contestproject;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by SOOMIN on 2017-11-01.
 */

public class Fragment5 extends android.support.v4.app.Fragment {
    MyApplication myApplication;
    TextView logView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment5, container, false);
        MyApplication myApplication = (MyApplication) getActivity().getApplicationContext();
        Log.d("Main", "onCreate");
        //if (myApplication.locationPermission) {


            logView = (TextView) viewGroup.findViewById(R.id.dotx);
            logView.setText("GPS 가 잡혀야 좌표가 구해짐");
            try {
                LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

                boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

                Log.d("Main", "isGPSEnabled=" + isGPSEnabled);
                Log.d("Main", "isNetworkEnabled=" + isNetworkEnabled);

                LocationListener locationListener = new LocationListener() {
                    public void onLocationChanged(Location location) {
                        double lat = location.getLatitude();
                        double lng = location.getLongitude();

                        logView.setText("latitude: " + lat + ", longitude: " + lng);
                    }

                    public void onStatusChanged(String provider, int status, Bundle extras) {
                        logView.setText("onStatusChanged");
                    }

                    public void onProviderEnabled(String provider) {
                        logView.setText("onProviderEnabled");
                    }

                    public void onProviderDisabled(String provider) {
                        logView.setText("onProviderDisabled");
                    }
                };
 locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

               String locationProvider = LocationManager.GPS_PROVIDER;
                Location lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);
                if (lastKnownLocation != null) {
                    double lng = lastKnownLocation.getLatitude();
                    double lat = lastKnownLocation.getLatitude();
                    Log.d("Main", "longtitude=" + lng + ", latitude=" + lat);
                }
            } catch (SecurityException e) {

            }
      //  }
        return viewGroup;
    }
}