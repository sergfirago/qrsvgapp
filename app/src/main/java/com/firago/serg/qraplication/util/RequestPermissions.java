package com.firago.serg.qraplication.util;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class RequestPermissions {
    private final static String TAG = "RequestPermission";

    private static int checkWriteExternalPermission(AppCompatActivity activity, String permission) {
        return ContextCompat.checkSelfPermission(activity, permission);
    }

    private static boolean requestPermission(AppCompatActivity activity, String permission) {
        int permission_code = checkWriteExternalPermission(activity, permission);
        Log.d(TAG, "requestPermission: cod = " + permission_code);
        Log.d(TAG, "requestPermission: " + permission);
        if (permission_code == PackageManager.PERMISSION_GRANTED) return true;
        ActivityCompat.requestPermissions(activity, new String[]{permission}
                , 0);
        return true;
    }

    public static boolean writeExternalPermission(AppCompatActivity activity) {
        return requestPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    public static boolean cameraPermission(AppCompatActivity activity) {
        return requestPermission(activity, Manifest.permission.CAMERA);
    }
}
