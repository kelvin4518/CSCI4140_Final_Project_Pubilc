package hk.com.csci4140.culife;

import android.app.Application;
import android.content.Context;

import com.baidu.mapapi.SDKInitializer;
import com.onesignal.OneSignal;

import hk.com.csci4140.culife.utility.SessionManager;

/**
 * Created by gechen(Ge Chen) on 26/04/2018.
 */

public class MyApplication extends Application {

    private static Context mContext;


    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        //Initialize the session manager
        SessionManager.init(mContext);


        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();

        // Call syncHashedEmail anywhere in your app if you have the user's email.
        // This improves the effectiveness of OneSignal's "best-time" notification scheduling feature.
        // OneSignal.syncHashedEmail(userEmail);
    }


    public static Context getInstance() {
        return mContext;
    }



}
