package sc.music.common;

import android.content.Context;
import android.preference.PreferenceManager;

/**
 * Created by Administrator on 2015/6/16.
 */
public class Settings {
    public static final String CONTENTDIRECTORY_SERVICE = "pref_contentDirectoryService";
    public static final String CONTENTDIRECTORY_NAME = "pref_contentDirectoryService_name";
    public static final String CONTENTDIRECTORY_SHARE = "pref_contentDirectoryService_share";
    public static final String CONTENTDIRECTORY_VIDEO = "pref_contentDirectoryService_video";
    public static final String CONTENTDIRECTORY_AUDIO = "pref_contentDirectoryService_audio";
    public static final String CONTENTDIRECTORY_IMAGE = "pref_contentDirectoryService_image";

    //本地内容目录服务名
    public static String getSettingContentDirectoryName(Context ctx)
    {
        String value = PreferenceManager.getDefaultSharedPreferences(ctx)
                .getString(CONTENTDIRECTORY_NAME, "");
        return (value != "") ? value : android.os.Build.MODEL;
    }
}
