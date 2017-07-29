package mm.kso.dayrangepickertest;
import android.app.Application;

import net.danlew.android.joda.JodaTimeAndroid;

/**
 * Created by Kyawsan Oo on 4/6/2017.
 */

public class MyApp extends Application {

    private static MyApp appInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        appInstance = this;
        JodaTimeAndroid.init(this);
    }

    public static MyApp getAppInstance() {
        return appInstance;
    }


}
