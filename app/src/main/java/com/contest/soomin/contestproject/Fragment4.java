package com.contest.soomin.contestproject;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by SOOMIN on 2017-10-31.
 */

public class Fragment4 extends Fragment {
    ListView listView;
    ArrayList<ItemVO> datas;
    ListAdapter adapter;
    TextView save_btn;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.activity_bookmark, container, false);


        datas = new ArrayList<>();
        listView = (ListView) viewGroup.findViewById(R.id.search_list);
        save_btn = (TextView) viewGroup.findViewById(R.id.save_btn);
        save_btn.setVisibility(View.GONE);

        return viewGroup;
    }

    @Override
    public void onResume() {
        super.onResume();
        DBHelper helper = new DBHelper(getContext());
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from bookmark", null);
    datas.clear();
        while (cursor.moveToNext()) {
            ItemVO vo = new ItemVO();
            //vo. = cursor.getInt(0);
            vo.apiDongName = cursor.getString(1);
            vo.apiNewAddress= cursor.getString(2);
            vo.apiTel = cursor.getString(3);
            vo.type = cursor.getString(4);
            datas.add(vo);
        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String name = datas.get(position).apiDongName;
                String address = datas.get(position).apiNewAddress;
                Intent intent = new Intent(Fragment4.super.getActivity(), FragmentItem.class);
                intent.putExtra("type", datas.get(position).type);
                intent.putExtra("name", datas.get(position).apiDongName);
                intent.putExtra("address", datas.get(position).apiNewAddress);

                startActivity(intent);
            }
        });
        adapter = new ListAdapter(getContext(), R.layout.list_item, datas);
        listView.setAdapter(adapter);
    }
}