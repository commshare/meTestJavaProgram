package sc.music.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import sc.droid.dmc.R;

/**
 * Created by Administrator on 2015/6/5.
 */
public class CDSDeviceFragment extends Fragment {
    public CDSDeviceFragment() {
    }
    private static View view=null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try{
            view=inflater.inflate(R.layout.sub_device_fragment_layout,container,false);
        }catch(InflateException e){
                /* parent is already there, just return view as it is */
        }
        return view;
    }
}
