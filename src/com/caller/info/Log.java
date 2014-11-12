package com.caller.info;

public class Log {
    private final static String LOG_CODE ="hh42";

    public static void debug(String s){
        android.util.Log.d(LOG_CODE,s);
    }
}
