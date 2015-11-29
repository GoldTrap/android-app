package com.asb.goldtrap.models.file.impl;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.asb.goldtrap.models.file.ImageWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * Created by arjun on 29/11/15.
 */
public class ImageWriterImpl implements ImageWriter {

    private static final String TAG = ImageWriterImpl.class.getSimpleName();
    public static final String GOLD_TRAP = "GOLD_TRAP";

    @Override
    public Uri writeFileToDisk(Bitmap bitmap) {
        Uri uri = null;
        if (isExternalStorageWritable()) {
            File dir = getAlbumStorageDir(GOLD_TRAP);
            File file = new File(dir, "gold_trap.jpg");
            try {
                FileOutputStream fos = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 95, fos);
            } catch (FileNotFoundException fileNotFoundException) {
                Log.e(TAG, "File not found: ", fileNotFoundException);
            }
            uri = Uri.fromFile(file);
        }
        return uri;
    }

    private File getAlbumStorageDir(String albumName) {
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), albumName);
        if (!file.mkdirs()) {
            Log.w(TAG, "Directory not created");
        }
        return file;
    }

    private boolean isExternalStorageWritable() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

}
