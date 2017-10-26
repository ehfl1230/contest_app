package com.example.soomin.contestproject;

import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

/**
 * Created by SOOMIN on 2017-10-26.
 */

public class AddDongWrapper {
    public TextView addDongNameView;
    public TextView addDongtelView;
    public TextView addDongaddressView;
    public RadioButton addDongRadioBtn;

    public AddDongWrapper(View root) {
        addDongNameView = (TextView) root.findViewById(R.id.add_dong_name_text);
        addDongtelView = (TextView) root.findViewById(R.id.add_dong_tel_text);
        addDongaddressView = (TextView) root.findViewById(R.id.add_dong_address_text);
        addDongRadioBtn = (RadioButton) root.findViewById(R.id.add_dong_radio_btn);
    }
}