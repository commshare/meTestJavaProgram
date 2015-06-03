package sc.music.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import org.fourthline.cling.support.messagebox.model.Message;

import java.util.List;

import sc.music.ui.adapter.MySectionIndexer;
import sc.music.ui.iface.PageAction;
import sc.music.ui.layout.LoadRelativeLayout;
import sc.music.ui.view.BladeView;

/**
 * Created by Administrator on 2015/5/27.
 */
public class LocalMusicFragment extends Fragment implements AbsListView.OnScrollListener {


    public LocalMusicFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {

    }

    @Override
    public void onScroll(AbsListView absListView, int i, int i1, int i2) {

    }
}
