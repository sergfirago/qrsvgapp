package com.firago.serg.qraplication.util;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import net.glxn.qrgen.android.QRCode;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class QRHelper {
    public static QRCode getQrCode(String svg) {
        return QRCode.from(svg)
                .withCharset("ISO-8859-1")
                .withErrorCorrection(ErrorCorrectionLevel.L)
                .withColor(Color.BLUE, Color.WHITE)
                .withSize(500, 500);
    }

    public static void writeToFile(String svgText, File file) throws FileNotFoundException {
        QRCode qrCode = QRHelper.getQrCode(svgText);
        OutputStream outputStream = new FileOutputStream(file);
        qrCode.writeTo(outputStream);
    }

    public static Bitmap getQRBitmap(String svgText) {
        return getQrCode(svgText).bitmap();
    }
}
