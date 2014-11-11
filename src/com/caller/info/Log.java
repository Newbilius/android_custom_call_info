package com.caller.info;

public class Log {
    private static String code="hh42";

    public static void Debug(String s){
        android.util.Log.d(code,s);
    }
}
