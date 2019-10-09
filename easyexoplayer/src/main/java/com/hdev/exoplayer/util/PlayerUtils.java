package com.hdev.exoplayer.util;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.Formatter;
import java.util.Locale;

public class PlayerUtils {

    /**
     * convert time to string
     *
     * @param timeMs
     * @return
     */
    public static String stringForTime(int timeMs) {
        StringBuilder stringBuilder = new StringBuilder();
        Formatter formatter = new Formatter(stringBuilder, Locale.getDefault());
        int totalSeconds = timeMs / 1000;

        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;

        stringBuilder.setLength(0);
        if (hours > 0) {
            return formatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return formatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }


    public static Bitmap getBitmap(Context context, String uriPath) {
        AssetManager assetManager = context.getAssets();
        Bitmap bitmap = null;
        try {
            InputStream inputStream = assetManager.open(uriPath);
            bitmap = BitmapFactory.decodeStream(inputStream);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static void debug(String message) {
        Log.d("DEBUG", message);
    }
}