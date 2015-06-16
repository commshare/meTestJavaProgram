package sc.music.ui.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;

import sc.music.common.Settings;

/**
 * Created by Administrator on 2015/6/16.
 */
//实现CompoundButton的
public class ContentDirectoryEnabler  implements CompoundButton.OnCheckedChangeListener{
    private final Context mContext;
    private Switch mSwitch;

    //操控外部传递进来的Switch
    public ContentDirectoryEnabler(Context context, Switch switch_)
    {
        mContext = context;
        setSwitch(switch_);
    }

    public void resume() {
        mSwitch.setOnCheckedChangeListener(this);
    }

    public void pause() {
        mSwitch.setOnCheckedChangeListener(null);
    }

    public void setSwitch(Switch switch_)
    {
        if(mSwitch == switch_) return;
        if(mSwitch != null)
            mSwitch.setOnCheckedChangeListener(null);
        mSwitch = switch_;
        mSwitch.setOnCheckedChangeListener(this);
        mSwitch.setChecked(isSwitchOn());
    }

    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
    {
//		if (/* Send intent message to activate content directory*/) {
//			// Intent has been taken into account, disable until new state is active
//			mSwitch.setEnabled(false);
//		} else {
//			// Error
//		}
        Log.d("ContentDirectoryEnabler", "onCheckedChanged " + ((isChecked) ? " true" : " false"));
        setConfig(isChecked);
    }

    private void setSwitchChecked(boolean checked)
    {
        if (checked != mSwitch.isChecked())
            mSwitch.setChecked(checked);
    }

    //根据switch的状态，设置是否激活本地CDS
    public void setConfig(boolean isChecked)
    {
        Log.d("ContentDirectoryEnabler", "setConfig " + ((isChecked) ? " true" : " false"));
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(mContext).edit();
        editor.putBoolean(Settings.CONTENTDIRECTORY_SERVICE, isChecked);
        editor.apply();
    }

    public boolean isSwitchOn()
    {
        Log.d("ContentDirectoryEnabler", "isSwitchOn " + ((PreferenceManager.getDefaultSharedPreferences(mContext)
                .getBoolean(Settings.CONTENTDIRECTORY_SERVICE, true)) ? " true" : " false"));
        return PreferenceManager.getDefaultSharedPreferences(mContext)
                .getBoolean(Settings.CONTENTDIRECTORY_SERVICE, true);
    }
}
