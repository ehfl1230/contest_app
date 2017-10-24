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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by SOOMIN on 2017-10-24.
 */

public class Fragment1 extends Fragment implements View.OnClickListener {

    ArrayList<ItemVO> datas;
    ListView listView;
    ImageView searchBtn;
    EditText searchField;
    String keyword = "";
    ListAdapter adapter;
    private boolean mLockListView;
    private LayoutInflater mInflater;
    View footerView;
    int page_num = 1;
    String type;
    int totalCount;
    int schedule_id;
    String intent_keyword = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment1, container, false);
        datas = new ArrayList<>();
        FragmentManager fm = getFragmentManager();
        fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        listView = (ListView) viewGroup.findViewById(R.id.search_list);
        //searchBtn = (ImageView) findViewById(R.id.search_bnt);
        //searchField = (EditText) findViewById(R.id.search_blank);
        adapter = new ListAdapter(this.getContext(), R.layout.list_item, datas);

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
                //args.putString("type", "hospital");
                //args.putString("name", name);
                //frag.setArguments(args);
                //FragmentTransaction ft = fm.beginTransaction();
                //ft.replace(R.id.container, frag);
                //ft.addToBackStack("back");
                //ft.commit();
            }
        });
        listView.setAdapter(adapter);
//        searchBtn.setOnClickListener(this);
        addItems();

        return viewGroup;

    }

    @Override
    public void onClick(View v) {
        if (v == searchBtn) {
            keyword = searchField.getText().toString();
        }
    }


    private void addItems() {
        try {
            ListParser task = new ListParser();
            ArrayList<String> params = new ArrayList<>();
            String url = "";
            url = "http://openapi.jeonju.go.kr/rest/dongmulhospitalservice/getDongMulHospital?ServiceKey=" + new data().apiKey +
                    "&pageNo=1&numOfRows=100&address=" + "" + "&dongName=" + "";
                    //URLEncoder.encode(intent_keyword, "UTF-8");
            params.add(0, url);
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

