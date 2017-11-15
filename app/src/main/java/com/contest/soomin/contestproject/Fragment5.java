package com.contest.soomin.contestproject;

import android.Manifest;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.ActivityCompat;

import net.daum.mf.map.api.CameraUpdateFactory;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapPointBounds;
import net.daum.mf.map.api.MapPolyline;
import net.daum.mf.map.api.MapView;

import java.util.ArrayList;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

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
    RelativeLayout no_data;
    ItemVO nearest;
    String from = "";
    ItemVO second_nearest;
    LinearLayout ll;
    private static final int msg = 1;
    int type = 0;
    MapPolyline polyline;
    ViewGroup viewGroup;
    String url = " http://openapi.jeonju.go.kr/rest/dongmulhospitalservice/getDongMulHospital?ServiceKey=" + new data().apiKey +
            "&pageNo=1&numOfRows=100&address=" + "" + "&dongName=";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment5, container, false);
        listView = (ListView) viewGroup.findViewById(R.id.search_list);
        //if (myApplication.locationPermission) {
        hospitalBtn = (TextView) viewGroup.findViewById(R.id.find_nearest_hospital);
        datas = new ArrayList<>();
        nearest_data = new ArrayList<>();
        no_data= (RelativeLayout) viewGroup.findViewById(R.id.no_data);
        adapter = new ListAdapter(getContext(), R.layout.list_item, nearest_data);
ll = (LinearLayout) viewGroup.findViewById(R.id.data);
   //     addItems("", "");
//
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            no_data.setVisibility(View.VISIBLE);
            ll.setVisibility(View.GONE);
        } else {
            no_data.setVisibility(View.GONE);
            ll.setVisibility(View.VISIBLE);
        }

        hospitalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                url = " http://openapi.jeonju.go.kr/rest/dongmulhospitalservice/getDongMulHospital?ServiceKey=" + new data().apiKey +
                        "&pageNo=1&numOfRows=100&address=" + "" + "&dongName=";
                type = 1;
                hospitalBtn.setBackgroundResource(R.drawable.border_solid);
                hospitalBtn.setTextColor(getResources().getColor(R.color.white));
                drugBtn.setBackgroundResource(R.drawable.border);
                drugBtn.setTextColor(getResources().getColor(R.color.dark_gray));
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
                drugBtn.setBackgroundResource(R.drawable.border_solid);
                drugBtn.setTextColor(getResources().getColor(R.color.white));
                hospitalBtn.setBackgroundResource(R.drawable.border);
                hospitalBtn.setTextColor(getResources().getColor(R.color.dark_gray));
                onResume();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String name = nearest_data.get(position).apiDongName;
                String address = nearest_data.get(position).apiNewAddress;
                String old_address = nearest_data.get(position).apiOldAddress;
                String tel = nearest_data.get(position).apiTel;
                String lat = nearest_data.get(position).apiLat;
                String lng = nearest_data.get(position).apiLng;
                Intent intent = new Intent();
                intent.setClass(Fragment5.super.getActivity(), FragmentItem.class);
                intent.putExtra("from", "near");
                intent.putExtra("type", "hospital");
                intent.putExtra("name", name);
                intent.putExtra("address", address);
                intent.putExtra("old_address", old_address);
                intent.putExtra("tel", tel);
                intent.putExtra("lat", lat);
                intent.putExtra("lng", lng);
                mapViewContainer.removeAllViews();

                startActivityForResult(intent, msg);
            }
        });
        listView.setAdapter(adapter);

        myApplication = (MyApplication) getActivity().getApplicationContext();
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            myApplication.locationPermission = true;
            mapViewContainer = (ViewGroup) viewGroup.findViewById(R.id.map_view);
            marker = new MapPOIItem();
            marker2 = new MapPOIItem();
            marker3 = new MapPOIItem();

            polyline = new MapPolyline();
         //   getLocation();
            System.out.println("32322111111111111111111111111111" + mapView);
            mapViewContainer.removeAllViews();

                mapView = new MapView(getActivity());

                mapViewContainer.addView(mapView);

        }
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
                System.out.print(nearest.apiLat + " " +  second_nearest.apiLat );
                if (nearest.apiLat == null || second_nearest.apiLat == null) {
                    Toast.makeText(getActivity(), "위치를 찾지 못했습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show();

                } else {
                    if (from.equals("item")) {
                        mapView = new MapView(getActivity());
                    }
                    polyline.setTag(1000);
                    polyline.addPoint(MapPoint.mapPointWithGeoCoord(Double.parseDouble(nearest.apiLat), Double.parseDouble(nearest.apiLng)));
                    polyline.addPoint(MapPoint.mapPointWithGeoCoord(Double.parseDouble(second_nearest.apiLat), Double.parseDouble(second_nearest.apiLng)));

                    polyline.addPoint(MapPoint.mapPointWithGeoCoord(lat, lng));

                    mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(lat, lng), true);
                    marker.setItemName(nearest.apiDongName + " \n" + String.format("%.2f", (distance / 1000)) + "km");
                    marker.setTag(0);

                    marker.setMapPoint(MapPoint.mapPointWithGeoCoord(Double.parseDouble(nearest.apiLat),
                            Double.parseDouble(nearest.apiLng)));
                    marker.setMarkerType(MapPOIItem.MarkerType.CustomImage);
                    marker.setCustomImageResourceId(R.drawable.marker2); // 마커 이미지.
                    //marker.setMarkerType(MapPOIItem.MarkerType.BluePin);
                    //marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin);


                    marker2.setItemName(second_nearest.apiDongName + " \n" + String.format("%.2f", (second_distance / 1000)) + "km");
                    marker2.setTag(0);

                    marker2.setMapPoint(MapPoint.mapPointWithGeoCoord(Double.parseDouble(second_nearest.apiLat), Double.parseDouble(second_nearest.apiLng)));
                    marker2.setMarkerType(MapPOIItem.MarkerType.CustomImage);
                    marker2.setCustomImageResourceId(R.drawable.marker2); // 마커 이미지.
                    //marker2.setMarkerType(MapPOIItem.MarkerType.BluePin);
                    //marker2.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin);


                    marker3.setItemName("현재위치");
                    marker3.setTag(0);

                    marker3.setMapPoint(MapPoint.mapPointWithGeoCoord(lat, lng));
                    marker3.setMarkerType(MapPOIItem.MarkerType.CustomImage);
                    marker3.setCustomImageResourceId(R.drawable.crosshair); // 마커 이미지.
                //    marker3.setMarkerType(MapPOIItem.MarkerType.BluePin);
                 //   marker3.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin);
                    mapView.addPOIItem(marker);
                    mapView.addPOIItem(marker2);
                    mapView.addPOIItem(marker3);
                    MapPointBounds mapPointBounds = new MapPointBounds(polyline.getMapPoints());
                    int padding = 100; // px
                    mapView.moveCamera(CameraUpdateFactory.newMapPointBounds(mapPointBounds, padding));


                    nearest_data.clear();
                    nearest_data.add(nearest);
                    nearest_data.add(second_nearest);
                    if (from.equals("item")) {
                        System.out.println("다시 붙여!");
                        mapViewContainer.addView(mapView);

                        from = "";
                    }
                }
            }
        } catch (SecurityException e) {

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            from = data.getStringExtra("from");
            addItems("", "");
            getLocation();
        }
        if (requestCode == RESULT_CANCELED) {

        }
    }

    @Override
    public void onResume() {
        super.onResume();


        if (mapView != null) {

            try {
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    no_data.setVisibility(View.VISIBLE);
                    ll.setVisibility(View.GONE);
                } else {
                    no_data.setVisibility(View.GONE);
                    ll.setVisibility(View.VISIBLE);
                    mapView.removePOIItem(marker);
                    mapView.removePOIItem(marker2);
                    addItems("", "");
                    getLocation();
                    adapter.notifyDataSetChanged();
                }

            } catch (NullPointerException e) {


            }
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