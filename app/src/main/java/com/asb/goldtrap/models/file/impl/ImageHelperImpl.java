package com.asb.goldtrap.models.file.impl;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.content.FileProvider;
import android.util.Log;

import com.asb.goldtrap.models.file.ImageHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by arjun on 29/11/15.
 */
public class ImageHelperImpl implements ImageHelper {

    private static final String TAG = ImageHelperImpl.class.getSimpleName();

    @Override
    public Uri writeFileToDisk(Bitmap bitmap, Context context) {
        File file = null;
        try {
            File dir = new File(context.getCacheDir(), "images");
            if (!dir.mkdir()) {
                Log.w(TAG, "Directory not created");
            }
            file = new File(dir, GOLD_TRAP_JPG);
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 95, fos);
        } catch (IOException ioe) {
            Log.e(TAG, "IOE: ", ioe);
        }
        return Uri.fromFile(file);
    }

    @Override
    public Uri getUriForExternalShare(Context context) {
        File dir = new File(context.getCacheDir(), "images");
        File file = new File(dir, GOLD_TRAP_JPG);
        return FileProvider.getUriForFile(context, "com.asb.goldtrap.fileprovider", file);
    }
}
