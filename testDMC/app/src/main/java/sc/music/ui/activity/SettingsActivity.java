package sc.music.ui.activity;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.v4.app.NavUtils;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListAdapter;

import java.util.ArrayList;
import java.util.List;

import sc.droid.dmc.R;
import sc.music.ui.adapter.MyPrefsHeaderAdapter;
//import android.support.v7.app.AppCompatActivity; //for setSupportActionBar/*
// 我的父类是PreferenceActivity*/
/**
 * Created by Administrator on 2015/6/11.
 */
public class SettingsActivity  extends PreferenceActivity {
    private Toolbar mActionBar;//v7的

    private static final String TAG = "SettingsActivity";
    //这个Header是PreferenceActivity才有的
    private List<Header> mHeaders;


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
       		/*
		* 所以这个参数的作用就是，是否把选取的视图加入到root中。false 的意思就是不添加到root中。可能需要我们手动添加。
		* */
        //true是自动加入这个视图到root中
        LayoutInflater.from(this).inflate(layoutResID, contentWrapper, true);
        getWindow().setContentView(contentView);
    }
    @Override
    protected void onResume()
    {
        super.onResume();

        if (getListAdapter() instanceof MyPrefsHeaderAdapter)
            ((MyPrefsHeaderAdapter) getListAdapter()).resume();
    }

    @Override
    protected void onPause()
    {
        super.onPause();

        if (getListAdapter() instanceof MyPrefsHeaderAdapter)
            ((MyPrefsHeaderAdapter) getListAdapter()).pause();
    }

    @Override
    protected boolean isValidFragment (String fragmentName)
    {
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button  有这个键么？
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override  //俩header的布局在这里加载
    public void onBuildHeaders(List<PreferenceActivity.Header> target)
    {
        loadHeadersFromResource(R.xml.preferences, target);
        mHeaders = target;
    }
    public void setListAdapter(ListAdapter adapter)
    {
        if (mHeaders == null) {
            mHeaders = new ArrayList<>();//这是一个列表？
            for (int i = 0; i < adapter.getCount(); ++i)
                mHeaders.add((Header) adapter.getItem(i));//从adatper里头区2数据，放在mHeaders这个列表中。
        }
        super.setListAdapter(new MyPrefsHeaderAdapter(this, mHeaders));
    }

}
