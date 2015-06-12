package sc.music.ui.activity;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import sc.droid.dmc.R;
//import android.support.v7.app.AppCompatActivity; //for setSupportActionBar/*
// 我的父类是PreferenceActivity*/
/**
 * Created by Administrator on 2015/6/11.
 */
public class SettingsActivity  extends PreferenceActivity {
    private Toolbar mActionBar;//v7的

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActionBar.setTitle(/*getTitle()*/R.string.SettingsActivity_title);
        //setSupportActionBar(mActionBar);
       // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void setContentView(int layoutResID) {
       // super.setContentView(layoutResID);
        /*
		//viewgroup可以包含其他view，LayoutInflater的作用是把xml变成一个对应的view对象。
		from是从一个context生成一个LayoutInflater
		inflate是具体把xml变为view
*/
        //这是新建一个LinearLayout线性布局，加入父布局中么？
        ViewGroup contentView = (ViewGroup) LayoutInflater.from(this).inflate(
                R.layout.preference_activity_layout, new LinearLayout(this), false);
        mActionBar=(Toolbar)contentView.findViewById(R.id.preference_activity_toolbar);
        mActionBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("mActionBar", "setNavigationOnClickListener");
                finish();
            }
        });

        ViewGroup contentWrapper = (ViewGroup) contentView.findViewById(R.id.content_wrapper);
        LayoutInflater.from(this).inflate(layoutResID, contentWrapper, true);
        getWindow().setContentView(contentView);
    }
}
