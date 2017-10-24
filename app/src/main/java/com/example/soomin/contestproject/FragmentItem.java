package com.example.soomin.contestproject;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by SOOMIN on 2017-10-24.
 */

public class FragmentItem extends AppCompatActivity {
    ArrayList<ItemVO> datas;
    public TextView dongNameView;
    public TextView telView;
    public TextView newAddressView;
    public TextView oldAddressView;
    public ImageView callBtnView;
    String name = "";
    String type = "";
    String address = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_detail);

        dongNameView = (TextView) findViewById(R.id.dong_name_text);
        telView = (TextView) findViewById(R.id.tel_text);
        newAddressView = (TextView) findViewById(R.id.new_address_text);
        oldAddressView = (TextView) findViewById(R.id.old_address_text);
        callBtnView = (ImageView) findViewById(R.id.call_btn);
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
                        "&pageNo=1&numOfRows=100&address=" + ""  + "&dongName=" + URLEncoder.encode(name_param, "UTF-8");

            params.add(0, url);
            ArrayList<ItemVO> item_list = task.execute(params).get();
            for (int i = 0; i < item_list.size(); i++) {
                datas.add(item_list.get(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
