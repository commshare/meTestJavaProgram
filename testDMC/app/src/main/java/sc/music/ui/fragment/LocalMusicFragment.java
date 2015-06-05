package sc.music.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.view.View;

import sc.droid.dmc.R;


/**
 * Created by Administrator on 2015/5/27.
 */
public class LocalMusicFragment extends Fragment implements AbsListView.OnScrollListener {


    public LocalMusicFragment() {
    }
    public static LocalMusicFragment  newInstance(){
        LocalMusicFragment f=new LocalMusicFragment();
        return f;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      //  return super.onCreateView(inflater, container, savedInstanceState);
        return  inflater.inflate(R.layout.simple_listview_layout,container,false);
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {

    }

    @Override
    public void onScroll(AbsListView absListView, int i, int i1, int i2) {

    }
}
