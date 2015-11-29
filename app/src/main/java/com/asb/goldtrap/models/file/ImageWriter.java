package com.asb.goldtrap.models.file;

import android.graphics.Bitmap;
import android.net.Uri;

/**
 * Created by arjun on 29/11/15.
 */
public interface ImageWriter {
    Uri writeFileToDisk(Bitmap bitmap);
}
