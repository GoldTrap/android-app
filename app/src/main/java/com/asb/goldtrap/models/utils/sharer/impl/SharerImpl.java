package com.asb.goldtrap.models.utils.sharer.impl;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.support.v4.app.ShareCompat;

import com.asb.goldtrap.R;
import com.asb.goldtrap.models.file.ImageHelper;
import com.asb.goldtrap.models.file.impl.ImageHelperImpl;
import com.asb.goldtrap.models.utils.sharer.Sharer;

import java.util.List;

/**
 * Created by arjun on 06/12/15.
 */
public class SharerImpl implements Sharer {
    ImageHelper imageHelper;

    public SharerImpl() {
        this.imageHelper = new ImageHelperImpl();
    }

    @Override
    public void shareGameImage(Activity activity) {
        Uri contentUri = imageHelper.getUriForExternalShare(activity.getApplicationContext());
        Intent shareIntent = ShareCompat.IntentBuilder
                .from(activity)
                .setType("image/*")
                .setSubject(activity.getString(R.string.share_with_others))
                .setText(activity.getString(R.string.share_with_others))
                .setChooserTitle(R.string.share)
                .setChooserTitle(activity.getString(R.string.share))
                .setStream(contentUri)
                .createChooserIntent()
                .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        List<ResolveInfo> resInfos =
                activity.getApplication().getPackageManager().queryIntentActivities(shareIntent,
                        PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo info : resInfos) {
            activity.getApplication().grantUriPermission(info.activityInfo.packageName, contentUri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        activity.startActivity(shareIntent);
    }
}
