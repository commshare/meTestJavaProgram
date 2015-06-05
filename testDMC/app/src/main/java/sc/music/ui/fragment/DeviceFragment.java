package sc.music.ui.fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.device_fragment_layout,container,false);
    }
}
