package com.grupomarostica.cloure.Core;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.InputStream;

public class DownloadImageTask extends AsyncTask<Void, Void, Bitmap> {
    private String url;
    public DownloadImageTask(String url){
        this.url = url;
    }

    protected Bitmap doInBackground(Void...voids) {
        Bitmap bmp = null;
        Bitmap res = null;
        try {
            if(!url.equals("")){
                InputStream in = new java.net.URL(url).openStream();
                bmp = BitmapFactory.decodeStream(in);
                res = Bitmap.createScaledBitmap(bmp, 200, 200, false);
            }
        } catch (Exception e) {
            Log.e("Error", e.getMessage() + "URL: "+ url);
            e.printStackTrace();
        }
        return res;
    }
    protected void onPostExecute(Bitmap result) {
        super.onPostExecute(result);
    }
}
