package com.firago.serg.qraplication.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.caverock.androidsvg.SVGParseException;
import com.firago.serg.qraplication.AsyncSvgLoadingTask;
import com.firago.serg.qraplication.R;
import com.firago.serg.qraplication.util.FileUtil;
import com.firago.serg.qraplication.util.QRHelper;
import com.firago.serg.qraplication.util.RequestPermissions;
import com.firago.serg.qraplication.util.SvgHelper;

import net.glxn.qrgen.core.exception.QRGenerationException;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements InputDialog.InputDialogListener {

    private static final String TAG = "MainActivity";
    private static final int REQUEST_CODE_QRSCAN = 1000;

    private static final String EXTRA_SVG_KEY = "com.firago.serg.qraplication.ui.MainActivity.EXTRA_KEY_SVG";
    private static final String EXTRA_STATE_KEY = "com.firago.serg.qraplication.ui.MainActivity.EXTRA_KEY_STATE";
    private static final int NOT_LOADED = 0;
    private static final int QR_PICTURE = 1;
    private static final int SVG_PICTURE = 2;

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

    private String svgText;
    private int state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        state = NOT_LOADED;
        if (savedInstanceState != null) {
            svgText = savedInstanceState.getString(EXTRA_SVG_KEY);
            state = savedInstanceState.getInt(EXTRA_STATE_KEY);
        }
        Log.d(TAG, "onCreate: state=" + state);
        setButtonState();
        switch (state) {
            case NOT_LOADED: {
                setNotLoadedState();
                break;
            }
            case QR_PICTURE: {
                setQRCodeState();
                break;
            }
            case SVG_PICTURE: {
                setSvgState();
                break;
            }
        }
    }

    private void setButtonState() {
        switch (state) {
            case NOT_LOADED: {
                btExport.setText(R.string.main_export);
                btExport.setEnabled(false);
                break;
            }
            case QR_PICTURE: {
                btExport.setText(R.string.main_svg);
                btExport.setEnabled(true);
                break;
            }
            case SVG_PICTURE: {
                btExport.setText(R.string.main_export);
                btExport.setEnabled(true);
                break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_QRSCAN) {
            if (resultCode == RESULT_OK) {
                svgText = ScanActivity.getQRCode(data);
                setSvgState();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(EXTRA_SVG_KEY, svgText);
        outState.putInt(EXTRA_STATE_KEY, state);
        Log.d(TAG, "onSaveInstanceState: state=" + state);
        super.onSaveInstanceState(outState);
    }


    @Override
    public void onDialogOkClick(String text) {
        if (text.trim().isEmpty()) return;
        if(!URLUtil.isValidUrl(text)){
            // it's storage file
            File file = new File(FileUtil.getExternalStorageDir(), text);
            try {
                text = file.toURI().toURL().toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        showSnackBarError(text);
        new AsyncSvgLoadingTask(this).execute(text);
    }


    @OnClick(R.id.btImport)
    public void onClickImport() {
        InputDialog inputDialog = new InputDialog();
        inputDialog.show(getSupportFragmentManager(), "Input");
    }

    @OnClick(R.id.btExport)
    public void onClickExport() {
        if (svgIsNotLoaded()) return;
        if (state != QR_PICTURE) {
            setQRCodeState();
        } else {
            setSvgState();
        }
    }


    @OnClick(R.id.btRead)
    public void onClickRead() {
        startActivityForResult(ScanActivity.newIntent(this), REQUEST_CODE_QRSCAN);
    }

    @OnClick(R.id.btSave)
    public void onClickSave() {
        if (svgIsNotLoaded()) return;

        if (!RequestPermissions.writeExternalPermission(this)) return;
        File file = null;
        try {
            file = FileUtil.createImageFile();
            Log.d(TAG, "onClickSave: " + file.getAbsolutePath());
            QRHelper.writeToFile(svgText, file);
            Toast.makeText(this, getString(R.string.main_save_qr_to)+" " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (QRGenerationException e){
            e.printStackTrace();
            showSnackBarError(getString(R.string.main_error_too_big_data));
            if (file!=null) file.delete();

        }
    }

    private boolean svgIsNotLoaded() {
        return svgText == null || svgText.trim().isEmpty();
    }





    private void setSvgState() {
        try {
            Bitmap bitmap = SvgHelper.getBitmapFromSvgText(svgText);
            ivPicture.setImageBitmap(bitmap);
            state = SVG_PICTURE;
        } catch (SVGParseException e) {
            state = NOT_LOADED;
            svgText = null;
            showSnackBarError(getString(R.string.main_incorrect_data));
            e.printStackTrace();
        }
        setButtonState();
    }

    private void setQRCodeState() {
        try {
            Bitmap bitmap = QRHelper.getQRBitmap(svgText);
            ivPicture.setImageBitmap(bitmap);
            state = QR_PICTURE;
            setButtonState();
        }catch (QRGenerationException e){
            e.printStackTrace();
            showSnackBarError(getString(R.string.main_error_too_big_data));
        }
    }



    private void setNotLoadedState() {
        state = NOT_LOADED;
        ivPicture.setImageResource(R.mipmap.ic_launcher_round);
        setButtonState();
    }


    public void setSvg(String s) {
        svgText = s;
        if (s == null) {
            setNotLoadedState();
            setButtonState();
        } else {
            setSvgState();
            setButtonState();
        }
    }

    private void showSnackBarError(String message) {
        Snackbar snackbar = Snackbar.make(btSave, message, Snackbar.LENGTH_LONG);

        Snackbar.SnackbarLayout snackbarLayout = (Snackbar.SnackbarLayout) snackbar.getView();

        TextView textView = snackbarLayout.findViewById(android.support.design.R.id.snackbar_text);
        textView.setVisibility(View.INVISIBLE);

        snackbarLayout.setPadding(0,0,0,0);
        View view = getLayoutInflater().inflate(R.layout.snackbar_item, snackbarLayout);

        TextView tvErrorMessage = view.findViewById(R.id.tvErrorMessage);
        tvErrorMessage.setText(message);
        snackbar.show();
    }

}
