package com.example.soomin.contestproject;

import android.content.Intent;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
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
        searchField = (EditText) viewGroup.findViewById(R.id.search_hospital);
        searchField.clearFocus();
        spinner = (Spinner) viewGroup.findViewById(R.id.spinner_type);
        ArrayAdapter typeAdapter = ArrayAdapter.createFromResource(this.getContext(), R.array.search_type, android.R.layout.simple_spinner_item);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(typeAdapter);
        adapter = new ListAdapter(this.getContext(), R.layout.list_item, datas);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String name = datas.get(position).apiDongName;
                String address = datas.get(position).apiNewAddress;
                Intent intent = new Intent(Fragment2.super.getActivity(), FragmentItem.class);
                intent.putExtra("type", "drugstore");
                intent.putExtra("name", name);
                intent.putExtra("address", address);

                startActivity(intent);
            }
        });
        listView.setAdapter(adapter);
        addItems("", keyword);

        return viewGroup;

    }

    @Override
    public void onClick(View v) {
        if (v == searchBtn) {
            if (spinner.getSelectedItem() == null)
                text = "";
            else {
                text = spinner.getSelectedItem().toString();
            }
            keyword = searchField.getText().toString();
            if (text.equals(" 이름 ")) {
                addItems("name", keyword);
            }
            if (text.equals(" 주소 ")) {
                addItems("address", keyword);
            }
        }
    }


    private void addItems(String type, String keyword) {
        try {
            ListParser task = new ListParser();
            ArrayList<String> params = new ArrayList<>();
            String url = "";
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

