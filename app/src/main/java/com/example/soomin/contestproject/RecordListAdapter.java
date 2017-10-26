package com.example.soomin.contestproject;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by SOOMIN on 2017-10-24.
 */

public class RecordListAdapter extends ArrayAdapter<RecordItemVO> {
    Context context;
    ArrayList<RecordItemVO> datas;
    int resId;
    ImageView modifyBtnView = null;


    public RecordListAdapter(Context context, int resId, ArrayList<RecordItemVO> datas) {
        super(context, resId);
        this.context = context;
        this.datas = datas;
        this.resId = resId;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(resId, null);

            RecordListWrapper wrapper = new RecordListWrapper(convertView);
            convertView.setTag(wrapper);
        }

        RecordListWrapper wrapper = (RecordListWrapper) convertView.getTag();
        TextView dateView = wrapper.dateView;
        TextView titleView = wrapper.titleView;
        modifyBtnView = wrapper.modifyBtnView;
        final RecordItemVO vo=datas.get(position);
        titleView.setText(vo.title);
        dateView.setText(vo.date);
        SetOnClick(modifyBtnView, vo._id);
        return convertView;
    }
    public void SetOnClick(final ImageView modifyBtnView, final int id) {
        modifyBtnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ModifyItemActivity.class);
                intent.putExtra("item_id", Integer.toString(id));
                context.startActivity(intent);
            }
        });
    }
}
