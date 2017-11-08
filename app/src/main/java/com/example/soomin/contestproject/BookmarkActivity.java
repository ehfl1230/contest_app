package com.example.soomin.contestproject;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URLEncoder;
import java.util.ArrayList;

public class BookmarkActivity extends AppCompatActivity implements View.OnClickListener {
    ListView listView;
    ArrayList<RecordItemVO> datas;
    BookmarkAdapter adapter;
    TextView save_btn;
    RelativeLayout no_data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(ContextCompat.getDrawable(this, R.color.blue));
        actionBar.setTitle("");
        actionBar.setDisplayHomeAsUpEnabled(true);
        DBHelper helper = new DBHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();
        datas = new ArrayList<>();
        listView = (ListView) findViewById(R.id.search_list);
        save_btn = (TextView) findViewById(R.id.save_btn);
        no_data = (RelativeLayout) findViewById(R.id.no_data2);
        save_btn.setOnClickListener(this);
        Cursor cursor = db.rawQuery("select * from bookmark", null);

        while (cursor.moveToNext()) {
            RecordItemVO vo = new RecordItemVO();
            vo._id = cursor.getInt(0);
            vo.dong_name = cursor.getString(1);
            vo.dong_address = cursor.getString(2);
            vo.dong_tel = cursor.getString(3);
            vo.type = cursor.getString(4);
            vo.dong_old_address = cursor.getString(5);
            vo.dong_lat = cursor.getString(6);
            vo.dong_lng = cursor.getString(7);
            datas.add(vo);
        }
          listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        if (datas.size() == 0) {
            no_data.setVisibility(View.VISIBLE);
        } else {
            no_data.setVisibility(View.GONE);
        }
        adapter = new BookmarkAdapter(this, R.layout.add_dong_item, datas);
        listView.setAdapter(adapter);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                // NavUtils.navigateUpFromSameTask(this);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }@Override
    public void onClick(View v) {

        if (v == save_btn) {
            if(adapter.getSelectedRadioPosition() == -1){
                Toast.makeText(this, "의료기관을 선택해주세요.", Toast.LENGTH_SHORT).show();
            } else {
            RecordItemVO item = datas.get(adapter.getSelectedRadioPosition());

                Intent intent = new Intent();
                intent.putExtra("name", item.dong_name);
                intent.putExtra("tel", item.dong_tel);
                intent.putExtra("address", item.dong_address);
                intent.putExtra("type", item.type);
                intent.putExtra("old_address", item.dong_old_address);
                intent.putExtra("lat", item.dong_lat);
                intent.putExtra("lng", item.dong_lng);
                setResult(RESULT_OK, intent);
                finish();
            }
        }
    }
}
