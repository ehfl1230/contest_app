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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by SOOMIN on 2017-10-24.
 */

public class Fragment3 extends Fragment {
    //ArrayList<RecordItemVO> mGroupList;
    //ArrayList<ArrayList<RecordItemVO>> mChildList;
    //ArrayList<RecordItemVO> mChildListContent;
    ArrayList<RecordItemVO> datas;
    ArrayList<ItemVO>  datas2;
    ListView listView;
    FloatingActionButton fab;
    RecordListAdapter adapter;
    ListAdapter adapter2;
    TextView record;
    TextView bookmark;
    int type = 1;
    RelativeLayout no_data;
    RelativeLayout no_data2;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        datas = new ArrayList<>();
        datas2 = new ArrayList<>();
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment3, container, false);
        listView = (ListView) viewGroup.findViewById(R.id.record_list);
        record = (TextView) viewGroup.findViewById(R.id.record);
        bookmark = (TextView) viewGroup.findViewById(R.id.bookmark);
        fab = (FloatingActionButton) viewGroup.findViewById(R.id.add_btn);
        fab.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_add_black_24dp));
        fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.blue)));
        no_data = (RelativeLayout) viewGroup.findViewById(R.id.no_data);
        no_data.setVisibility(View.GONE);
        no_data2 = (RelativeLayout) viewGroup.findViewById(R.id.no_data2);
        no_data2.setVisibility(View.GONE);

        // mGroupList = new ArrayList<>();
        //  mChildList = new ArrayList<ArrayList<RecordItemVO>>();
        //  mChildListContent = new ArrayList<RecordItemVO>();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Fragment3.super.getActivity(), AddRecordActivity.class);
                startActivity(intent);
                adapter.notifyDataSetChanged();
            }
        });
        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                type = 1;
                onResume();
            }
        });

        bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                type = 2;
                onResume();
            }
        });
        //searchBtn = (ImageView) findViewById(R.id.search_bnt);
        //searchField = (EditText) findViewById(R.id.search_blank);

        return viewGroup;
    }

    @Override
    public void onResume() {
        super.onResume();
        no_data.setVisibility(View.GONE);
        no_data2.setVisibility(View.GONE);

        DBHelper helper = new DBHelper(getContext());
        SQLiteDatabase db = helper.getWritableDatabase();
        //mGroupList.clear();
        //mChildList.clear();
        //mChildListContent.clear();

        if (type == 1) {
            datas.clear();
            Cursor cursor = db.rawQuery("select * from medical_record mr, animal an where mr.name=an._id", null);
            while (cursor.moveToNext()) {
                RecordItemVO vo = new RecordItemVO();
                vo._id = cursor.getInt(0);
                vo.title = cursor.getString(1);
                String date_str = cursor.getString(4);
                if (date_str != null && date_str.length() != 0)
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
                vo.dong_old_address = cursor.getString(9);
                vo.dong_lat = cursor.getString(10);
                vo.dong_lng = cursor.getString(11);
                vo.name = cursor.getString(13);
                //mChildListContent.add(vo2);
                // mChildList.add(mChildListContent);
                datas.add(vo);
            }
            adapter = new RecordListAdapter(this.getContext(), R.layout.record_list_item, datas);
            listView.setAdapter(adapter);
            if (type == 1 && datas.size() == 0) {
                no_data.setVisibility(View.VISIBLE);
                no_data2.setVisibility(View.GONE);
            } else {
                no_data.setVisibility(View.GONE);
                no_data2.setVisibility(View.GONE);
            }
        } else if (type ==2) {
            Cursor cursor = db.rawQuery("select * from bookmark", null);
            datas2.clear();
            while (cursor.moveToNext()) {
                ItemVO vo = new ItemVO();
                //vo. = cursor.getInt(0);
                vo.apiDongName = cursor.getString(1);
                vo.apiNewAddress= cursor.getString(2);
                vo.apiTel = cursor.getString(3);
                vo.type = cursor.getString(4);
                vo.apiOldAddress = cursor.getString(5);
                vo.apiLat = cursor.getString(6);
                vo.apiLng = cursor.getString(7);
                datas2.add(vo);
            }

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    String name = datas2.get(position).apiDongName;
                    String address = datas2.get(position).apiNewAddress;
                    String old_address = datas2.get(position).apiOldAddress;
                    String tel = datas2.get(position).apiTel;
                    String lat = datas2.get(position).apiLat;
                    String lng = datas2.get(position).apiLng;
                    Intent intent = new Intent(Fragment3.super.getActivity(), FragmentItem.class);
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
            adapter2 = new ListAdapter(getContext(), R.layout.list_item, datas2);
            listView.setAdapter(adapter2);
            if (type == 2 && datas2.size() == 0) {
                no_data.setVisibility(View.GONE);
                no_data2.setVisibility(View.VISIBLE);
            } else {
                no_data.setVisibility(View.GONE);
                no_data2.setVisibility(View.GONE);
            }
        }
        db.close();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        }
}
