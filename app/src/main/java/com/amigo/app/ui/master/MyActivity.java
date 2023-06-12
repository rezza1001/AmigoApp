package com.amigo.app.ui.master;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

public abstract class MyActivity extends AppCompatActivity {

    public static final String BROADCAST_FINISH = "FINISH_PAGE";
    protected AppCompatActivity mActivity;
    protected static String TAG = "MyActivity";


    protected IntentFilter mIntentFilter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(setLayout());
        mActivity = this;
        TAG = mActivity.getClass().getSimpleName();
        mIntentFilter = new IntentFilter();


        initLayout();
        initListener();
        new Handler().postDelayed(this::initialData,100);
    }

    protected abstract int setLayout();
    protected abstract void initLayout();
    protected abstract void initListener();

    protected void registerFinishPage(){
        mIntentFilter.addAction(BROADCAST_FINISH);
        registerReceiver(receiver, mIntentFilter);
    }

    protected void registerNotificationFB(){
//        mActivity.registerReceiver(receiver, new IntentFilter(FirebaseMessageService.MY_ACTION));
    }

    protected void initialData(){
    }

    protected void inComingNotificationFB(String title, String message, JSONObject data){
    }

    protected BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(BROADCAST_FINISH)){
                mActivity.finish();
            }
//            else  if (intent.getAction().equals(FirebaseMessageService.MY_ACTION)){
//                String title = intent.getStringExtra("Title");
//                String message = intent.getStringExtra("Message");
//                inComingNotificationFB(title, message, new JSONObject());
//            }
        }
    };

}
