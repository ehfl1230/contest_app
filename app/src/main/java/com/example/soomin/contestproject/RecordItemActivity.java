package com.example.soomin.contestproject;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class RecordItemActivity extends AppCompatActivity implements View.OnClickListener {

    TextView item_dong_name;
    TextView item_dong_tel;
    TextView item_title;
    TextView item_contents;
    TextView item_date;
    ImageView item_call_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_item);


        item_dong_name = (TextView) findViewById(R.id.item_dong_name);
        item_dong_tel = (TextView) findViewById(R.id.item_dong_tel);
        item_title = (TextView) findViewById(R.id.item_title);
        item_date = (TextView) findViewById(R.id.item_date);
        item_contents = (TextView) findViewById(R.id.item_contents);
        item_call_btn = (ImageView) findViewById(R.id.item_call_btn);
        item_call_btn.setOnClickListener(this);
        String item_id = getIntent().getExtras().getString("item_id");
        DBHelper helper = new DBHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from medical_record where _id=?", new String[]{item_id});
        cursor.moveToNext();
        item_title.setText(cursor.getString(1));
        item_contents.setText(cursor.getString(2));
        item_date.setText(cursor.getString(4));
        item_dong_name.setText(cursor.getString(5));
        item_dong_tel.setText(cursor.getString(6));
        String type = cursor.getString(7);
        db.close();
    }

    @Override
    public void onClick(View v) {

    }
}
