package com.firago.serg.qraplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import net.glxn.qrgen.android.QRCode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final int REQUEST_CODE_QRSCAN = 1000;
    @BindView(R.id.btExport)
    Button btExport;
    @BindView(R.id.btImport)
    Button btImport;
    @BindView(R.id.btRead)
    Button btRead;
    @BindView(R.id.btSave)
    Button btSave;
    @BindView(R.id.ivPicture)
    ImageView ivPicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }
    @OnClick(R.id.btImport)
    public void onClickImport(){
        try {
            String file = "Svg.svg";
            InputStream inputStream = getAssets().open(file);
//            SVG svg =SVG.getFromInputStream(inputStream);

            SVG svg = SVG.getFromString(getText(inputStream, 3000));
            Bitmap bitmap = Bitmap.createBitmap((int) svg.getDocumentWidth(), (int) svg.getDocumentHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            svg.renderToCanvas(canvas);
            ivPicture.setImageBitmap(bitmap);
        } catch (SVGParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.btExport)
    public void onClickExport(){
        String file = "Svg.svg";
        try {
            InputStream inputStream = getAssets().open(file);
            String svg = getText(inputStream, 3000);
            Log.d(TAG, "onClickExport: "+svg);
            Bitmap bitmap = QRCode.from(svg)
                    .withCharset("ISO-8859-1")
                    .withErrorCorrection(ErrorCorrectionLevel.L)
                    .withColor(Color.BLUE, Color.WHITE)
                    .withSize(500, 500)
                    .bitmap();
            ivPicture.setImageBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @OnClick(R.id.btRead)
    public void onClickRead(){
        startActivityForResult(ScanActivity.newIntent(this), REQUEST_CODE_QRSCAN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==REQUEST_CODE_QRSCAN){
            String qrCode = ScanActivity.getQRCode(data);
            Toast.makeText(this, qrCode, Toast.LENGTH_LONG).show();
            SVG svg = null;
            try {
                svg = SVG.getFromString(qrCode);
            } catch (SVGParseException e) {
                e.printStackTrace();
            }
            Bitmap bitmap = Bitmap.createBitmap((int) svg.getDocumentWidth(), (int) svg.getDocumentHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            svg.renderToCanvas(canvas);
            ivPicture.setImageBitmap(bitmap);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private String getText(InputStream inputStream, int size) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String buf;
        StringBuilder builder = new StringBuilder();
        while((buf = reader.readLine())!=null){
            builder.append(buf);
        }
        String result = builder.toString();
        return result.substring(0, Math.min(size,result.length()));
    }
}
