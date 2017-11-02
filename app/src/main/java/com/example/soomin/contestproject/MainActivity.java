package com.example.soomin.contestproject;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    Fragment1 fragment1;
    Fragment2 fragment2;
    Fragment3 fragment3;
    Fragment4 fragment4;
    Fragment5 fragment5;
    MyApplication myApplication;
    TabLayout tabs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ActionBar actionBar = getSupportActionBar();
        //actionBar.setDisplayShowTitleEnabled(false);

        fragment1 = new Fragment1();
        fragment2 = new Fragment2();
        fragment3 = new Fragment3();
        fragment4 = new Fragment4();
        fragment5 = new Fragment5();


       tabs= (TabLayout) findViewById(R.id.tabs);
        tabs.addTab(tabs.newTab().setText("동물병원"));
        tabs.addTab(tabs.newTab().setText("동물약국"));
        tabs.addTab(tabs.newTab().setText("내페이지"));
        tabs.addTab(tabs.newTab().setText("즐겨찾기"));
        tabs.addTab(tabs.newTab().setText("길찾기"));
        setupTabIcons();
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment1).commit();
        tabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();

                Fragment fragment = null;

                if (position == 0) {
                    fragment = fragment1;
                } else if (position == 1) {
                    fragment = fragment2;
                } else if (position == 2) {
                    fragment = fragment3;
                }else if (position == 3) {
                    fragment = fragment4;
                }
                else if (position == 4) {
                    fragment = fragment5;
                }
                android.app.FragmentManager fm = getFragmentManager();
                fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

                getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        myApplication = (MyApplication) getApplicationContext();

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            myApplication.callPermission = true;
        }

        if (!myApplication.callPermission) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 10);
        }

    }  private void setupTabIcons() {
        tabs.getTabAt(0).setIcon(R.drawable.ic_add_black_24dp);
        tabs.getTabAt(1).setIcon(R.drawable.ic_add_black_24dp);
        tabs.getTabAt(2).setIcon(R.drawable.ic_add_black_24dp);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 10 && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                myApplication.callPermission = true;
            }
        }
        if (requestCode == 20 && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                myApplication.locationPermission = true;
            }
        }
    }


}
