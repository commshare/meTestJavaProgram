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
// �ҵĸ�����PreferenceActivity*/
/**
 * Created by Administrator on 2015/6/11.
 */
public class SettingsActivity  extends PreferenceActivity {
    private Toolbar mActionBar;//v7��

    private static final String TAG = "SettingsActivity";
    //���Header��PreferenceActivity���е�
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
		//viewgroup���԰�������view��LayoutInflater�������ǰ�xml���һ����Ӧ��view����
		from�Ǵ�һ��context����һ��LayoutInflater
		inflate�Ǿ����xml��Ϊview
*/
        //�����½�һ��LinearLayout���Բ��֣����븸������ô��
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
		* ����������������þ��ǣ��Ƿ��ѡȡ����ͼ���뵽root�С�false ����˼���ǲ���ӵ�root�С�������Ҫ�����ֶ���ӡ�
		* */
        //true���Զ����������ͼ��root��
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
            // Respond to the action bar's Up/Home button  �������ô��
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override  //��header�Ĳ������������
    public void onBuildHeaders(List<PreferenceActivity.Header> target)
    {
        loadHeadersFromResource(R.xml.preferences, target);
        mHeaders = target;
    }
    public void setListAdapter(ListAdapter adapter)
    {
        if (mHeaders == null) {
            mHeaders = new ArrayList<>();//����һ���б�
            for (int i = 0; i < adapter.getCount(); ++i)
                mHeaders.add((Header) adapter.getItem(i));//��adatper��ͷ��2���ݣ�����mHeaders����б��С�
        }
        super.setListAdapter(new MyPrefsHeaderAdapter(this, mHeaders));
    }

}
