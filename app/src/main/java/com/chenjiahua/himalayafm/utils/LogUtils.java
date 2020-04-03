package com.chenjiahua.himalayafm.utils;

import android.util.Log;

public class LogUtils {

    public static String mLog = "Log";

    public static boolean isRelease = false;

    public static void initLog (String log,boolean release){
        mLog = log;
       isRelease = release;
    }

    public static void v (String TAG,String content){
        if(!isRelease){
            Log.d("[" + mLog + "]"+TAG,content);
        }
    }
    public static void d (String TAG,String content){
        if(!isRelease){
            Log.d("[" + mLog + "]"+TAG,content);
        }
    }
    public static void e (String TAG,String content){
        if(!isRelease){
            Log.d("[" + mLog + "]"+TAG,content);
        }
    }
    public static void i (String TAG,String content){
        if(!isRelease){
            Log.d("[" + mLog + "]"+TAG,content);
        }
    }
    public static void w (String TAG,String content){
        if(!isRelease){
            Log.d("[" + mLog + "]"+TAG,content);
        }
    }
    public static void a (String TAG,String content){
        if(!isRelease){
            Log.d("[" + mLog + "]"+TAG,content);
        }
    }



}
