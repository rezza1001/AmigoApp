package com.amigo.app.ui.master;

import android.annotation.SuppressLint;
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.AndroidViewModel;


public abstract class MyViewModel extends AndroidViewModel {

    @SuppressLint("StaticFieldLeak")
    protected AppCompatActivity mActivity;

    public MyViewModel(@NonNull Application application) {
        super(application);
    }

    public void init(AppCompatActivity activity){
        this.mActivity = activity;


    }
}
