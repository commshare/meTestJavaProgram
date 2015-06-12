package sc.music.ui.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import sc.droid.dmc.R;

/**
 * Created by Administrator on 2015/6/11.
 */
public class DrawerFragment extends PreferenceFragment
        implements SharedPreferences.OnSharedPreferenceChangeListener
{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.sub_device_fragment_layout,container,false);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {

    }
}
