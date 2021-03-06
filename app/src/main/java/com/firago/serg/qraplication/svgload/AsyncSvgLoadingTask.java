package com.firago.serg.qraplication.svgload;

import android.os.AsyncTask;

import com.firago.serg.qraplication.ui.MainActivity;
import com.firago.serg.qraplication.util.SvgHelper;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URL;

public class AsyncSvgLoadingTask extends AsyncTask<String, Void, String > {
    private String url;

    public AsyncSvgLoadingTask(MainActivity mainActivity) {
        this.mainActivity = new WeakReference<>(mainActivity);
    }

    private WeakReference<MainActivity> mainActivity;
    @Override
    protected String doInBackground(String... urls) {
        try {
            url = urls[0];
            InputStream inputStream = new URL(url).openStream();
            String svgText = SvgHelper.getSvgText(inputStream);
            return svgText;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    protected void onPostExecute(String s) {
        MainActivity mainActivity = this.mainActivity.get();
        if (mainActivity!=null){
            if (s!=null) mainActivity.setSvg(s);
            else mainActivity.setSvgError(url);
        }
    }
}
