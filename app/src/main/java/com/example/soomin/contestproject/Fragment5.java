package com.example.soomin.contestproject;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import net.daum.mf.map.api.CameraUpdateFactory;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapPointBounds;
import net.daum.mf.map.api.MapPolyline;
import net.daum.mf.map.api.MapView;

import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by SOOMIN on 2017-11-01.
 */

public class Fragment5 extends android.support.v4.app.Fragment {
    MyApplication myApplication;

    ArrayList<ItemVO> datas;
    ArrayList<ItemVO> nearest_data;
    ListAdapter adapter;
    ListView listView;
    TextView hospitalBtn;
    TextView drugBtn;
    MapPOIItem marker;
    MapPOIItem marker2;
    MapPOIItem marker3;
    ViewGroup mapViewContainer;
    MapView mapView;
    ItemVO nearest;
    ItemVO second_nearest;
    int type = 0;
    MapPolyline polyline;
    String url = " http://openapi.jeonju.go.kr/rest/dongmulhospitalservice/getDongMulHospital?ServiceKey=" + new data().apiKey +
            "&pageNo=1&numOfRows=100&address=" + "" + "&dongName=";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment5, container, false);
        MyApplication myApplication = (MyApplication) getActivity().getApplicationContext();
        listView = (ListView) viewGroup.findViewById(R.id.search_list);
        //if (myApplication.locationPermission) {
        hospitalBtn = (TextView) viewGroup.findViewById(R.id.find_nearest_hospital);
        mapViewContainer = (ViewGroup) viewGroup.findViewById(R.id.map_view);
        mapView = new MapView(getActivity());
        polyline = new MapPolyline();
        hospitalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                url = " http://openapi.jeonju.go.kr/rest/dongmulhospitalservice/getDongMulHospital?ServiceKey=" + new data().apiKey +
                        "&pageNo=1&numOfRows=100&address=" + "" + "&dongName=";
                type = 1;
                onResume();
            }
        });
        drugBtn = (TextView) viewGroup.findViewById(R.id.find_nearest_drugstore);
        drugBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                url = " http://openapi.jeonju.go.kr/rest/dongmuldrucservice/getDongMulDruc?ServiceKey=" + new data().apiKey +
                        "&pageNo=1&numOfRows=70&address=" + "" + "&dongName=";
                type = 2;
                onResume();
            }
        });
        datas = new ArrayList<>();
        nearest_data = new ArrayList<>();
        addItems("", "");
        getLocation();
        mapViewContainer.addView(mapView);
        adapter = new ListAdapter(getContext(), R.layout.list_item, nearest_data);
        listView.setAdapter(adapter);
        return viewGroup;
    }

    public void getLocation() {

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

                    //logView.setText("latitude: " + lat + ", longitude: " + lng);
                }

                public void onStatusChanged(String provider, int status, Bundle extras) {
                    // logView.setText("onStatusChanged");
                }

                public void onProviderEnabled(String provider) {
                    // logView.setText("onProviderEnabled");
                }

                public void onProviderDisabled(String provider) {
                    //  logView.setText("onProviderDisabled");
                }
            };
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

            String locationProvider = LocationManager.GPS_PROVIDER;
            Location lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);
            if (lastKnownLocation != null) {
                double lng = lastKnownLocation.getLongitude();
                double lat = lastKnownLocation.getLatitude();
                nearest = new ItemVO();
                second_nearest = new ItemVO();
                Double distance = -1.0;
                Double second_distance = -1.0;
                for (int i = 0; i < datas.size(); i++) {

                    double distanceMeter =
                            distance(lng, lat, Double.parseDouble(datas.get(i).apiLng), Double.parseDouble(datas.get(i).apiLat), "meter");
                    // 킬로미터(Kilo Meter) 단위
                    if (distance == -1) {
                        distance = distanceMeter;
                    } else if (distance > distanceMeter) {
                        second_nearest = nearest;
                        nearest = datas.get(i);
                        second_distance = distance;
                        distance = distanceMeter;
                    }
                    double distanceKiloMeter =
                            distance(lng, lat, Double.parseDouble(datas.get(i).apiLng), Double.parseDouble(datas.get(i).apiLat), "kilometer");


                }
                if (nearest.apiLat == null || second_nearest.apiLat == null) {
                    Toast.makeText(getActivity(), "위치를 찾지 못했습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                } else {

                    polyline.setTag(1000);
                    polyline.addPoint(MapPoint.mapPointWithGeoCoord(Double.parseDouble(nearest.apiLat), Double.parseDouble(nearest.apiLng)));
                    polyline.addPoint(MapPoint.mapPointWithGeoCoord(Double.parseDouble(second_nearest.apiLat), Double.parseDouble(second_nearest.apiLng)));

                    polyline.addPoint(MapPoint.mapPointWithGeoCoord(lat, lng));

                    mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(lat, lng), true);
                    marker = new MapPOIItem();
                    marker.setItemName(nearest.apiDongName);
                    marker.setTag(0);

                    marker.setMapPoint(MapPoint.mapPointWithGeoCoord(Double.parseDouble(nearest.apiLat),
                            Double.parseDouble(nearest.apiLng)));
                    marker.setMarkerType(MapPOIItem.MarkerType.BluePin);
                    marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin);

                    marker2 = new MapPOIItem();
                    marker2.setItemName(second_nearest.apiDongName);
                    marker2.setTag(0);

                    marker2.setMapPoint(MapPoint.mapPointWithGeoCoord(Double.parseDouble(second_nearest.apiLat), Double.parseDouble(second_nearest.apiLng)));
                    marker2.setMarkerType(MapPOIItem.MarkerType.BluePin);
                    marker2.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin);

                    marker3 = new MapPOIItem();
                    marker3.setItemName("현재위치");
                    marker3.setTag(0);

                    marker3.setMapPoint(MapPoint.mapPointWithGeoCoord(lat, lng));
                    marker3.setMarkerType(MapPOIItem.MarkerType.BluePin);
                    marker3.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin);
                    mapView.addPOIItem(marker);
                    mapView.addPOIItem(marker2);
                    mapView.addPOIItem(marker3);
                    MapPointBounds mapPointBounds = new MapPointBounds(polyline.getMapPoints());
                    int padding = 100; // px
                    mapView.moveCamera(CameraUpdateFactory.newMapPointBounds(mapPointBounds, padding));


                    nearest_data.clear();
                    nearest_data.add(nearest);
                    nearest_data.add(second_nearest);
                }
            }
        } catch (SecurityException e) {

        }
    }

    @Override
    public void onPause() {
        super.onPause();
        System.out.println("onPause dfjslkdfjlksjflksdf");
        //mapViewContainer.removeAllViews();
    }

    @Override
    public void onStop() {
        super.onStop();
        System.out.println("onStop dfjslkdfjlksjflksdf");
    }

    @Override
    public void onResume() {
        super.onResume();
        System.out.println("onResume ksjdflkjsdklfjslkdjflkj");
        mapView.refreshMapTiles();
        if (type != 0) {

            mapView.removePOIItem(marker);
            mapView.removePOIItem(marker2);
            addItems("", "");
            getLocation();

            //mapView.removePOIItem(marker);
            //         mapView.removeAllPOIItems();


          /*  marker = new MapPOIItem();
            marker.setItemName(nearest.apiDongName);
            marker.setTag(0);
            marker.setMapPoint(MapPoint.mapPointWithGeoCoord(Double.parseDouble(nearest.apiLat),
                    Double.parseDouble(nearest.apiLng)));
            marker2 = new MapPOIItem();
            marker2.setItemName(second_nearest.apiDongName);
            marker2.setTag(1);
            marker2.setMapPoint(MapPoint.mapPointWithGeoCoord(Double.parseDouble(second_nearest.apiLat),
                    Double.parseDouble(second_nearest.apiLng)));
          */  //  mapView.removePOIItem(marker);
            // mapView.addPOIItem(marker);
            //    mapView.refreshMapTiles();
            adapter.notifyDataSetChanged();
        }
    }

    private void addItems(String type, String keyword) {
        try {
            ListParser task = new ListParser();
            ArrayList<String> params = new ArrayList<>();
            params.add(0, url);
            datas.clear();
            ArrayList<ItemVO> item_list = task.execute(params).get();

            for (int i = 0; i < item_list.size(); i++) {
                ItemVO vo = item_list.get(i);
                datas.add(vo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 두 지점간의 거리 계산
     *
     * @param lat1 지점 1 위도
     * @param lon1 지점 1 경도
     * @param lat2 지점 2 위도
     * @param lon2 지점 2 경도
     * @param unit 거리 표출단위
     * @return
     */
    private static double distance(double lat1, double lon1, double lat2, double lon2, String unit) {

        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));

        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;

        if (unit == "kilometer") {
            dist = dist * 1.609344;
        } else if (unit == "meter") {
            dist = dist * 1609.344;
        }

        return (dist);
    }


    // This function converts decimal degrees to radians
    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    // This function converts radians to decimal degrees
    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }

}