package com.firago.serg.qraplication.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScanActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private static final String EXTRA_QR_CODE = "com.firago.serg.qraplication.QR_CODE";

    public static Intent newIntent(Context context){
        return new Intent(context, ScanActivity.class);
    }
    public static String getQRCode(Intent intent){
        return intent.getStringExtra(EXTRA_QR_CODE);
    }

    private final String TAG = "ScanActivity";

    private ZXingScannerView mScannerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
        setContentView(mScannerView);
    }
    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }

    @Override
    public void handleResult(Result result) {

        // If you would like to resume scanning, call this method below:
        mScannerView.resumeCameraPreview(this);
        Intent intent = new Intent();
        intent.putExtra(EXTRA_QR_CODE, result.getText());
        setResult(RESULT_OK, intent);
        finish();
    }
}
