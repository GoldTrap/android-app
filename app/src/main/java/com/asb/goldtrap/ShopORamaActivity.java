package com.asb.goldtrap;

import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.asb.goldtrap.fragments.shoporama.ShopORamaFragment;
import com.asb.goldtrap.iap.util.IabBroadcastReceiver;
import com.asb.goldtrap.iap.util.IabHelper;
import com.asb.goldtrap.iap.util.IabResult;
import com.asb.goldtrap.iap.util.Key;
import com.asb.goldtrap.models.eo.Buyable;

public class ShopORamaActivity extends AppCompatActivity
        implements IabBroadcastReceiver.IabBroadcastListener,
        ShopORamaFragment.OnFragmentInteractionListener {

    private static final String TAG = ShopORamaActivity.class.getSimpleName();
    private IabHelper iabHelper;
    private IabBroadcastReceiver mBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_orama);
        setupIAP();
        if (null == getSupportFragmentManager().findFragmentByTag(ShopORamaFragment.TAG)) {
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                    .replace(R.id.fragment_container,
                            ShopORamaFragment.newInstance(),
                            ShopORamaFragment.TAG)
                    .commit();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mBroadcastReceiver != null) {
            unregisterReceiver(mBroadcastReceiver);
        }
        Log.d(TAG, "Destroying helper.");
        if (iabHelper != null) {
            iabHelper.disposeWhenFinished();
            iabHelper = null;
        }
    }

    private void setupIAP() {
        iabHelper = new IabHelper(this, Key.value());
        iabHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                Log.d(TAG, "Setup finished.");
                if (null != iabHelper && result.isSuccess()) {
                    mBroadcastReceiver = new IabBroadcastReceiver(ShopORamaActivity.this);
                    IntentFilter broadcastFilter = new IntentFilter(IabBroadcastReceiver.ACTION);
                    registerReceiver(mBroadcastReceiver, broadcastFilter);
                }
            }
        });
    }

    @Override
    public void onBuyableClicked(Buyable buyable) {
        switch (buyable.getType()) {

            case FLIP:
                break;
            case PLUS_ONE:
                break;
            case SKIP:
                break;
            case DONATE:
                break;
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            if (hasFocus) {
                int uiVisibilityCode =
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_FULLSCREEN;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    uiVisibilityCode |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
                }
                getWindow().getDecorView().setSystemUiVisibility(uiVisibilityCode);
            }
        }
    }

    @Override
    public void receivedBroadcast() {

    }
}
