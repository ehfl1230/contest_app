package com.example.soomin.contestproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.net.URLEncoder;
import java.util.ArrayList;

public class AddDongActivity extends AppCompatActivity implements View.OnClickListener {

    ArrayList<ItemVO> datas;
    ListView listView;
    ImageView searchBtn;
    EditText searchField;
    String keyword = "";
    AddDongAdapter adapter;
    String text;
    Spinner spinner;
    Spinner spinner_dong;
    TextView save_btn;
    String type_dong = "";
    String type = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_dong);
        datas = new ArrayList<>();
        listView = (ListView) findViewById(R.id.search_list);
        save_btn = (TextView) findViewById(R.id.save_btn);
        searchBtn = (ImageView) findViewById(R.id.add_dong_search_btn);
        searchField = (EditText) findViewById(R.id.add_dong_search_field);
        searchField.clearFocus();
        save_btn.setOnClickListener(this);
        searchBtn.setOnClickListener(this);
        listView.setItemsCanFocus(false);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        spinner = (Spinner) findViewById(R.id.spinner_search_type);
        spinner_dong = (Spinner) findViewById(R.id.spinner_dong);
        ArrayAdapter typeAdapter = ArrayAdapter.createFromResource(this, R.array.search_type, R.layout.custom_simple_drop_item);
        typeAdapter.setDropDownViewResource(R.layout.custom_simple_drop_item);
        spinner.setAdapter(typeAdapter);
        ArrayAdapter dongAdapter = ArrayAdapter.createFromResource(this, R.array.dong_type, R.layout.custom_simple_drop_item);
        dongAdapter.setDropDownViewResource(R.layout.custom_simple_drop_item);
        spinner_dong.setAdapter(dongAdapter);

        adapter = new AddDongAdapter(this, R.layout.add_dong_item, datas);
        listView.setAdapter(adapter);
        addItems("hospital", "", keyword);

    }

    @Override
    public void onClick(View v) {
        if (v == searchBtn) {
            if (spinner.getSelectedItem() == null || type_dong.equals(""))
                type = "name";
            else {
                text = spinner.getSelectedItem().toString();
                if (text.equals("이름"))
                    type = "name";
                else if (text.equals("주소"))
                    type = "address";
            }
            if (spinner_dong.getSelectedItem() == null || type_dong.equals(""))
                type_dong = "hospital";
            else {

                text = spinner.getSelectedItem().toString();
                if (text.equals("병원"))
                    type_dong = "hospital";
                else if (text.equals("약국"))
                    type_dong = "drugstore";
            }

            keyword = searchField.getText().toString();
                addItems(type_dong, type, keyword);

        }
        if (v == save_btn) {
            ItemVO item = datas.get(adapter.getSelectedRadioPosition());
            System.out.println("selected" + item.apiDongName + " " + item.apiTel);
            Intent intent = new Intent();
            intent.putExtra("name", item.apiDongName);
            intent.putExtra("tel", item.apiTel);
            intent.putExtra("address", item.apiNewAddress);
            intent.putExtra("type", type_dong);
            setResult(RESULT_OK, intent);
            finish();
        }
    }


    private void addItems(String type_dong, String type, String keyword) {
        try {
            ListParser task = new ListParser();
            ArrayList<String> params = new ArrayList<>();
            String url = "";
            if (type_dong.equals("hospital")) {
                if (keyword.equals("")) {
                    url = "http://openapi.jeonju.go.kr/rest/dongmulhospitalservice/getDongMulHospital?ServiceKey=" + new data().apiKey +
                            "&pageNo=1&numOfRows=100&address=" + "" + "&dongName=";
                } else {
                    if (type.equals("name")) {
                        url = "http://openapi.jeonju.go.kr/rest/dongmulhospitalservice/getDongMulHospital?ServiceKey=" + new data().apiKey +
                                "&pageNo=1&numOfRows=100&dongName=" + URLEncoder.encode(keyword, "UTF-8");
                    }
                    if (type.equals("address")) {
                        url = "http://openapi.jeonju.go.kr/rest/dongmulhospitalservice/getDongMulHospital?ServiceKey=" + new data().apiKey +
                                "&pageNo=1&numOfRows=100&address=" + URLEncoder.encode(keyword, "UTF-8");
                    }
                }
            }
            if (type_dong.equals("drugstore")) {
                if (keyword.equals("")) {
                    url = " http://openapi.jeonju.go.kr/rest/dongmuldrucservice/getDongMulDruc?ServiceKey=" + new data().apiKey +
                            "&pageNo=1&numOfRows=100&address=" + "" + "&dongName=" ;
                } else {
                    if (type.equals("name")) {
                        url = "http://openapi.jeonju.go.kr/rest/dongmuldrucservice/getDongMulDruc?ServiceKey=" + new data().apiKey +
                                "&pageNo=1&numOfRows=100&dongName=" + URLEncoder.encode(keyword, "UTF-8");
                    }
                    if (type.equals("address")) {
                        url = "http://openapi.jeonju.go.kr/rest/dongmuldrucservice/getDongMulDruc?ServiceKey=" + new data().apiKey +
                                "&pageNo=1&numOfRows=100&address=" + URLEncoder.encode(keyword, "UTF-8");
                    }
                }
            }
            params.add(0, url);
            datas.clear();
            ArrayList<ItemVO> item_list = task.execute(params).get();
            for (int i = 0; i < item_list.size(); i++) {
                datas.add(item_list.get(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        adapter.notifyDataSetChanged();
    }

}
