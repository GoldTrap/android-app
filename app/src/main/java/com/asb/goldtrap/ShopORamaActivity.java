package com.asb.goldtrap;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.asb.goldtrap.fragments.shoporama.CheckoutFragment;
import com.asb.goldtrap.fragments.shoporama.ShopORamaFragment;
import com.asb.goldtrap.models.buyables.BuyableType;
import com.asb.goldtrap.models.eo.Buyable;
import com.google.android.gms.ads.MobileAds;

public class ShopORamaActivity extends AbstractPurchaseActivity
        implements ShopORamaFragment.OnFragmentInteractionListener,
        CheckoutFragment.OnFragmentInteractionListener {

    public static final String BOOSTER_TYPE = "BOOSTER_TYPE";
    private static final String TAG = ShopORamaActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_orama);
        MobileAds.initialize(getApplicationContext(), getString(R.string.application_id));
        if (null != getIntent().getExtras()) {
            String boosterType = getIntent().getExtras().getString(BOOSTER_TYPE);
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                    .replace(R.id.fragment_container,
                            CheckoutFragment.newInstance(boosterType),
                            CheckoutFragment.TAG)
                    .commit();
        }
        else if (null == getSupportFragmentManager().findFragmentByTag(ShopORamaFragment.TAG)
                && null == getSupportFragmentManager().findFragmentByTag(CheckoutFragment.TAG)) {
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                    .replace(R.id.fragment_container,
                            ShopORamaFragment.newInstance(),
                            ShopORamaFragment.TAG)
                    .commit();
        }
    }

    @Override
    public void onBuyableClicked(Buyable buyable) {
        switch (buyable.getType()) {

            case FLIP:
            case PLUS_ONE:
            case SKIP:
                showCheckoutFragment(buyable.getType());
                break;
            case DONATE:
                handleInAppPurchase(buyable.getType());
                break;
        }
    }

    private void showCheckoutFragment(BuyableType buyableType) {
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left,
                        R.anim.slide_in_left, R.anim.slide_out_right)
                .replace(R.id.fragment_container,
                        CheckoutFragment.newInstance(buyableType.name()),
                        CheckoutFragment.TAG)
                .addToBackStack(CheckoutFragment.TAG)
                .commit();
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
        Log.i(TAG, "Received a broadcast from IAP!");
        loadMyInventory();
    }

    @Override
    public void buyItem(BuyableType buyableType) {
        handleInAppPurchase(buyableType);
    }

    protected void onConsumptionComplete() {
        ViewGroup coordinateLayout = (ViewGroup) findViewById(R.id.fragment_container);
        Snackbar.make(coordinateLayout, getString(R.string.transaction_successful),
                Snackbar.LENGTH_SHORT).show();
    }
}
