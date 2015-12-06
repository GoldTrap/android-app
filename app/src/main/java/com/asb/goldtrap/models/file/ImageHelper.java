package com.asb.goldtrap.models.file;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;

/**
 * Created by arjun on 29/11/15.
 */
public interface ImageHelper {
    String GOLD_TRAP_JPG = "gold_trap.jpg";

    Uri writeFileToDisk(Bitmap bitmap, Context context);

    Uri getUriForExternalShare(Context context);
}
