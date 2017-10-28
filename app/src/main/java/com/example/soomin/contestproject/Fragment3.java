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
        Cursor cursor = db.rawQuery("select * from medical_record order by date", null);

        while (cursor.moveToNext()) {

            RecordItemVO vo = new RecordItemVO();
            vo._id = cursor.getInt(0);
            vo.title = cursor.getString(1);
            vo.date = cursor.getString(4);
            vo.dong_name = cursor.getString(5);
            vo.type = cursor.getString(8);
            mGroupList.add(vo);

            vo.content = cursor.getString(2);
            vo.name = cursor.getString(3);
            vo.dong_tel = cursor.getString(6);
            vo.dong_address = cursor.getString(7);
            mChildListContent.add(vo);
            mChildList.add(mChildListContent);
        }
        db.close();
        adapter = new RecordListAdapter(this.getContext(), R.layout.record_list_item, R.layout.record_list_item, mGroupList, mChildList);
        listView.setAdapter(adapter);
        listView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return false;
            }

        });
    }

}
