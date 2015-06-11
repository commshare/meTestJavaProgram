package com.qingchenglei.toolbardemo;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.qingchenglei.toolbardemo.adapter.MyFragmentAdapter;
import com.qingchenglei.toolbardemo.util.Tools;

import it.neokree.materialtabs.MaterialTab;
import it.neokree.materialtabs.MaterialTabHost;
import it.neokree.materialtabs.MaterialTabListener;

/**
 * Created by Zech on 2015/5/6.
 * <p/>
 * Tabs with MaterialTabs library
 */
public class TabLibraryActivity extends AppCompatActivity implements MaterialTabListener{

    private MaterialTabHost tabHost;
    private ViewPager vp;
    private MyFragmentAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_lib);

        Tools.setStatusBar(this, R.color.colorPrimary);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tabHost = (MaterialTabHost) findViewById(R.id.material_tab);
        vp = (ViewPager) this.findViewById(R.id.vp_lib);

        // init view pager
        pagerAdapter = new MyFragmentAdapter(this,getSupportFragmentManager());
        vp.setAdapter(pagerAdapter);
        vp.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // when user do a swipe the selected tab change
                tabHost.setSelectedNavigationItem(position);
            }
        });

        // insert all tabs from pagerAdapter data
        for (int i = 0; i < pagerAdapter.getCount(); i++) {
            tabHost.addTab(
                    tabHost.newTab()
                            .setText(pagerAdapter.getPageTitle(i))
                            .setTabListener(this)
            );
        }

    }


    @Override
    public void onTabSelected(MaterialTab materialTab) {
        // when the tab is clicked the pager swipe content to the tab position
        vp.setCurrentItem(materialTab.getPosition());
    }

    @Override
    public void onTabReselected(MaterialTab materialTab) {

    }

    @Override
    public void onTabUnselected(MaterialTab materialTab) {

    }
}
