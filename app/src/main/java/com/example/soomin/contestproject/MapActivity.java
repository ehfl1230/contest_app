package com.example.soomin.contestproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

public class MapActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        String name = getIntent().getExtras().getString("name");
        String posy = getIntent().getExtras().getString("posy");
        String posx = getIntent().getExtras().getString("posx");
        System.out.println(name + " " + posy + " " + posx);
        ViewGroup mapViewContainer = (ViewGroup) findViewById(R.id.map_view);
        MapView mapView = new MapView(this);


        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(Double.parseDouble(posy),
                Double.parseDouble(posx)), true);
        MapPOIItem marker = new MapPOIItem();
        marker.setItemName(name);
        marker.setTag(0);

        marker.setMapPoint(MapPoint.mapPointWithGeoCoord(Double.parseDouble(posy),
                Double.parseDouble(posx)));
        marker.setMarkerType(MapPOIItem.MarkerType.BluePin); // 기본으로 제공하는 BluePin 마커 모양.
        marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.

        mapView.addPOIItem(marker);
        mapViewContainer.addView(mapView);
    }
}
