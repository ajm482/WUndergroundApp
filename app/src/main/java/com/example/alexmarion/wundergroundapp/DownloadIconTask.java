package com.example.alexmarion.wundergroundapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;

/**
 * Code used from Kyle Clegg on Stack Overflow
 * http://stackoverflow.com/questions/5776851/load-image-from-url
 * Edited to fit application
 */
public class DownloadIconTask extends AsyncTask<String, Void, Bitmap> {
    ImageView bitmapImage;

    public DownloadIconTask(ImageView bitmapImage) {
        this.bitmapImage = bitmapImage;
    }

    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap icon = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            icon = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return icon;
    }

    protected void onPostExecute(Bitmap result) {
        bitmapImage.setImageBitmap(result);
    }
}
