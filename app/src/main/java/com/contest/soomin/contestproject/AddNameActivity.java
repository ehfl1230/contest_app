package com.contest.soomin.contestproject;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.tsengvn.typekit.TypekitContextWrapper;

import java.util.ArrayList;

public class AddNameActivity extends AppCompatActivity implements View.OnClickListener {
    ListView listView;
    EditText new_name;
    ImageView add_name;
    ArrayList<NameVO> datas;
    AddNameAdapter adapter;
    RelativeLayout no_data;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(ContextCompat.getDrawable(this, R.color.blue));
        actionBar.setTitle("");
        actionBar.setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_add_name);
        no_data = (RelativeLayout) findViewById(R.id.no_data);
        listView = (ListView) findViewById(R.id.name_list);
        add_name = (ImageView) findViewById(R.id.add_name);
        new_name = (EditText) findViewById(R.id.new_name);
        new_name.clearFocus();
        downKeyboard(AddNameActivity.this, new_name);
        add_name.setOnClickListener(this);


    }
    public static void downKeyboard(Context context, EditText editText) {
        InputMethodManager mInputMethodManager = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        mInputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //    NavUtils.navigateUpFromSameTask(this);

                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
        if (datas.size() == 0) {
            no_data.setVisibility(View.VISIBLE);
        } else {
            no_data.setVisibility(View.GONE);
        }
        adapter = new AddNameAdapter(this, R.layout.add_name_item, datas);
        listView.setAdapter(adapter);
        new_name.clearFocus();
        downKeyboard(AddNameActivity.this, new_name);
    }

    public void onClick(View v) {

        if (v == add_name) {
            new_name.clearFocus();
            downKeyboard(AddNameActivity.this, new_name);
            String new_name_str = new_name.getText().toString();
            if (new_name_str == null || new_name_str.equals("")) {
                Toast.makeText(this, "이름을 입력해주세요.", Toast.LENGTH_SHORT).show();
            } else {
                DBHelper helper = new DBHelper(this);
                SQLiteDatabase db = helper.getWritableDatabase();
                Cursor cursor = db.rawQuery("select * from animal where animal_name=?", new String[]{new_name_str });
                if (cursor.getCount() > 0) {
                    Toast.makeText(this, "이미 입력된 이름입니다. 다른 이름을 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    db.execSQL("insert into animal (animal_name) values (?)", new String[]{new_name_str});
                    Toast.makeText(this, "저장되었습니다.", Toast.LENGTH_SHORT).show();
                    new_name.setText("");
                }
                db.close();
                adapter.notifyDataSetChanged();
                onResume();
            }

        }
    }
}
