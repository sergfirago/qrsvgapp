package com.firago.serg.qraplication.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.support.annotation.NonNull;

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class SvgHelper {

    public static final int DEFAULT_SIZE = 200;

    public static String getSvgText(InputStream inputStream) throws IOException, IndexOutOfBoundsException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String buf;
        StringBuilder builder = new StringBuilder();
        while ((buf = reader.readLine()) != null) {
            builder.append(buf);
        }
        String result = builder.toString();
        int svgTextLength = result.length();

        return result.substring(0, svgTextLength);
    }

    @NonNull
    public static Bitmap getBitmapFromSvgText(String svgText) throws SVGParseException {
        SVG svg = SVG.getFromString(svgText);
        int documentWidth = (int) svg.getDocumentWidth();
        int documentHeight = (int) svg.getDocumentHeight();
        if (documentHeight<=0 ) documentHeight = DEFAULT_SIZE;
        if (documentWidth<=0 ) documentWidth = DEFAULT_SIZE;
        Bitmap bitmap = Bitmap.createBitmap( documentWidth, documentHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        svg.renderToCanvas(canvas);
        return bitmap;
    }
}
