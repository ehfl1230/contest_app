package com.example.soomin.contestproject;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by SOOMIN on 2017-10-24.
 */

public class Fragment3 extends Fragment{
    ArrayList<RecordItemVO> datas;
    ListView listView;
    FloatingActionButton fab;
    RecordListAdapter adapter;

    @Override
    public void onResume() {
        super.onResume();
        DBHelper helper = new DBHelper(getContext());
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from medical_record order by date", null);

        datas = new ArrayList<>();
        while (cursor.moveToNext()) {
            RecordItemVO vo = new RecordItemVO();
            vo._id = cursor.getInt(0);
            vo.title = cursor.getString(1);
            vo.content = cursor.getString(2);
            vo.name = cursor.getString(3);
            vo.date = cursor.getString(4);
            vo.dong_name = cursor.getString(5);
            vo.dong_tel = cursor.getString(6);
            vo.dong_address = cursor.getString(7);
            vo.type = cursor.getString(8);
            datas.add(vo);
        }
        adapter = new RecordListAdapter(this.getContext(), R.layout.record_list_item, datas);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String item_id = Integer.toString(datas.get(position)._id);
                Intent intent = new Intent(Fragment3.super.getActivity(), RecordItemActivity.class);
                intent.putExtra("item_id", item_id);
                startActivity(intent);
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment3, container, false);
        listView = (ListView) viewGroup.findViewById(R.id.record_list);
        fab = (FloatingActionButton) viewGroup.findViewById(R.id.add_btn);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "test", Toast.LENGTH_SHORT);
                Intent intent = new Intent(Fragment3.super.getActivity(), AddRecordActivity.class);
                startActivity(intent);
                adapter.notifyDataSetChanged();
            }
        });
        //searchBtn = (ImageView) findViewById(R.id.search_bnt);
        //searchField = (EditText) findViewById(R.id.search_blank);

        return viewGroup;
    }
}
