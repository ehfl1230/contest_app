package com.example.soomin.contestproject;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class AddRecordActivity extends AppCompatActivity {

    EditText title;
    EditText content;
    TextView addRecordName;
    TextView addRecordTel;
    TextView addRecordAddress;
    TextView moveSearch;
    TextView moveBookmark;
    TextView date;
    TextView addDateBtn;
    TextView addDongBtn;
    TextView saveBtn;
    TextView manageNameBtn;
    Spinner nameSpinner;
    LinearLayout rl;
    InputMethodManager imm;
    int mYear, mMonth, mDay;
    String type = "";
    private static final int msg = 1;
    String tel = "";
    String address = "";
    String name = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Calendar cal = new GregorianCalendar();
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(ContextCompat.getDrawable(this, R.color.blue));
        actionBar.setTitle("");
        actionBar.setDisplayHomeAsUpEnabled(true);
        mYear = cal.get(Calendar.YEAR);
        mMonth = cal.get(Calendar.MONTH);
        mDay = cal.get(Calendar.DAY_OF_MONTH);

        setContentView(R.layout.activity_add_record);

        title = (EditText) findViewById(R.id.title);
        content = (EditText) findViewById(R.id.contents);
        date = (TextView) findViewById(R.id.date);
        rl = (LinearLayout) findViewById(R.id.relative_layout);
        rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(title.getWindowToken(), 0);
               // imm.hideSoftInputFromWindow(content.getWindowToken(), 0);
            }
        });
        addDongBtn = (TextView) findViewById(R.id.move_search);
        moveSearch = (TextView) findViewById(R.id.move_bookmark);
        nameSpinner = (Spinner) findViewById(R.id.spinner_name);

        String add_init_date = String.format("%d/%d/%d", mYear, mMonth + 1, mDay);
        date.setText(add_init_date);
        addDateBtn = (TextView) findViewById(R.id.add_date_btn);
        manageNameBtn = (TextView) findViewById(R.id.manage_name);
        //addDongBtn = (TextView) findViewById(R.id.add_dong);
        saveBtn = (TextView) findViewById(R.id.save_btn);
        addRecordName = (TextView) findViewById(R.id.add_record_name);
        // addRecordTel = (TextView)findViewById(R.id.add_record_tel);
        // addRecordAddress = (TextView)findViewById(R.id.add_record_address);
        moveSearch.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v) {
                                              Intent intent = new Intent();
                                              intent.setClass(AddRecordActivity.this, BookmarkActivity.class);
                                              startActivityForResult(intent, msg);
                                          }
                                      }
        );
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
                String title_str = title.getText().toString();
                AlertDialog.Builder alert_confirm = new AlertDialog.Builder(AddRecordActivity.this);

                if (title_str.equals("")) {
                    alert_confirm.setMessage("주요정보를 입력해주세요.").setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();     //닫기
                        }
                    });
                } else if (name.equals("의료기관선택") || name.equals("")|| name == null) {
                    alert_confirm.setMessage("의료기관을 선택해주세요.").setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();     //닫기
                        }
                    });
                }else if (nameSpinner.getSelectedItem() == null || nameSpinner.getSelectedItem().toString().equals("")) {
                    alert_confirm.setMessage("반려동물이름을 선택해주세요.").setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();     //닫기
                        }
                    });
                }
                    else
                 {
                    title_str = title.getText().toString();
                    String animal_name = nameSpinner.getSelectedItem().toString();
                    Cursor cursor = db.rawQuery("select * from animal where animal_name=?", new String[]{animal_name});
                    NameVO vo = new NameVO();
                    while (cursor.moveToNext()) {
                        vo._id = cursor.getInt(0);
                        vo.name = cursor.getString(1);
                    }
                    db.execSQL("insert into medical_record (title, memo, date, dong_name, dong_tel, dong_address, type, name) values (?,?,?,?,?,?,?,?)",
                            new String[]{title_str, content.getText().toString(), date.getText().toString(), name, tel, address, type, Integer.toString(vo._id)});

                    db.close();
                    alert_confirm.setMessage("저장되었습니다.").setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();     //닫기
                            finish();
                        }
                    });
                }
                AlertDialog alert = alert_confirm.create();
                alert.show();
            }
        });
        manageNameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddRecordActivity.this, AddNameActivity.class);
                startActivity(intent);
            }
        });
        title.clearFocus();
        content.clearFocus();
        downKeyboard(this, title);
        downKeyboard(this, content);
        }

    @Override
    protected void onResume() {
        super.onResume();
        final ArrayList<String> spinner_Name = new ArrayList<>();
        DBHelper helper = new DBHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from animal order by animal_name", null);
        while (cursor.moveToNext()) {
            NameVO vo = new NameVO();
            vo._id = cursor.getInt(0);
            vo.name = cursor.getString(1);
            spinner_Name.add(vo.name);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.custom_simple_drop_item, spinner_Name);
        nameSpinner.setAdapter(adapter);
    }

    public static void downKeyboard(Context context, EditText editText) {
        InputMethodManager mInputMethodManager = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        mInputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }
    @Override
        public boolean onOptionsItemSelected (MenuItem item){
            switch (item.getItemId()) {
                case android.R.id.home:
                    //NavUtils.navigateUpFromSameTask(this);
                    finish();
                    return true;
                default:
                    return super.onOptionsItemSelected(item);
            }
        }

        @Override
        protected void onActivityResult ( int requestCode, int resultCode, Intent data){
            super.onActivityResult(requestCode, resultCode, data);
            if (resultCode == RESULT_OK) {
                type = data.getStringExtra("type");
                name = data.getStringExtra("name");
                tel = data.getStringExtra("tel");
                address = data.getStringExtra("address");
                addRecordName.setText(name);
                System.out.println("ddfjsdflksjdklfjksldjflksdf" + type);
                //    addRecordTel.setText(tel);
                //    addRecordAddress.setText(address);
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
