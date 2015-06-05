package sc.music.ui.fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import sc.droid.dmc.R;

/**
 * Created by Administrator on 2015/6/5.
 */
public class DeviceFragment extends Fragment {
    public DeviceFragment() {
    }
    private static View view=null;
    public static DeviceFragment newInstance(){
        DeviceFragment f=new DeviceFragment();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      //  return inflater.inflate(R.layout.device_fragment_layout,container,false);
        //原来是这里导致的子fragment加载报错啊
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try{
            view=inflater.inflate(R.layout.device_fragment_layout,container,false);
        }catch(InflateException e){
                /* parent is already there, just return view as it is */
        }
        return view;
    }
}
