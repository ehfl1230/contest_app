package com.example.soomin.contestproject;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
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

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class AddRecordActivity extends AppCompatActivity {

    EditText title;
    EditText content;
    TextView addRecordName;
    TextView addRecordTel;
    TextView addRecordAddress;
    TextView date;
    TextView addDateBtn;
    TextView addDongBtn;
    ImageView saveBtn;
    int mYear, mMonth, mDay;
    String type = "";
    private static final int msg = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Calendar cal = new GregorianCalendar();
        mYear = cal.get(Calendar.YEAR);
        mMonth = cal.get(Calendar.MONTH);
        mDay = cal.get(Calendar.DAY_OF_MONTH);

        setContentView(R.layout.activity_add_record);
        title = (EditText) findViewById(R.id.title);
        content = (EditText) findViewById(R.id.contents);
        date = (TextView) findViewById(R.id.date);
        addDateBtn = (TextView) findViewById(R.id.add_date_btn);
        addDongBtn = (TextView) findViewById(R.id.add_dong);
        saveBtn = (ImageView) findViewById(R.id.save_btn);
        addRecordName = (TextView)findViewById(R.id.add_record_name);
        addRecordTel = (TextView)findViewById(R.id.add_record_tel);
        addRecordAddress = (TextView)findViewById(R.id.add_record_address);
        addDongBtn.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v) {
                                              Intent intent = new Intent();
                                              intent.setClass(AddRecordActivity.this, AddDongActivity.class);
                                              startActivityForResult(intent, msg);
                                          }
                                      }
        );
        addDateBtn.setOnClickListener(new View.OnClickListener() {
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
                String date_str = "";
                if (date.getText().toString().equals("날짜선택")) {
                    date_str = "";
                } else {
                    date_str = date.getText().toString();
                }
                db.execSQL("insert into medical_record (title, memo, date, dong_name, dong_tel, dong_address, type) values (?,?,?,?,?,?, ?)",
                        new String[]{title.getText().toString(), content.getText().toString(), date_str,
                                addRecordName.getText().toString(), addRecordTel.getText().toString(),
                                addRecordAddress.getText().toString(), type});

                db.close();
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            type = data.getStringExtra("type");
            String name = data.getStringExtra("name");
            String tel = data.getStringExtra("tel");
            String address = data.getStringExtra("address");
            addRecordName.setText(name);
            addRecordTel.setText(tel);
            addRecordAddress.setText(address);
        }
        if (requestCode == RESULT_CANCELED) {

        }
    }

    DatePickerDialog.OnDateSetListener mDateSetListener =
            new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    // TODO Auto-generated method stub
                    mYear = year;
                    mMonth = monthOfYear;
                    mDay = dayOfMonth;
                    String add_date_str = String.format("%d/%d/%d", mYear, mMonth + 1, mDay);
                    date.setText(add_date_str);
                }
            };
}
