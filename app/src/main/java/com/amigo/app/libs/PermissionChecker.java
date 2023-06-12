package com.amigo.app.libs;

import android.Manifest;
import android.content.pm.PackageManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class PermissionChecker {

    public static final int PERMISSION_STORAGE = 1;
    public static final int PERMISSION_BLUETOOTH = 2;

    public static boolean checkPermissionStorage(AppCompatActivity mActivity){
        String[] PERMISSIONS = new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE};

        if(hasPermissions(mActivity, PERMISSIONS)){
            ActivityCompat.requestPermissions(mActivity, PERMISSIONS, PERMISSION_STORAGE);
            return false;
        }
        return true;

    }

    public static boolean checkBluetooth(AppCompatActivity mActivity){
        String[] PERMISSIONS = new String[0];
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            PERMISSIONS = new String[]{
                    Manifest.permission.BLUETOOTH,
                    Manifest.permission.BLUETOOTH_CONNECT,
                    Manifest.permission.BLUETOOTH_SCAN};
        }

        if(hasPermissions(mActivity, PERMISSIONS)){
            ActivityCompat.requestPermissions(mActivity, PERMISSIONS, PERMISSION_BLUETOOTH);
            return false;
        }
        return true;

    }


    public static boolean checkLocation(AppCompatActivity mActivity){
        String[] PERMISSIONS = new String[0];
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            PERMISSIONS = new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION};
        }

        if(hasPermissions(mActivity, PERMISSIONS)){
            ActivityCompat.requestPermissions(mActivity, PERMISSIONS, PERMISSION_BLUETOOTH);
            return false;
        }
        return true;

    }


    public static  boolean hasPermissions(AppCompatActivity mActivity,  String... permissions) {
        boolean isValid  = true;
        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(mActivity, permission) != PackageManager.PERMISSION_GRANTED) {
                isValid = false;
            }
        }
        return !isValid;
    }
}
