package com.qingchenglei.toolbardemo.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.qingchenglei.toolbardemo.R;
import com.qingchenglei.toolbardemo.fragment.MyFragment;
import com.qingchenglei.toolbardemo.fragment.TabOneFragment;
import com.qingchenglei.toolbardemo.fragment.TabThreeFragment;
import com.qingchenglei.toolbardemo.fragment.TabTwoFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zech on 2015/5/6.
 * <p/>
 * FragmentPagerAdapter
 */
public class MyFragmentAdapter extends FragmentPagerAdapter {
    String[] tabs;
    int[] icons;
    Context context;
    List<Fragment> fragments;

    public MyFragmentAdapter(Context context,FragmentManager fm) {
        super(fm);
        tabs = context.getResources().getStringArray(R.array.tabs);
        icons = new int[]{R.mipmap.ic_home_black_24dp, R.mipmap.ic_favorite_black_24dp, R.mipmap.ic_supervisor_account_black_24dp};
        fragments = new ArrayList<Fragment>();
        fragments.add(new TabOneFragment());
        fragments.add(new TabTwoFragment());
        fragments.add(new TabThreeFragment());
    }

    @Override
    public CharSequence getPageTitle(int position) {
        //tabs with icon
//            Drawable drawable = getResources().getDrawable(icons[position]);
//            drawable.setBounds(0,0,72,72);
//            ImageSpan imageSpan = new ImageSpan(drawable);
//            SpannableString spannableString = new SpannableString(" ");
//            spannableString.setSpan(imageSpan,0,spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//            return spannableString;
        return tabs[position];
    }

    @Override
    public Fragment getItem(int i) {
//        return MyFragment.getInstance(i);
        return fragments.get(i);
    }

    @Override
    public int getCount() {
        return 3;
    }
}
