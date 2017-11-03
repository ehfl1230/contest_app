package com.example.soomin.contestproject;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
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
 * Created by SOOMIN on 2017-10-24.
 */

public class Fragment1 extends Fragment implements View.OnClickListener {

    ArrayList<ItemVO> datas;
    ListView listView;
    ListView mapListView;
    ImageView searchBtn;
    EditText searchField;
    TextView all;
    TextView bookmark;
    LinearLayout search_layout;
    RelativeLayout rl;
    TextView near;
    RelativeLayout ml;
    String keyword = "";
    ListAdapter adapter;
    ListAdapter adapter2;
    String text;
    Spinner spinner;
    int type = 0;
    ArrayList<ItemVO> nearest_data;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment1, container, false);
        datas = new ArrayList<>();
        FragmentManager fm = getFragmentManager();
        fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        listView = (ListView) viewGroup.findViewById(R.id.search_list);
        mapListView = (ListView) viewGroup.findViewById(R.id.map_search_list);
        searchBtn = (ImageView) viewGroup.findViewById(R.id.search_hospital_btn);
        all = (TextView) viewGroup.findViewById(R.id.all);
        ml = (RelativeLayout) viewGroup.findViewById(R.id.map_layout);
        bookmark = (TextView) viewGroup.findViewById(R.id.bookmark);
        near = (TextView) viewGroup.findViewById(R.id.near);
        search_layout = (LinearLayout) viewGroup.findViewById(R.id.search_layout);
        //rl = (RelativeLayout) viewGroup.findViewById(R.id.map_view);
        searchBtn.setOnClickListener(this);
        all.setOnClickListener(this);
        bookmark.setOnClickListener(this);
        near.setOnClickListener(this);
                searchField = (EditText) viewGroup.findViewById(R.id.search_hospital);
        searchField.clearFocus();
        spinner = (Spinner) viewGroup.findViewById(R.id.spinner_type);

        return viewGroup;

    }


    @Override
    public void onResume() {
        super.onResume();

        ArrayAdapter typeAdapter = ArrayAdapter.createFromResource(this.getContext(), R.array.search_type, R.layout.custom_simple_drop_item);
        typeAdapter.setDropDownViewResource(R.layout.custom_simple_drop_item);
        spinner.setAdapter(typeAdapter);

        adapter = new ListAdapter(this.getContext(), R.layout.list_item, datas);
        if (type==3) {
            mapListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    String name = datas.get(position).apiDongName;
                    String address = datas.get(position).apiNewAddress;
                    Intent intent = new Intent(Fragment1.super.getActivity(), FragmentItem.class);
                    intent.putExtra("type", "hospital");
                    intent.putExtra("name", name);
                    intent.putExtra("address", address);

                    startActivity(intent);
                }
            });
            mapListView.setAdapter(adapter);

        } else {
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    String name = datas.get(position).apiDongName;
                    String address = datas.get(position).apiNewAddress;
                    Intent intent = new Intent(Fragment1.super.getActivity(), FragmentItem.class);
                    intent.putExtra("type", "hospital");
                    intent.putExtra("name", name);
                    intent.putExtra("address", address);

                    startActivity(intent);
                }
            });
            listView.setAdapter(adapter);
        }
        addItems("", keyword);

    }
    @Override
    public void onClick(View v) {
        if (v == searchBtn) {
            downKeyboard(getContext(), searchField);
            rl.setVisibility(View.GONE);
            if (spinner.getSelectedItem() == null)
                text = "";
            else {
                text = spinner.getSelectedItem().toString();
            }
            keyword = searchField.getText().toString();
            if (text.equals("이름")) {
                addItems("name", keyword);
            }
            if (text.equals("주소")) {
                addItems("address", keyword);
            }

        }
        if(v == all) {
            type = 1;
            search_layout.setVisibility(View.VISIBLE);
            spinner.setVisibility(View.VISIBLE);
            searchBtn.setVisibility(View.VISIBLE);
            searchField.setVisibility(View.VISIBLE);
            listView.setVisibility(View.VISIBLE);
           // rl.setVisibility(View.GONE);
            ml.setVisibility(View.GONE);
            datas.clear();
            addItems("", keyword);
        }
        if(v == bookmark) {
            type=2;
            search_layout.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
           // rl.setVisibility(View.GONE);
            ml.setVisibility(View.GONE);
            spinner.setVisibility(View.GONE);
            searchField.setVisibility(View.GONE);
            searchBtn.setVisibility(View.GONE);

            DBHelper helper = new DBHelper(getActivity());
            SQLiteDatabase db = helper.getWritableDatabase();
            datas.clear();

            Cursor cursor = db.rawQuery("select * from bookmark", null);

            while (cursor.moveToNext()) {
                ItemVO vo = new ItemVO();
                vo.apiSid = Integer.toString(cursor.getInt(0));
                vo.apiDongName = cursor.getString(1);
                vo.apiNewAddress= cursor.getString(2);
                vo.apiTel = cursor.getString(3);
                vo.type = cursor.getString(4);
                datas.add(vo);
            }

        }if (v==near) {
            type=3;
            addItems("", "");
            search_layout.setVisibility(View.GONE);
            ml.setVisibility(View.VISIBLE);
          //  rl.setVisibility(View.VISIBLE);

            //search_layout.setVisibility(View.GONE);
            //rl.setVisibility(View.VISIBLE);

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
                    ItemVO nearest = new ItemVO();

                    ItemVO second_nearest = new ItemVO();
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
                        return;
                    }
                    ViewGroup mapViewContainer =
                            (ViewGroup) getActivity().findViewById(R.id.map_view);
                //    mapViewContainer.removeAllViews();
                    MapView mapView= new MapView(getActivity());
                    MapPolyline polyline= new MapPolyline();

                    polyline.setTag(1000);
                    polyline.addPoint(MapPoint.mapPointWithGeoCoord(Double.parseDouble(nearest.apiLat), Double.parseDouble(nearest.apiLng)));
                    polyline.addPoint(MapPoint.mapPointWithGeoCoord(Double.parseDouble(second_nearest.apiLat), Double.parseDouble(second_nearest.apiLng)));

                    polyline.addPoint(MapPoint.mapPointWithGeoCoord(lat, lng));

                    mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(lat, lng), true);
                    MapPOIItem marker = new MapPOIItem();
                    marker.setItemName(nearest.apiDongName);
                    marker.setTag(0);

                    marker.setMapPoint(MapPoint.mapPointWithGeoCoord(Double.parseDouble(nearest.apiLat),
                            Double.parseDouble(nearest.apiLng)));
                    marker.setMarkerType(MapPOIItem.MarkerType.BluePin);
                    marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin);

                    MapPOIItem marker2 = new MapPOIItem();
                    marker2.setItemName(second_nearest.apiDongName);
                    marker2.setTag(0);

                    marker2.setMapPoint(MapPoint.mapPointWithGeoCoord(Double.parseDouble(second_nearest.apiLat), Double.parseDouble(second_nearest.apiLng)));
                    marker2.setMarkerType(MapPOIItem.MarkerType.BluePin);
                    marker2.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin);

                    MapPOIItem marker3 = new MapPOIItem();
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

                    mapViewContainer.addView(mapView);
                    nearest_data = new ArrayList<>();
                    nearest_data.add(nearest);
                    nearest_data.add(second_nearest);
                    datas.clear();
                    datas = nearest_data;
                }
            } catch (SecurityException e) {

            }
        }
        adapter.notifyDataSetChanged();
    }

    public static void downKeyboard(Context context, EditText editText) {
        InputMethodManager mInputMethodManager = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        mInputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    private void addItems(String type, String keyword) {
        try {
            ListParser task = new ListParser();
            ArrayList<String> params = new ArrayList<>();
            String url = "";
            if (keyword.equals("")) {
                url = "http://openapi.jeonju.go.kr/rest/dongmulhospitalservice/getDongMulHospital?ServiceKey=" + new data().apiKey +
                        "&pageNo=1&numOfRows=100&address=" + "" + "&dongName=";
            } else {
                if (type.equals("name")) {
                    url = "http://openapi.jeonju.go.kr/rest/dongmulhospitalservice/getDongMulHospital?ServiceKey=" + new data().apiKey +
                            "&pageNo=1&numOfRows=100&dongName=" + URLEncoder.encode(keyword, "UTF-8");
                }
                if (type.equals("address")) {
                    url = " http://openapi.jeonju.go.kr/rest/dongmulhospitalservice/getDongMulHospital?ServiceKey=" + new data().apiKey +
                            "&pageNo=1&numOfRows=100&address=" + URLEncoder.encode(keyword, "UTF-8");
                }
            }
            params.add(0, url);
            datas.clear();
            ArrayList<ItemVO> item_list = task.execute(params).get();
            DBHelper helper = new DBHelper(getContext());
            SQLiteDatabase db = helper.getWritableDatabase();
            for (int i = 0; i < item_list.size(); i++) {
                ItemVO vo = item_list.get(i);

                Cursor cursor = db.rawQuery("select * from bookmark where dong_name=?", new String[]{vo.apiDongName});
                if (cursor.getCount() == 0) {
                    vo.bookmark = 0;
                }
                else {
                    vo.bookmark = 1;
                }
                datas.add(vo);
            }
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        adapter.notifyDataSetChanged();
    }
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

