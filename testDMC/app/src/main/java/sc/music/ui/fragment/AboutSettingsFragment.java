package sc.music.ui.fragment;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.util.Log;

//import de.psdev.licensesdialog.LicensesDialog;
//import de.psdev.licensesdialog.licenses.ApacheSoftwareLicense20;
//import de.psdev.licensesdialog.licenses.BSD3ClauseLicense;
//import de.psdev.licensesdialog.licenses.GnuLesserGeneralPublicLicense21;
//import de.psdev.licensesdialog.model.Notice;
//import de.psdev.licensesdialog.model.Notices;
import sc.droid.dmc.R;

/**
 * Created by Administrator on 2015/6/16.
 */
public class AboutSettingsFragment  extends PreferenceFragment {
    private static final String TAG = "AboutSettingsFragment";
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.about);

        // Set version
        try {
            Preference pref = findPreference("version");
            pref.setSummary(getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0).versionName);
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "exception", e);
        }

        // Dialog for external license
        Preference pref = findPreference("licenses_other");
        pref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
//                Notices notices = new Notices();
//                notices.addNotice(new Notice(
//                        "AppCompat", "http://developer.android.com/tools/support-library/",
//                        "Copyright (C) The Android Open Source Project", new ApacheSoftwareLicense20()));
//                notices.addNotice(new Notice(
//                        "Cling", "http://4thline.org/projects/cling/",
//                        "Copyright (C) 4th Line GmbH", new GnuLesserGeneralPublicLicense21()));
//                notices.addNotice(new Notice(
//                        "NanoHttpd", "https://github.com/NanoHttpd/nanohttpd",
//                        "Copyright (C) 2012-2013 by Paul S. Hawke, 2001,2005-2013 by Jarno Elonen, 2010 by Konstantinos Togias", new BSD3ClauseLicense()));
//                notices.addNotice(new Notice(
//                        "ActionBar-PullToRefresh", "https://github.com/chrisbanes/ActionBar-PullToRefresh",
//                        "Copyright (C) Chris Banes", new ApacheSoftwareLicense20()));
//                notices.addNotice(new Notice(
//                        "LicenseDialog", "http://psdev.de/LicensesDialog/",
//                        "Copyright (C) Philip Schiffer", new ApacheSoftwareLicense20()));
//
//                LicensesDialog.Builder licensesDialog = new LicensesDialog.Builder(getActivity());
//                licensesDialog.setNotices(notices);
//                licensesDialog.setTitle(R.string.licenses_other);
//                licensesDialog.build().show();
                return false;
            }

        });
    }
}
