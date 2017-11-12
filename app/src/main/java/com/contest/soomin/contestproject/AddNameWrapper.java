package com.contest.soomin.contestproject;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by SOOMIN on 2017-10-30.
 */

public class AddNameWrapper {
    public TextView nameView;
    public ImageView deleteBtn;

    public AddNameWrapper(View root) {
        nameView = (TextView) root.findViewById(R.id.name);
        deleteBtn = (ImageView) root.findViewById(R.id.delete_btn);

    }
}
