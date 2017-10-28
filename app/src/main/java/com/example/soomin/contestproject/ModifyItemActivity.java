package com.example.soomin.contestproject;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.StringTokenizer;

public class ModifyItemActivity extends AppCompatActivity implements View.OnClickListener {
    EditText modify_title;
    EditText modify_contents;
    TextView origin_date;
    TextView modify_date;
    TextView saveBtn;
    TextView deleteBtn;
    String item_id;
    int mYear, mMonth, mDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_item);


        modify_title = (EditText) findViewById(R.id.modify_title);
        modify_contents = (EditText) findViewById(R.id.modify_contents);
        origin_date = (TextView) findViewById(R.id.origin_date);
        modify_date = (TextView) findViewById(R.id.modify_date);
        saveBtn = (TextView) findViewById(R.id.save_btn);
        deleteBtn = (TextView) findViewById(R.id.delete_btn);
        modify_date.setOnClickListener(this);
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
        vo.dong_name = cursor.getString(5);
        vo.dong_tel = cursor.getString(6);
        vo.dong_address = cursor.getString(7);
        vo.type = cursor.getString(8);
        db.close();
        modify_title.setText(vo.title);
        modify_contents.setText(vo.content);
        origin_date.setText(vo.date);
    }

    @Override
    public void onClick(View v) {


        if (v == deleteBtn) {
            AlertDialog.Builder alert_confirm = new AlertDialog.Builder(ModifyItemActivity.this);
            alert_confirm.setMessage("삭제하겠습니까?").setCancelable(false).setPositiveButton("삭제하기",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            DBHelper helper = new DBHelper(ModifyItemActivity.this);
                            SQLiteDatabase db = helper.getWritableDatabase();
                            db.execSQL("delete from medical_record where _id=?",
                                    new String[]{item_id});
                            db.close();
                            finish();
                        }
                    }).setNegativeButton("취소",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            AlertDialog alert = alert_confirm.create();
            alert.show();

        }
        if (v == saveBtn) {
            AlertDialog.Builder alert_confirm = new AlertDialog.Builder(ModifyItemActivity.this);
            if (modify_date.equals("") || modify_title.equals("")) {
                alert_confirm.setMessage("데이터를 입력해주세요.").setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();     //닫기
                    }
                });

            } else {
                alert_confirm.setMessage("저장하겠습니까?").setCancelable(false).setPositiveButton("저장하기",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DBHelper helper = new DBHelper(ModifyItemActivity.this);
                                SQLiteDatabase db = helper.getWritableDatabase();
                                db.execSQL("update medical_record set title=?, memo=?, date=?, dong_name=?, dong_tel=?, type=?" +
                                                "where _id=?",
                                        new String[]{modify_title.getText().toString(), modify_contents.getText().toString(), origin_date.getText().toString(), "", "", "", item_id});
                                db.close();
                                finish();
                            }
                        }).setNegativeButton("취소",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
            }
            AlertDialog alert = alert_confirm.create();
            alert.show();

        }

        if (v == modify_date) {
            String date = origin_date.getText().toString();
            if (date.equals("")) {
                Calendar cal = new GregorianCalendar();
                mYear = cal.get(Calendar.YEAR);
                mMonth = cal.get(Calendar.MONTH);
                mDay = cal.get(Calendar.DAY_OF_MONTH);
            } else {
                String[] dates = date.split("/");

                mYear = Integer.parseInt(dates[0]);
                mMonth = Integer.parseInt(dates[1]);
                mDay = Integer.parseInt(dates[2]);
                mMonth -= 1;
            }
            System.out.println(mYear + " " + mMonth + " " + mDay);
            DatePickerDialog dpd = new DatePickerDialog(this, mDateSetListener, mYear,
                    mMonth, mDay);
            dpd.show();
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
                    origin_date.setText(add_date_str);

                }
            };
}