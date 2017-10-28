package com.example.soomin.contestproject;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by SOOMIN on 2017-10-24.
 */

public class FragmentItem extends AppCompatActivity implements View.OnClickListener{
    ArrayList<ItemVO> datas;
    public TextView dongNameView;
    public TextView telView;
    public TextView newAddressView;
    public TextView oldAddressView;
    public ImageView callBtnView;
    public ImageView checkMapBtnView;
    String name = "";
    String type = "";
    String address = "";
    MyApplication myApplication;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_detail);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(ContextCompat.getDrawable(this, R.color.blue));
        actionBar.setTitle("");
        actionBar.setDisplayHomeAsUpEnabled(true);
        dongNameView = (TextView) findViewById(R.id.dong_name_text);
        telView = (TextView) findViewById(R.id.tel_text);
        newAddressView = (TextView) findViewById(R.id.new_address_text);
        oldAddressView = (TextView) findViewById(R.id.old_address_text);
        callBtnView = (ImageView) findViewById(R.id.call_phone_image);
        callBtnView.setOnClickListener(this);
        datas = new ArrayList<>();

        name = getIntent().getExtras().getString("name");

        type = getIntent().getExtras().getString("type");

        address = getIntent().getExtras().getString("address");

        setItems(type, name, address);
        System.out.println("name" + name);
        if (datas.size() == 1) {
            dongNameView.setText(datas.get(0).apiDongName);
            telView.setText(datas.get(0).apiTel);
            newAddressView.setText(datas.get(0).apiNewAddress);
            oldAddressView.setText(datas.get(0).apiOldAddress);
            ViewGroup mapViewContainer = (ViewGroup) findViewById(R.id.map_view);
            MapView mapView = new MapView(this);


            mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(Double.parseDouble(datas.get(0).apiLat),
                    Double.parseDouble(datas.get(0).apiLng)), true);
            MapPOIItem marker = new MapPOIItem();
            marker.setItemName(name);
            marker.setTag(0);

            marker.setMapPoint(MapPoint.mapPointWithGeoCoord(Double.parseDouble(datas.get(0).apiLat),
                    Double.parseDouble(datas.get(0).apiLng)));
            marker.setMarkerType(MapPOIItem.MarkerType.BluePin); // 기본으로 제공하는 BluePin 마커 모양.
            marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.

            mapView.addPOIItem(marker);
            mapViewContainer.addView(mapView);

        }
        myApplication = (MyApplication) getApplicationContext();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            myApplication.locationPermission = true;
        }


        if (!myApplication.locationPermission) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 20);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == callBtnView) {
            MyApplication myApplication = (MyApplication) getApplicationContext();
            if (myApplication.callPermission) {
                AlertDialog.Builder alert_confirm = new AlertDialog.Builder(FragmentItem.this);
                alert_confirm.setMessage("통화를 시도하겠습니까?").setCancelable(false).setPositiveButton("전화 걸기",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent();
                                intent.setAction(Intent.ACTION_CALL);
                                String tel = "";

                                if (datas.size() == 1) {
                                    tel = datas.get(0).apiTel;
                                    if (tel.equals("")) {
                                        Toast t = Toast.makeText(FragmentItem.this, R.string.no_tel, Toast.LENGTH_SHORT);
                                        t.show();
                                    }
                                 }
                                intent.setData(Uri.parse("tel:" + tel));
                                startActivity(intent);
                            }
                        }).setNegativeButton("취소",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                AlertDialog alert = alert_confirm.create();
                alert.show();
            } else {
                Toast t = Toast.makeText(FragmentItem.this, R.string.permission_error, Toast.LENGTH_SHORT);
                t.show();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setItems(String type_param, String name_param, String address_param) {
        try {
            ListParser task = new ListParser();
            ArrayList<String> params = new ArrayList<>();
            String url = "";
            if (type_param.equals("hospital"))
                url = "http://openapi.jeonju.go.kr/rest/dongmulhospitalservice/getDongMulHospital?ServiceKey=" + new data().apiKey +
                        "&pageNo=1&numOfRows=100&address=" + "" + "&dongName=" + URLEncoder.encode(name_param, "UTF-8");
            else
                url = "http://openapi.jeonju.go.kr/rest/dongmuldrucservice/getDongMulDruc?ServiceKey=" + new data().apiKey +
                        "&pageNo=1&numOfRows=100&address=" + "" + "&dongName=" + URLEncoder.encode(name_param, "UTF-8");

            params.add(0, url);
            ArrayList<ItemVO> item_list = task.execute(params).get();
            for (int i = 0; i < item_list.size(); i++) {
                datas.add(item_list.get(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 10 && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                myApplication.callPermission = true;
            }
        }
        if (requestCode == 20 && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                myApplication.locationPermission = true;
            }
        }
    }
}
