package sc.music;

import android.app.Application;

/**
 * Created by Administrator on 2015/5/29.
 */
public class SpiderApplication extends Application {
    private static SpiderApplication sBeyondApplication = null;

    @Override
    public void onCreate() {
        super.onCreate();
        sBeyondApplication = this;
    }

    synchronized public static SpiderApplication getApplication() {
        return sBeyondApplication;
    }
}

