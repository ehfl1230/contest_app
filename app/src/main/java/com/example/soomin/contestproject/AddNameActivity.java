package com.example.soomin.contestproject;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class AddNameActivity extends AppCompatActivity implements View.OnClickListener {
    ListView listView;
    EditText new_name;
    ImageView add_name;
    ArrayList<NameVO> datas;
    AddNameAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_name);
        listView = (ListView) findViewById(R.id.name_list);
        add_name = (ImageView) findViewById(R.id.add_name);
        new_name = (EditText) findViewById(R.id.new_name);
        add_name.setOnClickListener(this);
        new_name.clearFocus();

    }

    @Override
    protected void onResume() {
        super.onResume();
        DBHelper helper = new DBHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from animal order by animal_name", null);
        datas = new ArrayList<>();
        while (cursor.moveToNext()) {
            NameVO vo = new NameVO();
            vo._id = cursor.getInt(0);
            vo.name = cursor.getString(1);
            datas.add(vo);
        }
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        adapter = new AddNameAdapter(this, R.layout.add_name_item, datas);
        listView.setAdapter(adapter);
    }

    public void onClick(View v) {

        if (v == add_name) {
            String new_name_str = new_name.getText().toString();
            if (new_name_str == null || new_name_str.equals("")) {
                Toast.makeText(this, "이름을 입력해주세요.", Toast.LENGTH_SHORT).show();
            } else {
                DBHelper helper = new DBHelper(this);
                SQLiteDatabase db = helper.getWritableDatabase();
                Cursor cursor = db.rawQuery("select * from animal order by animal_name", null);
                if (cursor.getCount() > 0) {
                    Toast.makeText(this, "이미 입력된 이름입니다. 다른 이름을 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    db.execSQL("insert into animal (animal_name) values (?)", new String[]{new_name_str});
                    Toast.makeText(this, "저장되었습니다.", Toast.LENGTH_SHORT).show();
                }
                db.close();
                adapter.notifyDataSetChanged();
                onResume();
            }

        }
    }
}
