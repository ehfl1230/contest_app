package com.contest.soomin.contestproject;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by SOOMIN on 2017-10-26.
 */

public class BookmarkAdapter extends ArrayAdapter<RecordItemVO> {
    Context context;
    ArrayList<RecordItemVO> datas;
    int resId;
    int selectedRadioPosition = -1;
    View lastSelectedRadioButton;

    public BookmarkAdapter(Context context, int resId, ArrayList<RecordItemVO> datas) {
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
        TextView addDongType = wrapper.addDongType;
        final RecordItemVO vo = datas.get(position);
        addDongaddressView.setText(vo.dong_address);
        addDongtelView.setText(vo.dong_tel);
        addDongNameView.setText(vo.dong_name);
        addDongType.setText(vo.type);
        if (getContext().getClass() == MainActivity.class) {
            convertView.setBackgroundResource(0);
            ViewCompat.setBackground(convertView, null);
            ViewCompat.setBackground(convertView, ContextCompat.getDrawable(getContext(), R.drawable.round_border));
        } else{
            System.out.println(getContext().getClass());
            if (selectedRadioPosition == position) {

                convertView.setBackgroundResource(0);
                ViewCompat.setBackground(convertView, null);
                ViewCompat.setBackground(convertView, ContextCompat.getDrawable(getContext(), R.drawable.selected_round_border));
            } else {
                convertView.setBackgroundResource(0);
                ViewCompat.setBackground(convertView, null);
                ViewCompat.setBackground(convertView, ContextCompat.getDrawable(getContext(), R.drawable.round_border));

            }
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (selectedRadioPosition == position) {
                        return;
                    }
                    selectedRadioPosition = position;
                    if (lastSelectedRadioButton != null) {
                        lastSelectedRadioButton.setBackgroundResource(0);
                        ViewCompat.setBackground(lastSelectedRadioButton, null);
                        ViewCompat.setBackground(lastSelectedRadioButton, ContextCompat.getDrawable(getContext(), R.drawable.round_border));

                    }
                    lastSelectedRadioButton = (View) v;
                    notifyDataSetChanged();
                }
            });
        }
        return convertView;
    }
}


