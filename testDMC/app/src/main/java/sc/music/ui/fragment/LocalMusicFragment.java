package sc.music.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.view.View;

import sc.droid.dmc.R;


/**
 * Created by Administrator on 2015/5/27.
 */
public class LocalMusicFragment extends Fragment implements AbsListView.OnScrollListener {

    private static View view=null;
    public LocalMusicFragment() {
    }
    public static LocalMusicFragment  newInstance(){
        LocalMusicFragment f=new LocalMusicFragment();
        return f;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try{
            view=inflater.inflate(R.layout.local_music_layout,container,false);
        }catch(InflateException e){
                /* parent is already there, just return view as it is */
        }
      //  return super.onCreateView(inflater, container, savedInstanceState);
        return  view;
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {

    }

    @Override
    public void onScroll(AbsListView absListView, int i, int i1, int i2) {

    }
}
