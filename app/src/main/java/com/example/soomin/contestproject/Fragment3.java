package com.example.soomin.contestproject;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
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
    //ArrayList<RecordItemVO> mGroupList;
    //ArrayList<ArrayList<RecordItemVO>> mChildList;
    //ArrayList<RecordItemVO> mChildListContent;
    ArrayList<RecordItemVO> datas;
    ListView listView;
    FloatingActionButton fab;
    RecordListAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        datas = new ArrayList<>();
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment3, container, false);
        listView = (ListView) viewGroup.findViewById(R.id.record_list);
       fab = (FloatingActionButton) viewGroup.findViewById(R.id.add_btn);
       fab.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.iconmonstr_plus_5_240));
        fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.blue)));

       // mGroupList = new ArrayList<>();
      //  mChildList = new ArrayList<ArrayList<RecordItemVO>>();
      //  mChildListContent = new ArrayList<RecordItemVO>();
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
        //mGroupList.clear();
        //mChildList.clear();
        //mChildListContent.clear();
        datas.clear();
        Cursor cursor = db.rawQuery("select * from medical_record mr, animal an where mr.name=an._id", null);


        while (cursor.moveToNext()) {
            RecordItemVO vo = new RecordItemVO();
            vo._id = cursor.getInt(0);
            vo.title = cursor.getString(1);
            String date_str = cursor.getString(4);
            if (date_str != null && date_str.length() != 0 )
                  vo.date = date_str.substring(2);
            vo.type = cursor.getString(8);
          //  mGroupList.add(vo);
          //   mChildListContent= new ArrayList<>();

        //    RecordItemVO vo2 = new RecordItemVO();
            vo.content = cursor.getString(2);
            vo.dong_name = cursor.getString(5);
            vo.dong_tel = cursor.getString(6);
            vo.dong_address = cursor.getString(7);
            vo.type = cursor.getString(8);
            vo.name = cursor.getString(10);
            //mChildListContent.add(vo2);
           // mChildList.add(mChildListContent);
            datas.add(vo);
        }
        db.close();
        adapter = new RecordListAdapter(this.getContext(), R.layout.record_list_item, datas);
        listView.setAdapter(adapter);

    }

}
