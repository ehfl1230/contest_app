package com.contest.soomin.contestproject;

import android.app.Application;

import com.tsengvn.typekit.Typekit;

/**
 * Created by SOOMIN on 2017-10-19.
 */

public class MyApplication extends Application {
    public boolean callPermission;
    public boolean locationPermission;
    @Override
    public void onCreate() {
        super.onCreate();
        Typekit.getInstance()
                .addBold(Typekit.createFromAsset(this, "fonts/NanumBarunpenB.ttf"))
                        .addNormal(Typekit.createFromAsset(this, "fonts/NanumBarunpenR.ttf"));
    }
}
