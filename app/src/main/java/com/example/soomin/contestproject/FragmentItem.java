package com.example.soomin.contestproject;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import net.daum.android.map.MapViewController;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by SOOMIN on 2017-10-24.
 */

public class FragmentItem extends AppCompatActivity implements View.OnClickListener {
    ArrayList<ItemVO> datas;
    public TextView dongNameView;
    public TextView telView;
    public TextView newAddressView;
    public TextView oldAddressView;
    public ImageView callBtnView;
    public ImageView bookmarkBtn;
    String name = "";
    String type = "";
    String address = "";
    String phone = "";
    String old_address = "";
    String tel = "";
    String lat = "";
    String lng = "";
    String from = "";
    MyApplication myApplication;
    MapView mapView;
    ViewGroup mapViewContainer;
    @Override
    protected void onResume() {
        super.onResume();
        DBHelper helper = new DBHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();

        Cursor cursor = db.rawQuery("select * from bookmark where dong_name=?", new String[] {name});
        if (cursor.getCount() == 0) {
            bookmarkBtn.setColorFilter(ContextCompat.getColor(this, R.color.gray), android.graphics.PorterDuff.Mode.SRC_IN);
        }
        else {
            bookmarkBtn.setColorFilter(ContextCompat.getColor(this, R.color.yellow), android.graphics.PorterDuff.Mode.SRC_IN);

        }
        db.close();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_detail);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("");
        actionBar.setBackgroundDrawable(ContextCompat.getDrawable(this, R.color.blue));
        actionBar.setDisplayHomeAsUpEnabled(true);
        from = getIntent().getExtras().getString("from");
        name = getIntent().getExtras().getString("name");
        type = getIntent().getExtras().getString("type");
        address = getIntent().getExtras().getString("address");
        old_address = getIntent().getExtras().getString("old_address");
        phone = getIntent().getExtras().getString("tel");
        lat = getIntent().getExtras().getString("lat");
        lng = getIntent().getExtras().getString("lng");

        bookmarkBtn = (ImageView) findViewById(R.id.bookmark_btn);
        dongNameView = (TextView) findViewById(R.id.dong_name_text);
        telView = (TextView) findViewById(R.id.tel_text);
        newAddressView = (TextView) findViewById(R.id.new_address_text);
        oldAddressView = (TextView) findViewById(R.id.old_address_text);
        callBtnView = (ImageView) findViewById(R.id.call_phone_image);
        callBtnView.setOnClickListener(this);
        bookmarkBtn.setOnClickListener(this);
        datas = new ArrayList<>();
        System.out.println("skdjfldjf"  + mapView);
        mapView = new MapView(this);
      //  setItems(type, name, address);


       // for (int i = 0; i < datas.size(); i++) {
        //    if (datas.get(i).apiDongName.equals(name)) {
                dongNameView.setText(name);
                //phone = datas.get(i).apiTel;
                telView.setText(phone);
                newAddressView.setText(address);
                oldAddressView.setText(old_address);
                mapViewContainer = (ViewGroup) findViewById(R.id.map_view);



                mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(Double.parseDouble(lat), Double.parseDouble(lng)), true);
                MapPOIItem marker = new MapPOIItem();
                marker.setItemName(name);
                marker.setTag(0);
                mapView.setZoomLevel(3, true);
                marker.setMapPoint(MapPoint.mapPointWithGeoCoord(Double.parseDouble(lat), Double.parseDouble(lng)));
                marker.setMarkerType(MapPOIItem.MarkerType.BluePin);
                marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin);
                mapView.addPOIItem(marker);
                mapViewContainer.addView(mapView);

        //    }
       // }

        myApplication = (MyApplication) getApplicationContext();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            myApplication.locationPermission = true;
        }

        if (!myApplication.locationPermission) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 20);
        }
    }
    @Override
    public void onBackPressed() {
        if (from != null && from.equals("near")) {
            mapViewContainer.removeAllViews();
            Intent intent = new Intent();
            intent.putExtra("from", "item");
            setResult(RESULT_OK, intent);
            finish();
        } else {
            finish();
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


                                    tel = phone;
                                    if (tel.equals("")) {
                                        Toast t = Toast.makeText(FragmentItem.this, R.string.no_tel, Toast.LENGTH_SHORT);
                                        t.show();
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
        if (v == bookmarkBtn) {
            DBHelper helper = new DBHelper(this);
            SQLiteDatabase db = helper.getWritableDatabase();

            Cursor cursor = db.rawQuery("select * from bookmark where dong_name=?", new String[] {name});
            if (cursor.getCount() == 0) {
                db.execSQL("insert into bookmark (dong_name, dong_address, dong_tel, type, dong_old_address, dong_lat, dong_lng) values (?,?,?,?,?,?,?)",
                        new String[]{name, address, phone, type, old_address, lat, lng});
                Toast.makeText(this, "즐겨찾기에 추가되었습니다.", Toast.LENGTH_SHORT).show();

                //  actionBar.setIcon(R.drawable.iconmonstr_star_6_64_yellow);

               // bookmarkBtn.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_stars_black_24dp));
                bookmarkBtn.setColorFilter(ContextCompat.getColor(this, R.color.yellow), android.graphics.PorterDuff.Mode.SRC_IN);
            }
            else {
                db.execSQL("delete from bookmark where dong_name=?", new String[] {name});
                Toast.makeText(this, "즐겨찾기에서 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                bookmarkBtn.setColorFilter(ContextCompat.getColor(this, R.color.gray), android.graphics.PorterDuff.Mode.SRC_IN);
          //      bookmarkBtn.setColorFilter(R.color.colorAccent);
            }
            db.close();

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        ActionBar actionBar = getSupportActionBar();
        switch (item.getItemId()) {
            case android.R.id.home:
                if (from != null && from.equals("near")) {
                    mapViewContainer.removeAllViews();
                    Intent intent = new Intent();
                    setResult(RESULT_OK, intent);

                    finish();
                } else {
                    finish();
                }
                // NavUtils.navigateUpFromSameTask(this);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_menu, menu);
        return true;
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

    @Override
    protected void onPause() {
        super.onPause();

    }
}
