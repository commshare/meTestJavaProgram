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
    //;// {"推荐", "分类", "本月热榜", "热门推荐", "专栏", "热门收藏", "随缘"};

    //内存要在这里分配的啊
    List<Fragment> fragmentList = new ArrayList<Fragment>();

    public PagerFragmentAdapter(FragmentManager fm,Context mContext,List<Fragment> fragmentList) {
        super(fm);
        this.mContext=mContext;
        TITLES = mContext.getResources().getStringArray(R.array.pager_title);//因为定义在arrays.xml中
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
