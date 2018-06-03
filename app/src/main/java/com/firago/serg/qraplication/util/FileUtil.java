package com.firago.serg.qraplication.util;

import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;



public class FileUtil {
    private static String TAG = "FileUtil";

    public static File createImageFile() throws IOException {
        // Create an image file name
        String imageFileName = imageFileName();
        File storageDir = getPublicAlbumStorageDir("qr");
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".png",         /* suffix */
                storageDir      /* directory */
        );

        return image;
    }

    public static File getPublicPicturesStorageDir() {
        // Get the directory for the user's public pictures directory.
        File file = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        return file;
    }

    public static File getExternalStorageDir(){
//        return Environment.getExternalStoragePublicDirectory(Environment.MEDIA_MOUNTED);
        return getPublicPicturesStorageDir();
    }

    public static File getPublicAlbumStorageDir(String albumName) {
        // Get the directory for the user's public pictures directory.

        File file = new File(getPublicPicturesStorageDir(), albumName);
        if (!file.mkdirs()) {
            Log.e(TAG, "Directory not created " + file.getAbsolutePath());
        }
        return file;
    }

    @NonNull
    private static String imageFileName() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return "QR_" + timeStamp + "_";
    }
}
