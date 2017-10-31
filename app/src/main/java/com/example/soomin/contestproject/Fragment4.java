package com.example.soomin.contestproject;

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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by SOOMIN on 2017-10-31.
 */

public class Fragment4 extends Fragment {
    ListView listView;
    ArrayList<RecordItemVO> datas;
    BookmarkAdapter adapter;
    TextView save_btn;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.activity_bookmark, container, false);

        DBHelper helper = new DBHelper(getContext());
        SQLiteDatabase db = helper.getWritableDatabase();
        datas = new ArrayList<>();
        listView = (ListView) viewGroup.findViewById(R.id.search_list);
        save_btn = (TextView) viewGroup.findViewById(R.id.save_btn);
        save_btn.setVisibility(View.GONE);
        Cursor cursor = db.rawQuery("select * from bookmark", null);

        while (cursor.moveToNext()) {
            RecordItemVO vo = new RecordItemVO();
            vo._id = cursor.getInt(0);
            vo.dong_name = cursor.getString(1);
            vo.dong_address = cursor.getString(2);
            vo.dong_tel = cursor.getString(3);
            vo.type = cursor.getString(4);
            datas.add(vo);
        }
        adapter = new BookmarkAdapter(getContext(), R.layout.add_dong_item, datas);
        listView.setAdapter(adapter);
        return viewGroup;
    }
}