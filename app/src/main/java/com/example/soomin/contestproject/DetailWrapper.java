package com.example.soomin.contestproject;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by SOOMIN on 2017-10-24.
 */

public class DetailWrapper {
    public TextView dongNameView;
    public TextView telView;
    public TextView newAddressView;
    public TextView oldAddressView;
    public ImageView callBtnView;

    public DetailWrapper(View root) {
        dongNameView = (TextView) root.findViewById(R.id.dong_name_text);
        telView = (TextView) root.findViewById(R.id.tel_text);
        newAddressView = (TextView) root.findViewById(R.id.new_address_text);
        oldAddressView = (TextView) root.findViewById(R.id.old_address_text);
        callBtnView = (ImageView) root.findViewById(R.id.call_btn);
    }
}
