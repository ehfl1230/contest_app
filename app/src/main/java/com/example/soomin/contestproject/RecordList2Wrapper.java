package com.example.soomin.contestproject;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by SOOMIN on 2017-10-05.
 */

public class RecordList2Wrapper {
    public TextView itemDongNameView;
   // public TextView itemDongTelView;
   // public ImageView itemCallBtn;
    public TextView itemContentsView;
    public TextView itemTypeView;
    public TextView itemAnimalNameView;

    public RecordList2Wrapper(View root) {
        itemDongNameView = (TextView) root.findViewById(R.id.item_dong_name);
     //   itemDongTelView = (TextView) root.findViewById(R.id.item_dong_tel);
      //  itemCallBtn = (ImageView) root.findViewById(R.id.item_call_btn);
        itemContentsView = (TextView) root.findViewById(R.id.item_contents);
        itemTypeView =  (TextView) root.findViewById(R.id.item_type);
        itemAnimalNameView = (TextView) root.findViewById(R.id.aninmal_name);
    }
}
