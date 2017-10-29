package com.example.soomin.contestproject;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by SOOMIN on 2017-10-24.
 */

public class Fragment3 extends Fragment{
    ArrayList<RecordItemVO> mGroupList;
    ArrayList<ArrayList<RecordItemVO>> mChildList;
    ArrayList<RecordItemVO> mChildListContent;
    ExpandableListView listView;
    FloatingActionButton fab;
    RecordListAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment3, container, false);
        listView = (ExpandableListView) viewGroup.findViewById(R.id.record_list);
        fab = (FloatingActionButton) viewGroup.findViewById(R.id.add_btn);
        fab.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.iconmonstr_plus_5_64));
        mGroupList = new ArrayList<>();
        mChildList = new ArrayList<ArrayList<RecordItemVO>>();
        mChildListContent = new ArrayList<RecordItemVO>();
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

    @Override
    public void onResume() {
        super.onResume();
        DBHelper helper = new DBHelper(getContext());
        SQLiteDatabase db = helper.getWritableDatabase();
        mGroupList.clear();
        mChildList.clear();
        mChildListContent.clear();
        Cursor cursor = db.rawQuery("select * from medical_record order by date", null);

        while (cursor.moveToNext()) {

            RecordItemVO vo = new RecordItemVO();
            vo._id = cursor.getInt(0);
            vo.title = cursor.getString(1);
            String date_str = cursor.getString(4);
            vo.date = date_str.substring(2);
            vo.type = cursor.getString(8);
            mGroupList.add(vo);
             mChildListContent= new ArrayList<>();

            RecordItemVO vo2 = new RecordItemVO();
            vo2.content = cursor.getString(2);
            vo2.dong_name = cursor.getString(5);
            vo2.dong_tel = cursor.getString(6);
            vo2.dong_address = cursor.getString(7);
            vo2.type = cursor.getString(8);
            mChildListContent.add(vo2);
            mChildList.add(mChildListContent);
        }
        db.close();
        adapter = new RecordListAdapter(this.getContext(), R.layout.record_list_item, R.layout.record_list_item2, mGroupList, mChildList);
        listView.setAdapter(adapter);
        listView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return false;
            }

        });
    }

}
