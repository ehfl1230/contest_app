package com.example.soomin.contestproject;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by SOOMIN on 2017-10-24.
 */

public class RecordListAdapter extends ArrayAdapter<RecordItemVO> {
    Context context;
//    ArrayList<RecordItemVO> groupList;
 //   ArrayList<ArrayList<RecordItemVO>> childList;
    ArrayList<RecordItemVO> datas;
    int resId;
    int resId2;
    ImageView modifyBtnView = null;


    public RecordListAdapter(Context context, int resId, ArrayList<RecordItemVO> datas) {
        super(context, resId, datas);
        this.context = context;
        //this.groupList = groupList;
        //this.childList = childList;
        this.datas = datas;
        this.resId = resId;
        //this.resId2 = resId2;
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

            RecordListWrapper wrapper = new RecordListWrapper(convertView);
            convertView.setTag(wrapper);
        }

        RecordListWrapper wrapper = (RecordListWrapper) convertView.getTag();
        TextView dateView = wrapper.dateView;
        TextView titleView = wrapper.titleView;
        modifyBtnView = wrapper.modifyBtnView;
        TextView itemContentsView = wrapper.itemContentsView;
        TextView itemDongNameView = wrapper.itemDongNameView;
        ImageView itemTypeView = wrapper.itemTypeView;
        TextView itemAnimalNameView = wrapper.itemAnimalNameView;
        ImageView homeBtn = wrapper.homebtn;
        final RecordItemVO vo=datas.get(position);
        itemDongNameView.setText(vo.dong_name);
        itemContentsView.setText(vo.content);
        itemAnimalNameView.setText(vo.name);
        titleView.setText(vo.title);
        dateView.setText(vo.date);
        if (vo.type ==null) {
            vo.type = "hospital";
            itemTypeView.setImageDrawable(ContextCompat.getDrawable(this.context, R.drawable.ic_local_hospital_black_24dp));
            itemTypeView.setColorFilter(ContextCompat.getColor(this.context, R.color.colorPrimary), android.graphics.PorterDuff.Mode.SRC_IN);
        }
        if (vo.type.equals("hospital")) {
            //itemTypeView.setImageDrawable(ContextCompat.getDrawable(this.context, R.drawable.ic_local_hospital_black_24dp));
                    //.setText("병원
            itemTypeView.setImageDrawable(ContextCompat.getDrawable(this.context, R.drawable.ic_local_hospital_black_24dp));
            itemTypeView.setColorFilter(ContextCompat.getColor(this.context, R.color.colorPrimary), android.graphics.PorterDuff.Mode.SRC_IN);
           // itemTypeView.setBackgroundDrawable(ContextCompat.getDrawable(this.context, R.drawable.round_border_with_blue));
        } else if (vo.type.equals("drugstore")){
           //itemTypeView.setText("약국");
           // itemTypeView.setBackgroundDrawable(ContextCompat.getDrawable(this.context, R.drawable.round_border_with_red));
            itemTypeView.setImageDrawable(ContextCompat.getDrawable(this.context, R.drawable.ic_local_pharmacy_black_24dp));
            itemTypeView.setColorFilter(ContextCompat.getColor(this.context, R.color.colorAccent), android.graphics.PorterDuff.Mode.SRC_IN);
        }
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = datas.get(position).dong_name;
                String address = datas.get(position).dong_address;
                String old_address = datas.get(position).dong_old_address;
                String tel = datas.get(position).dong_tel;
                String lat = datas.get(position).dong_lat;
                String lng = datas.get(position).dong_lng;
                Intent intent = new Intent(RecordListAdapter.super.getContext(), FragmentItem.class);
                intent.putExtra("type", "hospital");
                intent.putExtra("name", name);
                intent.putExtra("address", address);
                intent.putExtra("old_address", old_address);
                intent.putExtra("tel", tel);
                intent.putExtra("lat", lat);
                intent.putExtra("lng", lng);

                getContext().startActivity(intent);
            }
        });
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
/*
    @Override
    public RecordItemVO getGroup(int groupPosition) {
        return groupList.get(groupPosition);
    }
    @Override
    public int getGroupCount() {
        return groupList.size();
    }
    // 그룹 ID를 반환한다.
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }



    @Override
    public RecordItemVO getChild(int groupPosition, int childPosition) {
        return childList.get(groupPosition).get(childPosition);
    }

    // 차일드뷰 사이즈를 반환한다.
    @Override
    public int getChildrenCount(int groupPosition) {
        return childList.get(groupPosition).size();
    }

    // 차일드뷰 ID를 반환한다.
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }
    @NonNull
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {

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
        final RecordItemVO vo=groupList.get(groupPosition);
        titleView.setText(vo.title);
        dateView.setText(vo.date);
        SetOnClick(modifyBtnView, vo._id);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(resId2, null);

            RecordList2Wrapper wrapper = new RecordList2Wrapper(convertView);
            convertView.setTag(wrapper);
        }

        RecordList2Wrapper wrapper = (RecordList2Wrapper) convertView.getTag();
        TextView itemContentsView = wrapper.itemContentsView;
        TextView itemDongNameView = wrapper.itemDongNameView;
        TextView itemTypeView = wrapper.itemTypeView;
        TextView itemAnimalNameView = wrapper.itemAnimalNameView;
   //     ImageView itemCallBtn = wrapper.itemCallBtn;
  //      TextView itemDongTelView = wrapper.itemDongTelView;
        final RecordItemVO vo=childList.get(groupPosition).get(childPosition);
        itemDongNameView.setText(vo.dong_name);
        itemContentsView.setText(vo.content);
        itemAnimalNameView.setText(vo.name);
//        itemDongTelView.setText(vo.dong_tel);
//        itemContentsView.setText(vo.content);
        System.out.println("ddf" + vo.type);
        if (vo.type ==null) {
            vo.type = "hospital";
        }
        if (vo.type.equals("hospital")) {
            itemTypeView.setText("병원");
            itemTypeView.setBackgroundDrawable(ContextCompat.getDrawable(this.context, R.drawable.round_border_with_blue));
        } else if (vo.type.equals("drugstore")){
            itemTypeView.setText("약국");
            itemTypeView.setBackgroundDrawable(ContextCompat.getDrawable(this.context, R.drawable.round_border_with_red));

        }
        return convertView;
    }

    @Override
    public boolean hasStableIds() { return true; }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) { return true; }

*/

}
