package com.example.soomin.contestproject;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by SOOMIN on 2017-10-24.
 */

public class ListAdapter extends ArrayAdapter<ItemVO> {
    Context context;
    ArrayList<ItemVO> datas;
    int resId;

    public ListAdapter(Context context, int resId, ArrayList<ItemVO> datas) {
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
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(resId, null);

            ListWrapper wrapper = new ListWrapper(convertView);
            convertView.setTag(wrapper);
        }

        ListWrapper wrapper = (ListWrapper) convertView.getTag();
        TextView dongNameView = wrapper.dongNameView;
        TextView telView = wrapper.telView;
        TextView addressView = wrapper.addressView;
        final ImageView callBtnView = wrapper.callBtnView;
        final ItemVO vo = datas.get(position);
        dongNameView.setText(vo.apiDongName);
        telView.setText(vo.apiTel);
        addressView.setText(vo.apiNewAddress);
        callBtnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alert_confirm = new AlertDialog.Builder(getContext());
                alert_confirm.setMessage("통화를 시도하겠습니까?").setCancelable(false).setPositiveButton("전화 걸기",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent();
                                intent.setAction(Intent.ACTION_CALL);
                                String tel = "";

                                    tel = vo.apiTel;
                                    if (tel.equals("")) {
                                        Toast t = Toast.makeText(getContext(), R.string.no_tel, Toast.LENGTH_SHORT);
                                        t.show();
                                    }

                                intent.setData(Uri.parse("tel:" + tel));
                                getContext().startActivity(intent);
                            }
                        }).setNegativeButton("취소",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                AlertDialog alert = alert_confirm.create();
                alert.show();
            }
        });
        return convertView;
        }
    }
