package com.asb.goldtrap;

import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.asb.goldtrap.fragments.shoporama.CheckoutFragment;
import com.asb.goldtrap.fragments.shoporama.ShopORamaFragment;
import com.asb.goldtrap.iap.util.IabBroadcastReceiver;
import com.asb.goldtrap.iap.util.IabHelper;
import com.asb.goldtrap.iap.util.IabResult;
import com.asb.goldtrap.iap.util.Inventory;
import com.asb.goldtrap.iap.util.Key;
import com.asb.goldtrap.iap.util.Purchase;
import com.asb.goldtrap.models.buyables.BuyableType;
import com.asb.goldtrap.models.eo.Buyable;
import com.asb.goldtrap.models.iap.IAPCreditor;
import com.asb.goldtrap.models.iap.factory.IAPCreditorFactory;

public class ShopORamaActivity extends AppCompatActivity
        implements IabBroadcastReceiver.IabBroadcastListener,
        ShopORamaFragment.OnFragmentInteractionListener,
        CheckoutFragment.OnFragmentInteractionListener {

    public static final String BOOSTER_TYPE = "BOOSTER_TYPE";
    private static final String TAG = ShopORamaActivity.class.getSimpleName();
    private IabHelper iabHelper;
    private IabBroadcastReceiver mBroadcastReceiver;
    private IAPCreditorFactory iapCreditorFactory;
    private String[] consumables;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_orama);
        iapCreditorFactory = new IAPCreditorFactory();
        consumables = getResources().getStringArray(R.array.iap_sku);
        setupIAP();
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
                    loadMyInventory();
                }
            }
        });
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
                .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
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

    }

    @Override
    public void buyItem(BuyableType buyableType) {
        handleInAppPurchase(buyableType);
    }

    private void handleInAppPurchase(BuyableType buyableType) {
        try {
            if (null != iabHelper) iabHelper.flagEndAsync();
            iabHelper.launchPurchaseFlow(this, buyableType.getSku(), buyableType.getRequestCode(),
                    new IabHelper.OnIabPurchaseFinishedListener() {
                        @Override
                        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
                            if (result.isSuccess()) {
                                Log.i(TAG, "Purchasing " + purchase.getSku() +
                                        " is Successful, let's consume it now");
                                consumeIAP(purchase);
                            }
                        }
                    }, buyableType.getDeveloperPayload());
        } catch (IabHelper.IabAsyncInProgressException e) {
            Log.e(TAG, "Purchase Exception", e);
        }
    }

    public void loadMyInventory() {
        try {
            iabHelper.queryInventoryAsync(new IabHelper.QueryInventoryFinishedListener() {
                @Override
                public void onQueryInventoryFinished(IabResult result, Inventory inv) {
                    if (result.isSuccess()) {
                        for (String consumable : consumables) {
                            if (inv.hasPurchase(consumable)) {
                                Log.i(TAG, "Consumable " + consumable +
                                        " is with me. Let's consume it now.");
                                consumeIAP(inv.getPurchase(consumable));
                            }
                        }
                    }
                }
            });
        } catch (IabHelper.IabAsyncInProgressException e) {
            Log.e(TAG, "Exception in querying my inventory", e);
        }
    }

    private void consumeIAP(Purchase consumable) {
        try {
            iabHelper.consumeAsync(consumable, new IabHelper.OnConsumeFinishedListener() {
                @Override
                public void onConsumeFinished(Purchase purchase, IabResult result) {
                    if (result.isSuccess()) {
                        Log.i(TAG, "Consumption of " + purchase.getSku() +
                                " is Successful, let's update the db.");
                        IAPCreditor iapCreditor = iapCreditorFactory
                                .getIAPCreditor(purchase.getSku(), getApplicationContext());
                        iapCreditor.creditItems();
                        showMessage(getString(R.string.transaction_successful));
                    }
                }
            });
        } catch (IabHelper.IabAsyncInProgressException e) {
            Log.e(TAG, "Exception in consuming purchase. Lulz!", e);
        }
    }

    private void showMessage(String msg) {
        ViewGroup coordinateLayout = (ViewGroup) findViewById(R.id.fragment_container);
        Snackbar.make(coordinateLayout, msg, Snackbar.LENGTH_SHORT).show();
    }
}
