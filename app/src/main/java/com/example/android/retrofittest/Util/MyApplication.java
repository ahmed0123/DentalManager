package com.example.android.retrofittest.Util;


import android.app.Application;

public class MyApplication extends Application {

    private static MyApplication mInstance;

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }
    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
    }



    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListiner listener) {
        ConnectivityReceiver.connectivityReceiverListiner = listener;
    }
}
