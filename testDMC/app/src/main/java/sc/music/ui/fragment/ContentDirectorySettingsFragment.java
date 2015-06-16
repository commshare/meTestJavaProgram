package sc.music.ui.fragment;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Switch;

import sc.droid.dmc.R;
import sc.music.common.Settings;
import sc.music.ui.view.ContentDirectoryEnabler;

/**
 * Created by Administrator on 2015/6/16.
 */
public class ContentDirectorySettingsFragment  extends PreferenceFragment
        implements SharedPreferences.OnSharedPreferenceChangeListener{
    private ContentDirectoryEnabler enabler;
    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        final Activity activity = getActivity();

        PreferenceManager.getDefaultSharedPreferences(activity).registerOnSharedPreferenceChangeListener(this);
        //布局在这里
        addPreferencesFromResource(R.xml.contentdirectory);
        //创建了一个Switch
        Switch actionBarSwitch = new Switch(activity);

/*
			if(activity instanceof PreferenceActivity)
			{
				PreferenceActivity preferenceActivity = (PreferenceActivity) activity;
				if(preferenceActivity.onIsHidingHeaders() || !preferenceActivity.onIsMultiPane())
				{
					activity.getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM,
							ActionBar.DISPLAY_SHOW_CUSTOM);
					activity.getActionBar().setCustomView(actionBarSwitch, new ActionBar.LayoutParams(
						ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT,
						Gravity.CENTER_VERTICAL | Gravity.END));
				}
			}
			*/
        enabler = new ContentDirectoryEnabler(getActivity(), actionBarSwitch);
        updateSettings();

        EditTextPreference editTextPref = (EditTextPreference) findPreference(Settings.CONTENTDIRECTORY_NAME);
        if(editTextPref != null)
            editTextPref.setSummary(Settings.getSettingContentDirectoryName(activity));
    }

    public void onResume()
    {
        super.onResume();
        enabler.resume();
        updateSettings();
    }

    public void onPause()
    {
        super.onPause();
        enabler.pause();
    }

    protected void updateSettings()
    {
        boolean available = enabler.isSwitchOn();
        Log.d("CDSettingsFragment", "updateSettings " + (available ? " true" : " false"));

        // Enable or not preference field
        for(int i = 0; i < getPreferenceScreen().getPreferenceCount(); ++i)
            getPreferenceScreen().getPreference(i).setEnabled(available);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key)
    {
        Log.d("CDSettingsFragment", "onSharedPreferenceChanged " + key);

        if (key.equals(Settings.CONTENTDIRECTORY_SERVICE))
        {
            updateSettings();
        }
        else if(key.equals(Settings.CONTENTDIRECTORY_NAME))
        {
            EditTextPreference editTextPref = (EditTextPreference) findPreference(Settings.CONTENTDIRECTORY_NAME);
            if(editTextPref != null )
                editTextPref.setSummary(Settings.getSettingContentDirectoryName(getActivity()));
        }
    }
}
