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
    List<Fragment> fragmentList ;//= new ArrayList<Fragment>();

    public PagerFragmentAdapter(FragmentManager fm,Context mContext,List<Fragment> fragmentList) {
        super(fm);
        this.mContext=mContext;
        TITLES = mContext.getResources().getStringArray(R.array.pager_title);//��Ϊ������arrays.xml��
        this.fragmentList=fragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
//        switch(pos) {
//
////            case 0: return FirstFragment.newInstance("FirstFragment, Instance 1");
////            case 1: return SecondFragment.newInstance("SecondFragment, Instance 1");
////            case 2: return ThirdFragment.newInstance("ThirdFragment, Instance 1");
////            case 3: return ThirdFragment.newInstance("ThirdFragment, Instance 2");
////            case 4: return ThirdFragment.newInstance("ThirdFragment, Instance 3");
////            default: return ThirdFragment.newInstance("ThirdFragment, Default");
//        }
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TITLES[position];
    }
}
