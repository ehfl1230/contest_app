package com.example.soomin.contestproject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.ToolbarWidgetWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by SOOMIN on 2017-10-26.
 */

public class AddNameAdapter extends ArrayAdapter<NameVO> {
    Context context;
    ArrayList<NameVO> datas;
    int resId;

    public AddNameAdapter(Context context, int resId, ArrayList<NameVO> datas) {
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
    public View getView(final int position, @Nullable View convertView, @NonNull final ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(resId, null);

            AddNameWrapper wrapper = new AddNameWrapper(convertView);
            convertView.setTag(wrapper);
        }

        AddNameWrapper wrapper = (AddNameWrapper) convertView.getTag();
        TextView nameView = wrapper.nameView;
        ImageView deleteBtn = wrapper.deleteBtn;
        final NameVO vo = datas.get(position);
        nameView.setText(vo.name);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBHelper helper = new DBHelper(getContext());
                SQLiteDatabase db = helper.getWritableDatabase();
                db.execSQL("delete from animal where _id=?", new String[]{Integer.toString(vo._id)});
                db.close();
                Toast.makeText(getContext(), "삭제되었습니다.", Toast.LENGTH_SHORT).show();
                notifyDataSetChanged();
            }
        });
        return convertView;
    }
}


