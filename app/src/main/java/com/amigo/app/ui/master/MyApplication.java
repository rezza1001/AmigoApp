package com.amigo.app.ui.master;

import android.app.Application;

public class MyApplication extends Application {

    public static String SERBADA_SERVER;
    public static String TOKEN_FCM;
    private static final String TAG = "MyApplication";

    @Override
    public void onCreate() {
        super.onCreate();

//        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
//            if (!task.isSuccessful()) {
//                Log.w(TAG, "Fetching FCM registration token failed", task.getException());
//                return;
//            }
//
//            String token = task.getResult();
//            Log.d(TAG, "Firebase Token "+ token);
//            TOKEN_FCM = token;
//        });
    }
}
