package com.example.soomin.contestproject;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by SOOMIN on 2017-10-05.
 */

public class RecordListWrapper {
    public TextView titleView;
    public TextView dateView;
    public ImageView modifyBtnView;

    public RecordListWrapper(View root) {
        titleView = (TextView) root.findViewById(R.id.title_text);
        dateView = (TextView) root.findViewById(R.id.date_text);
        modifyBtnView = (ImageView) root.findViewById(R.id.modify_btn);

    }
}
