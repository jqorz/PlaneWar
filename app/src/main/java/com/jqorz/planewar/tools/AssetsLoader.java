package com.jqorz.planewar.tools;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author j1997
 * @since 2020/5/4
 */
public class AssetsLoader {
    public static String getFileFromAssets(Context context, String fileName) {

        if (TextUtils.isEmpty(fileName)) {
            return null;
        }

        try {
            StringBuilder sb = new StringBuilder();
            InputStreamReader in = new InputStreamReader(context.getResources().getAssets().open(fileName));
            BufferedReader br = new BufferedReader(in);
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Bitmap getBitmapFromAssets(Context context, String fileName) {

        if (TextUtils.isEmpty(fileName)) {
            return null;
        }

        try {
            return BitmapFactory.decodeStream(context.getResources().getAssets().open(fileName));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
