package com.jyt.baseapp.domin;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.view.ViewConfiguration;

import java.lang.reflect.Field;

//import com.google.gson.Gson;

/**
 * Created by Administrator on 2016/11/20.
 */
public class MyPlayApplication extends Application {
    private static Context context;
    private static Handler handler;
    private static int mainThreadid;
//    private static Typeface typeface;
//    private static Gson gson;

    @Override
    public void onCreate() {
        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            if (menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception ex) {
            // Ignore
        }
        super.onCreate();
        context = getApplicationContext();
        handler = new Handler();
        mainThreadid = android.os.Process.myTid();//主线程ID
//        typeface = Typeface.createFromAsset(context.getAssets(), "fonts/pop.ttf");

    }

    public static Context getContext() {
        return context;
    }

    public static Handler getHandler() {
        return handler;
    }

    public static int getMainThreadid() {
        return mainThreadid;
    }


}
