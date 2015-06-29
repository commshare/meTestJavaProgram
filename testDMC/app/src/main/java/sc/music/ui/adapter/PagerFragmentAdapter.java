package sc.music.ui.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


import com.xwj.toolbardemo.BaseCardFragment;

import java.util.ArrayList;
import java.util.List;

import sc.droid.dmc.R;
import sc.music.ui.fragment.ContentDirectoryFragment;
import sc.music.ui.fragment.DeviceFragment;
import sc.music.ui.fragment.LocalMusicFragment;

/**
 * Created by Administrator on 2015/5/27.
 */
public class PagerFragmentAdapter extends FragmentPagerAdapter {
    Context mContext;
    private  String[] TITLES ;//=
    //;// {"推荐", "分类", "本月热榜", "热门推荐", "专栏", "热门收藏", "随缘"};

    //内存要在这里分配的啊
    List<Fragment> fragmentList ;//= new ArrayList<Fragment>();

    public PagerFragmentAdapter(FragmentManager fm,Context mContext,List<Fragment> fragmentList) {
        super(fm);
        this.mContext=mContext;
        TITLES = mContext.getResources().getStringArray(R.array.pager_title);//因为定义在arrays.xml中
        this.fragmentList=fragmentList;
    }
    public PagerFragmentAdapter(FragmentManager fm,Context mContext) {
        super(fm);
        this.mContext=mContext;
        TITLES = mContext.getResources().getStringArray(R.array.pager_title);//因为定义在arrays.xml中
    }

    @Override
    public Fragment getItem(int position) {
     //   return fragmentList.get(position);
        switch(position) {

            case 0: return LocalMusicFragment.newInstance();
            //case 1: return BaseCardFragment.newInstance(1);
            case 2: return BaseCardFragment.newInstance(2);
            case 1: return DeviceFragment.newInstance();
            default: return BaseCardFragment.newInstance(3);
        }
    }

    @Override
    public int getCount() {
       // return fragmentList.size();
        return TITLES.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TITLES[position];
    }
}
