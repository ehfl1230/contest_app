package com.example.soomin.contestproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URLEncoder;
import java.util.ArrayList;

public class AddDongActivity extends AppCompatActivity implements View.OnClickListener {

    ArrayList<ItemVO> datas;
    ListView listView;
    ImageView searchBtn;
    TextView moveBookmark;
    EditText searchField;
    String keyword = "";
    AddDongAdapter adapter;
    RelativeLayout no_data;
    String text;
    Spinner spinner_type;
    Spinner spinner_dong;
    TextView save_btn;
    String type_dong = "";
    String type = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_dong);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(ContextCompat.getDrawable(this, R.color.blue));
        actionBar.setTitle("");
        actionBar.setDisplayHomeAsUpEnabled(true);
        datas = new ArrayList<>();
        listView = (ListView) findViewById(R.id.search_list);
        save_btn = (TextView) findViewById(R.id.save_btn);
        searchBtn = (ImageView) findViewById(R.id.add_dong_search_btn);
        searchField = (EditText) findViewById(R.id.add_dong_search_field);
        moveBookmark = (TextView) findViewById(R.id.move_bookmark);
        no_data = (RelativeLayout) findViewById(R.id.no_data);
        no_data.setVisibility(View.GONE);
        searchField.clearFocus();
        save_btn.setOnClickListener(this);
        searchBtn.setOnClickListener(this);
        listView.setItemsCanFocus(false);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        spinner_dong = (Spinner) findViewById(R.id.spinner_dong);
        spinner_type = (Spinner) findViewById(R.id.spinner_search_type);

        ArrayAdapter typeAdapter = ArrayAdapter.createFromResource(this, R.array.search_type, R.layout.custom_simple_drop_item);
        typeAdapter.setDropDownViewResource(R.layout.custom_simple_drop_item);
        spinner_type.setAdapter(typeAdapter);
        ArrayAdapter dongAdapter = ArrayAdapter.createFromResource(this, R.array.dong_type, R.layout.custom_simple_drop_item);
        dongAdapter.setDropDownViewResource(R.layout.custom_simple_drop_item);
        spinner_dong.setAdapter(dongAdapter);

        adapter = new AddDongAdapter(this, R.layout.add_dong_item, datas);
        listView.setAdapter(adapter);
        addItems("hospital", "", keyword);

    }

    public static void downKeyboard(Context context, EditText editText) {
        InputMethodManager mInputMethodManager = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        mInputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //    NavUtils.navigateUpFromSameTask(this);

                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == searchBtn) {
            downKeyboard(this, searchField);
            if (spinner_type.getSelectedItem() == null || spinner_type.getSelectedItem().equals(""))
                type = "name";
            else {
                text = spinner_type.getSelectedItem().toString();
                if (text.equals("이름"))
                    type = "name";
                else if (text.equals("주소"))
                    type = "address";
            }
            if (spinner_dong.getSelectedItem() == null || spinner_dong.getSelectedItem().equals(""))
                type_dong = "hospital";
            else {

                text = spinner_dong.getSelectedItem().toString();
                if (text.equals("병원"))
                    type_dong = "hospital";
                else if (text.equals("약국"))
                    type_dong = "drugstore";
            }
            keyword = searchField.getText().toString();
            addItems(type_dong, type, keyword);

        }
        if (v == save_btn) {
            if(adapter.getSelectedRadioPosition() == -1){
                Toast.makeText(this, "의료기관을 선택해주세요.", Toast.LENGTH_SHORT).show();
            }
            else {
            ItemVO item = datas.get(adapter.getSelectedRadioPosition());
                Intent intent = new Intent();
                intent.putExtra("name", item.apiDongName);
                intent.putExtra("tel", item.apiTel);
                intent.putExtra("address", item.apiNewAddress);
                intent.putExtra("type", type_dong);
                intent.putExtra("old_address", item.apiOldAddress);
                intent.putExtra("lat", item.apiLat);
                intent.putExtra("lng", item.apiLng);
                setResult(RESULT_OK, intent);
                finish();
            }
        }
        if (v == moveBookmark) {
            Intent intent = new Intent(this, BookmarkActivity.class);
            startActivity(intent);
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
                            "&pageNo=1&numOfRows=100&address=" + "" + "&dongName=";
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
        if (datas.size() == 0) {
            no_data.setVisibility(View.VISIBLE);
        } else {
            no_data.setVisibility(View.GONE);
        }
    }

}
