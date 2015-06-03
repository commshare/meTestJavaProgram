package sc.music.ui.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;



import java.util.ArrayList;
import java.util.List;

import sc.droid.dmc.R;

/**
 * Created by Administrator on 2015/5/27.
 */
public class PagerFragmentAdapter extends FragmentPagerAdapter {
    Context mContext;
    private  String[] TITLES ;//=
    //;// {"�Ƽ�", "����", "�����Ȱ�", "�����Ƽ�", "ר��", "�����ղ�", "��Ե"};

    //�ڴ�Ҫ���������İ�
    List<Fragment> fragmentList = new ArrayList<Fragment>();

    public PagerFragmentAdapter(FragmentManager fm,Context mContext,List<Fragment> fragmentList) {
        super(fm);
        this.mContext=mContext;
        TITLES = mContext.getResources().getStringArray(R.array.pager_title);//��Ϊ������arrays.xml��
        this.fragmentList=fragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }
}
