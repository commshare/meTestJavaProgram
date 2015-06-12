package com.qingchenglei.toolbardemo;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import com.qingchenglei.toolbardemo.adapter.MyFragmentAdapter;
import com.qingchenglei.toolbardemo.util.Tools;
import com.qingchenglei.toolbardemo.view.SlidingTabLayout;

/**
 * Created by Zech on 2015/5/3.
 * <p/>
 * Tabs whit google iosched 2014
 */
public class TabActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private LinearLayout toolbarContainer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);

        Tools.setStatusBar(this, R.color.colorPrimary);

        toolbarContainer = (LinearLayout) findViewById(R.id.tool_bar_container);

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ViewPager vp = (ViewPager) findViewById(R.id.vp);
        vp.setAdapter(new MyFragmentAdapter(this,getSupportFragmentManager()));

        SlidingTabLayout tab = (SlidingTabLayout) findViewById(R.id.tabs);
        tab.setDistributeEvenly(true);

        //custom tab view
//        tab.setCustomTabView(R.layout.tab_view,R.id.tv_tab);

        tab.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        tab.setSelectedIndicatorColors(getResources().getColor(R.color.colorAccent));
        tab.setViewPager(vp);
    }

}
