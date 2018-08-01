package com.firago.serg.qraplication.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class IntentLoader {

    private static final String TAG = "IntentLoader";

    public static String loadFromIntent(Context context, Intent intent) {
        String type = intent.getType();
        String action = intent.getAction();
        Bundle extras = intent.getExtras();
        if (Intent.ACTION_SEND.equals(action)) {
            Log.d(TAG, "onCreate: " + type);
            assert type != null;
            if (type.contains("image/")) {
                Uri data = intent.getData();
                Uri uri = (Uri) extras.getParcelable(Intent.EXTRA_STREAM);

                Log.d(TAG, "onCreate: " + data);
                Log.d(TAG, "onCreate: " + uri);
                assert data != null;
                return loadImage(context, uri);
            }
        }
        return "";
    }

    private static String loadImage(Context context, Uri data) {
        if (data == null) return "";
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        try {
            InputStream inputStream;
            if ("content".equals(data.getScheme())) {
                inputStream = context.getContentResolver().openInputStream(data);
            }else{
                inputStream = new FileInputStream(data.getPath());
            }
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null, options);
            MultiFormatReader reader = new MultiFormatReader();
            int[] intArray = new int[bitmap.getWidth() * bitmap.getHeight()];
            //copy pixel data from the Bitmap into the 'intArray' array
            bitmap.getPixels(intArray, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());

            LuminanceSource source = new RGBLuminanceSource(bitmap.getWidth(), bitmap.getHeight(), intArray);
            BinaryBitmap bbitmap = new BinaryBitmap(new HybridBinarizer(source));

            Result result = reader.decode(bbitmap);
            return result.getText();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (NotFoundException e) {
            e.printStackTrace();
        }

        return "";
    }

}
