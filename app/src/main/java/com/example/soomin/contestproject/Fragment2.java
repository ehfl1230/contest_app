package com.example.soomin.contestproject;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by SOOMIN on 2017-10-24.
 */

public class Fragment2 extends Fragment implements View.OnClickListener {

    ArrayList<ItemVO> datas;
    ListView listView;
    ImageView searchBtn;
    EditText searchField;
    String keyword = "";
    ListAdapter adapter;
    RelativeLayout no_data;

    String text;
    Spinner spinner;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment2, container, false);
        datas = new ArrayList<>();
        FragmentManager fm = getFragmentManager();
        fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        listView = (ListView) viewGroup.findViewById(R.id.search_list);
        searchBtn = (ImageView) viewGroup.findViewById(R.id.search_hospital_btn);
        searchBtn.setOnClickListener(this);
        no_data = (RelativeLayout) viewGroup.findViewById(R.id.no_data);
        no_data.setVisibility(View.GONE);
        searchField = (EditText) viewGroup.findViewById(R.id.search_hospital);
        searchField.clearFocus();
        spinner = (Spinner) viewGroup.findViewById(R.id.spinner_type);
        downKeyboard(getContext(), searchField);
        return viewGroup;

    }

    @Override
    public void onResume() {
        super.onResume();

        ArrayAdapter typeAdapter = ArrayAdapter.createFromResource(this.getContext(), R.array.search_type, R.layout.custom_simple_drop_item);
        typeAdapter.setDropDownViewResource(R.layout.custom_simple_drop_item);
        spinner.setAdapter(typeAdapter);
        adapter = new ListAdapter(this.getContext(), R.layout.list_item, datas);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String name = datas.get(position).apiDongName;
                String address = datas.get(position).apiNewAddress;
                String old_address = datas.get(position).apiOldAddress;
                String tel = datas.get(position).apiTel;
                String lat = datas.get(position).apiLat;
                String lng = datas.get(position).apiLng;
                Intent intent = new Intent(Fragment2.super.getActivity(), FragmentItem.class);
                intent.putExtra("type", "hospital");
                intent.putExtra("name", name);
                intent.putExtra("address", address);
                intent.putExtra("old_address", old_address);
                intent.putExtra("tel", tel);
                intent.putExtra("lat", lat);
                intent.putExtra("lng", lng);

                startActivity(intent);
            }
        });
        listView.setAdapter(adapter);
        addItems("", keyword);

    }

    @Override
    public void onClick(View v) {
        if (v == searchBtn) {
            downKeyboard(getContext(), searchField);
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
                url = " http://openapi.jeonju.go.kr/rest/dongmuldrucservice/getDongMulDruc?ServiceKey=" + new data().apiKey +
                        "&pageNo=1&numOfRows=70&address=" + "" + "&dongName=" ;
            } else {
                if (type.equals("name")) {http://openapi.jeonju.go.kr/rest/dongmuldrucserv
                    url = " ice/getDongMulDruc?ServiceKey=" + new data().apiKey +
                            "&pageNo=1&numOfRows=70&dongName=" + URLEncoder.encode(keyword, "UTF-8");
                }
                if (type.equals("address")) {
                    url = " http://openapi.jeonju.go.kr/rest/dongmuldrucservice/getDongMulDruc?ServiceKey=" + new data().apiKey +
                            "&pageNo=1&numOfRows=70&address=" + URLEncoder.encode(keyword, "UTF-8");
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
        if (datas.size() == 0) {
            no_data.setVisibility(View.VISIBLE);
        } else {
            no_data.setVisibility(View.GONE);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        downKeyboard(getContext(), searchField);
    }
}

