package com.qingchenglei.toolbardemo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qingchenglei.toolbardemo.R;

/**
 * Created by Zech on 2015/5/3.
 * <p/>
 * simple fragment
 */
public class MyFragment extends Fragment {

    public static MyFragment getInstance(int position){
        MyFragment myFragment = new MyFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        myFragment.setArguments(bundle);
        return myFragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        TextView textView = (TextView) view.findViewById(R.id.tv_simple);
        Bundle bundle = getArguments();
        if (bundle != null){
            textView.setText("Page"+bundle.getInt("position"));
        }
    }
}
