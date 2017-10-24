package com.example.soomin.contestproject;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class AddRecordActivity extends AppCompatActivity {

    EditText title;
    EditText content;
    TextView date;
    ImageView saveBtn;
    int mYear, mMonth, mDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_record);
        title = (EditText) findViewById(R.id.title);
        content = (EditText) findViewById(R.id.contents);
        date = (TextView) findViewById(R.id.date);
        saveBtn = (ImageView) findViewById(R.id.save_btn);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dpd = new DatePickerDialog(AddRecordActivity.this, mDateSetListener, mYear,
                        mMonth, mDay);
                dpd.show();

            }
        });
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBHelper helper = new DBHelper(AddRecordActivity.this);
                SQLiteDatabase db = helper.getWritableDatabase();
                String result = "";
                db.execSQL("insert into medical_record (title, memo, date) values (?,?,?)",
                        new String[]{title.getText().toString(), "test", date.getText().toString()});

                db.close();
                finish();
            }
        });
    }

    DatePickerDialog.OnDateSetListener mDateSetListener =
            new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    // TODO Auto-generated method stub
                    mYear = year;
                    mMonth = monthOfYear;
                    mDay = dayOfMonth;
                    String add_date_str = String.format("%d/%d/%d", mYear, mMonth+1, mDay);
                    date.setText(add_date_str);
                }
            };
}
