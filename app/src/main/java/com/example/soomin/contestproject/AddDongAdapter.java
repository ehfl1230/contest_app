package com.example.soomin.contestproject;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by SOOMIN on 2017-10-26.
 */

public class AddDongAdapter extends ArrayAdapter<ItemVO> {
    Context context;
    ArrayList<ItemVO> datas;
    int resId;
    int selectedRadioPosition = 0;
    RadioButton lastSelectedRadioButton;

    public AddDongAdapter(Context context, int resId, ArrayList<ItemVO> datas) {
        super(context, resId);
        this.context = context;
        this.datas = datas;
        this.resId = resId;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    public int getSelectedRadioPosition() {return selectedRadioPosition;}

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(resId, null);

            AddDongWrapper wrapper = new AddDongWrapper(convertView);
            convertView.setTag(wrapper);
        }

        AddDongWrapper wrapper = (AddDongWrapper) convertView.getTag();
        TextView addDongaddressView = wrapper.addDongaddressView;
        TextView addDongtelView = wrapper.addDongtelView;
        TextView addDongNameView = wrapper.addDongNameView;
        RadioButton addDongRadioBtn = wrapper.addDongRadioBtn;
        final ItemVO vo = datas.get(position);
        addDongaddressView.setText(vo.apiNewAddress);
        addDongtelView.setText(vo.apiTel);
        addDongNameView.setText(vo.apiDongName);
        if (selectedRadioPosition == position) {
            addDongRadioBtn.setChecked(true);

        } else {
            addDongRadioBtn.setChecked(false);
        }
        addDongRadioBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (selectedRadioPosition == position) {
                    return;
                }
                selectedRadioPosition = position;

                if (lastSelectedRadioButton != null) {
                    lastSelectedRadioButton.setChecked(false);
                }
                lastSelectedRadioButton = (RadioButton)v;
                notifyDataSetChanged();
            }
        });
        return convertView;
    }
}


