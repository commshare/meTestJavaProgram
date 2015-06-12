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
// �ҵĸ�����PreferenceActivity*/
/**
 * Created by Administrator on 2015/6/11.
 */
public class SettingsActivity  extends PreferenceActivity {
    private Toolbar mActionBar;//v7��

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
        LayoutInflater.from(this).inflate(layoutResID, contentWrapper, true);
        getWindow().setContentView(contentView);
    }
}
