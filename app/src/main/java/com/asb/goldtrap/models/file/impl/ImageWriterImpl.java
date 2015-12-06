package com.asb.goldtrap.models.file.impl;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import com.asb.goldtrap.models.file.ImageWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by arjun on 29/11/15.
 */
public class ImageWriterImpl implements ImageWriter {

    private static final String TAG = ImageWriterImpl.class.getSimpleName();

    @Override
    public Uri writeFileToDisk(Bitmap bitmap, Context context) {
        File file = null;
        try {
            file = File.createTempFile("gold_trap.jpg", null, context.getCacheDir());
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 95, fos);
        } catch (IOException ioe) {
            Log.e(TAG, "IOE: ", ioe);
        }
        return Uri.fromFile(file);
    }
}
