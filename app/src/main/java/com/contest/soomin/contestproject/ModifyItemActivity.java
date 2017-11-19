package com.contest.soomin.contestproject;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.tsengvn.typekit.TypekitContextWrapper;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
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
    TextView modifyDongBtn;
    TextView origin_dong;
    TextView addDongBtn;
    TextView moveSearch;
    TextView manageNameBtn;
    Spinner spinnerName;
    RelativeLayout rl;
    String item_id;
    String name;
    int mYear, mMonth, mDay;
    String type = "";
    private static final int msg = 1;
    String tel = "";
    String address = "";
    RecordItemVO vo;
    InputMethodManager imm;
    String old_address = "";
    String lat = "";
    String lng = "";
    int selected = -1;
    final ArrayList<String> spinner_Name = new ArrayList<>();

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_item);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(ContextCompat.getDrawable(this, R.color.blue));
        actionBar.setTitle("");
        actionBar.setDisplayHomeAsUpEnabled(true);

        modify_title = (EditText) findViewById(R.id.modify_title);
        modify_contents = (EditText) findViewById(R.id.modify_contents);
        rl = (RelativeLayout) findViewById(R.id.relative_layout);
        rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(modify_title.getWindowToken(), 0);
            }
        });
        origin_date = (TextView) findViewById(R.id.origin_date);
        modify_date = (TextView) findViewById(R.id.modify_date);
        saveBtn = (TextView) findViewById(R.id.save_btn);
        deleteBtn = (TextView) findViewById(R.id.delete_btn);
        addDongBtn = (TextView) findViewById(R.id.move_search);
        moveSearch = (TextView) findViewById(R.id.move_bookmark);
        spinnerName = (Spinner) findViewById(R.id.spinner_name);
        downKeyboard(this, modify_title);
        modify_title.clearFocus();
        downKeyboard(this, modify_contents);
        modify_contents.clearFocus();
       // modifyDongBtn = (TextView) findViewById(R.id.add_dong);
        manageNameBtn = (TextView) findViewById(R.id.manage_name);
        origin_dong = (TextView) findViewById(R.id.add_record_name);
        modify_date.setOnClickListener(this);
//        modifyDongBtn.setOnClickListener(this);
        saveBtn.setOnClickListener(this);
        deleteBtn.setOnClickListener(this);
        manageNameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ModifyItemActivity.this, AddNameActivity.class);
                startActivity(intent);
            }
        });
        moveSearch.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v) {
                                              Intent intent = new Intent();
                                              intent.setClass(ModifyItemActivity.this, BookmarkActivity.class);
                                              startActivityForResult(intent, msg);
                                          }
                                      }
        );
        addDongBtn.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v) {
                                              Intent intent = new Intent();
                                              intent.setClass(ModifyItemActivity.this, AddDongActivity.class);
                                              startActivityForResult(intent, msg);
                                          }
                                      }
        );
        item_id = getIntent().getExtras().getString("item_id");
        DBHelper helper = new DBHelper(ModifyItemActivity.this);
        SQLiteDatabase db = helper.getWritableDatabase();

        Cursor cursor = db.rawQuery("select * from medical_record mr, animal an where mr.name=an._id and mr._id=?",
                new String[]{item_id});
        cursor.moveToNext();
        vo  = new RecordItemVO();
        vo._id = cursor.getInt(0);
        vo.title = cursor.getString(1);
        vo.content = cursor.getString(2);
        vo.name = cursor.getString(3);
        vo.date = cursor.getString(4);
        vo.dong_name = cursor.getString(5);
        vo.dong_tel = cursor.getString(6);
        vo.dong_address = cursor.getString(7);
        vo.type = cursor.getString(8);
        vo.dong_old_address = cursor.getString(9);
        vo.dong_lat= cursor.getString(10);
        vo.dong_lng = cursor.getString(11);
        vo.name = cursor.getString(13);

        modify_title.setText(vo.title);
        modify_contents.setText(vo.content);
        origin_date.setText(vo.date);
        origin_dong.setText(vo.dong_name);

    db.close();
    }


    @Override
    protected void onResume() {
        super.onResume();
        DBHelper helper = new DBHelper(ModifyItemActivity.this);
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor2 = db.rawQuery("select * from animal order by animal_name", null);
        spinner_Name.clear();
        while (cursor2.moveToNext()) {
            NameVO nvo = new NameVO();
            nvo._id = cursor2.getInt(0);
            nvo.name = cursor2.getString(1);
            spinner_Name.add(nvo.name);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.custom_simple_drop_item, spinner_Name);
        spinnerName.setAdapter(adapter);
        if (selected == -1) {
            for (int i = 0; i < spinner_Name.size(); i++) {
                if (spinner_Name.get(i).equals(vo.name)) {
                    selected = i;
                }
            }
        }
        db.close();
        spinnerName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id)
            {
                selected = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0)
            {

            }
        });
        if (spinner_Name.size() == 1|| spinner_Name.size() <= selected) {
            spinnerName.setSelection(-1);
        }else {
            spinnerName.setSelection(selected);
        }
    }
    public static void downKeyboard(Context context, EditText editText) {
        InputMethodManager mInputMethodManager = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        mInputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }
    @Override
    public void onBackPressed() {
        AlertDialog.Builder alert_confirm = new AlertDialog.Builder(ModifyItemActivity.this);

        alert_confirm.setMessage("수정사항을 저장하겠습니까?").setCancelable(false).setPositiveButton("저장하기",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DBHelper helper = new DBHelper(ModifyItemActivity.this);
                        SQLiteDatabase db = helper.getWritableDatabase();
                        Cursor cursor = db.rawQuery("select * from animal where animal_name=?", new String[]{spinnerName.getSelectedItem().toString()});
                        NameVO nvo = new NameVO();
                        while (cursor.moveToNext()) {
                            nvo._id = cursor.getInt(0);
                            nvo.name = cursor.getString(1);
                        }
                        if (type == null || type.equals("")) {
                            db.execSQL("update medical_record set title=?, memo=?, date=?, dong_tel=?, dong_address=?, name=?, dong_old_address=?, dong_lat=?, dong_lng=?" +
                                            "where _id=?",
                                    new String[]{modify_title.getText().toString(), modify_contents.getText().toString(), origin_date.getText().toString(), tel, address, Integer.toString(nvo._id),
                                            old_address, lat, lng, item_id});

                        }else {
                            db.execSQL("update medical_record set title=?, memo=?, date=?, dong_name=?, dong_tel=?, dong_address=?, type=?, name=?, dong_old_address=?, dong_lat=?, dong_lng=?" +
                                            "where _id=?",
                                    new String[]{modify_title.getText().toString(), modify_contents.getText().toString(), origin_date.getText().toString(), origin_dong.getText().toString(), tel, address, type, Integer.toString(nvo._id),
                                            old_address, lat, lng, item_id});

                        }
                        db.close();
                        finish();
                    }
                }).setNegativeButton("저장하지 않고 종료",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
        AlertDialog alert = alert_confirm.create();
        alert.show();
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
            if (modify_title.getText().toString().equals("")) {
                alert_confirm.setMessage("증상을 입력해주세요.").setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();     //닫기
                    }
                });
            } else if (origin_dong.getText() == null || origin_dong.getText().toString().equals("의료기관선택") || origin_dong.getText().toString().equals("")) {
                alert_confirm.setMessage("의료기관을 선택해주세요.").setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();     //닫기
                    }
                });
            }else if (spinnerName.getSelectedItem() == null || spinnerName.getSelectedItem().toString().equals("")) {
                alert_confirm.setMessage("반려동물이름을 선택해주세요.").setPositiveButton("확인", new DialogInterface.OnClickListener() {
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
                                Cursor cursor = db.rawQuery("select * from animal where animal_name=?", new String[]{spinnerName.getSelectedItem().toString()});
                                NameVO nvo = new NameVO();
                                while (cursor.moveToNext()) {
                                    nvo._id = cursor.getInt(0);
                                    nvo.name = cursor.getString(1);
                                }
                                if (type == null || type.equals("")) {
                                    db.execSQL("update medical_record set title=?, memo=?, date=?, dong_tel=?, dong_address=?, name=?, dong_old_address=?, dong_lat=?, dong_lng=?" +
                                                    "where _id=?",
                                            new String[]{modify_title.getText().toString(), modify_contents.getText().toString(), origin_date.getText().toString(), tel, address, Integer.toString(nvo._id),
                                                    old_address, lat, lng, item_id});

                                }else {
                                    db.execSQL("update medical_record set title=?, memo=?, date=?, dong_name=?, dong_tel=?, dong_address=?, type=?, name=?, dong_old_address=?, dong_lat=?, dong_lng=?" +
                                                    "where _id=?",
                                            new String[]{modify_title.getText().toString(), modify_contents.getText().toString(), origin_date.getText().toString(), origin_dong.getText().toString(), tel, address, type, Integer.toString(nvo._id),
                                                    old_address, lat, lng, item_id});

                                }
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

        if (v == modifyDongBtn) {
            Intent intent = new Intent();
            intent.setClass(this, AddDongActivity.class);
            startActivityForResult(intent, msg);
        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            type = data.getStringExtra("type");
            name = data.getStringExtra("name");
            tel = data.getStringExtra("tel");
            address = data.getStringExtra("address");
            old_address = data.getStringExtra("old_address");
            lng = data.getStringExtra("lng");
            lat = data.getStringExtra("lat");
            origin_dong.setText(name);
            //    addRecordTel.setText(tel);
            //    addRecordAddress.setText(address);
        }
        if (requestCode == RESULT_CANCELED) {

        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                AlertDialog.Builder alert_confirm = new AlertDialog.Builder(ModifyItemActivity.this);

                alert_confirm.setMessage("수정사항을 저장하겠습니까?").setCancelable(false).setPositiveButton("저장하기",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DBHelper helper = new DBHelper(ModifyItemActivity.this);
                                SQLiteDatabase db = helper.getWritableDatabase();
                                Cursor cursor = db.rawQuery("select * from animal where animal_name=?", new String[]{spinnerName.getSelectedItem().toString()});
                                NameVO nvo = new NameVO();
                                while (cursor.moveToNext()) {
                                    nvo._id = cursor.getInt(0);
                                    nvo.name = cursor.getString(1);
                                }
                                if (type == null || type.equals("")) {
                                    db.execSQL("update medical_record set title=?, memo=?, date=?, dong_tel=?, dong_address=?, name=?, dong_old_address=?, dong_lat=?, dong_lng=?" +
                                                    "where _id=?",
                                            new String[]{modify_title.getText().toString(), modify_contents.getText().toString(), origin_date.getText().toString(), tel, address, Integer.toString(nvo._id),
                                                    old_address, lat, lng, item_id});

                                }else {
                                    db.execSQL("update medical_record set title=?, memo=?, date=?, dong_name=?, dong_tel=?, dong_address=?, type=?, name=?, dong_old_address=?, dong_lat=?, dong_lng=?" +
                                                    "where _id=?",
                                            new String[]{modify_title.getText().toString(), modify_contents.getText().toString(), origin_date.getText().toString(), origin_dong.getText().toString(), tel, address, type, Integer.toString(nvo._id),
                                                    old_address, lat, lng, item_id});

                                }
                                db.close();
                                finish();
                            }
                        }).setNegativeButton("저장하지 않고 종료",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });
                AlertDialog alert = alert_confirm.create();
                alert.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
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