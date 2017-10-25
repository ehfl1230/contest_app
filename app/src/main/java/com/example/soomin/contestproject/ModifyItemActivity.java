package com.example.soomin.contestproject;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

public class ModifyItemActivity extends AppCompatActivity implements View.OnClickListener{
    EditText modify_title;
    EditText modify_contents;
    ImageView saveBtn;
    ImageView deleteBtn;
    String item_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_item);
        modify_title = (EditText) findViewById(R.id.modify_title);
        modify_contents = (EditText) findViewById(R.id.modify_contents);
        saveBtn = (ImageView) findViewById(R.id.save_btn);
        deleteBtn = (ImageView) findViewById(R.id.delete_btn);
        saveBtn.setOnClickListener(this);
        deleteBtn.setOnClickListener(this);
        item_id = getIntent().getExtras().getString("item_id");
        DBHelper helper = new DBHelper(ModifyItemActivity.this);
        SQLiteDatabase db = helper.getWritableDatabase();

        Cursor cursor = db.rawQuery("select * from medical_record where _id=?",
                new String[]{item_id});
        cursor.moveToNext();
        RecordItemVO vo = new RecordItemVO();
        vo._id = cursor.getInt(0);
        vo.title = cursor.getString(1);
        vo.content = cursor.getString(2);
        vo.name = cursor.getString(3);
        vo.date = cursor.getString(4);
        vo.type_id = cursor.getString(5);
        vo.type = cursor.getString(6);
        db.close();
        modify_title.setText(vo.title);
        modify_contents.setText(vo.content);
    }

    @Override
    public void onClick(View v) {
        DBHelper helper = new DBHelper(ModifyItemActivity.this);
        SQLiteDatabase db = helper.getWritableDatabase();

        if (v == deleteBtn){

            db.execSQL("delete from medical_record where _id=?",
                    new String[]{item_id});

        }
        if (v == saveBtn) {
            db.execSQL("update medical_record set title=?, memo=?, date=?, type_id=?, type=?" +
                            "where _id=?",
                    new String[]{modify_title.getText().toString(), modify_contents.getText().toString(), "","", "", item_id});
        } db.close();
    }
}