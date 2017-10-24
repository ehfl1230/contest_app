package com.example.soomin.contestproject;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by SOOMIN on 2017-10-05.
 */

public class ListWrapper {
    public TextView dongNameView;
    public TextView telView;
    public TextView addressView;
    public ImageView callBtnView;

    public ListWrapper(View root) {
        dongNameView = (TextView) root.findViewById(R.id.dong_name_text);
        telView = (TextView) root.findViewById(R.id.tel_text);
        addressView = (TextView) root.findViewById(R.id.address_text);
        callBtnView = (ImageView) root.findViewById(R.id.call_btn);
    }
}
