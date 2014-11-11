package com.caller.info;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.telephony.TelephonyManager;
import android.view.*;
import android.widget.Button;
import android.widget.TextView;

public class CallReceiver extends BroadcastReceiver {
    private static String PhoneNumber = "";
    private static boolean Incoming = false;
    private static WindowManager windowManager;
    private static ViewGroup layoutInfo;
    private static LayoutInflater layoutInflater;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.PHONE_STATE")) {
            String phone_state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
            if (phone_state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {

                PhoneNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
                Incoming = true;

                Log.Debug("Show window: " + PhoneNumber);

                /*
                try {
                    //грязноватый хак, но можно использовать для надёжности
                    Thread.sleep(1000);
                } catch (InterruptedException ie) {
                    //ну и ладно
                }*/

                ShowWindow(context,PhoneNumber);

            } else if (phone_state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
                //телефон находится в режиме звонка (набор номера / разговор) - закрываем окно
                if (Incoming) {
                    Log.Debug("Close window.");
                    CloseWindow();
                    Incoming = false;
                }
            } else if (phone_state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
                //телефон находиться в ждущем режиме. Это событие наступает по окончанию разговора.
                Log.Debug("Close window.");
                CloseWindow();
                Incoming = false;
            }
        }
    }

    private void ShowWindow(Context context,String phone) {
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.TOP;

        layoutInfo = (ViewGroup) layoutInflater.inflate(R.layout.info, null);

        TextView textViewNumber=(TextView)layoutInfo.findViewById(R.id.textViewNumber);
        Button buttonClose=(Button)layoutInfo.findViewById(R.id.buttonClose);
        textViewNumber.setText(phone);
        buttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CloseWindow();
            }
        });

        windowManager.addView(layoutInfo, params);
    }

    private void CloseWindow() {
        if (layoutInfo!=null){
            windowManager.removeView(layoutInfo);
            layoutInfo=null;
        }
    }
}