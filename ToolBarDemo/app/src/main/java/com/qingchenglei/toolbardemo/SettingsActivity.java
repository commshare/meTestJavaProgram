package com.qingchenglei.toolbardemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.qingchenglei.toolbardemo.util.Tools;

/**
 * Created by Zech on 2015/5/3.
 * <p/>
 * setting
 */
public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //activity的布局
        setContentView(R.layout.activity_settings);
        //这个是？
        Tools.setStatusBar(this,R.color.colorPrimary);

        //从布局中得到Toobar
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
