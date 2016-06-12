package com.asb.goldtrap;

import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.asb.goldtrap.iap.util.IabBroadcastReceiver;
import com.asb.goldtrap.iap.util.IabHelper;
import com.asb.goldtrap.iap.util.IabResult;
import com.asb.goldtrap.iap.util.Inventory;
import com.asb.goldtrap.iap.util.Key;
import com.asb.goldtrap.iap.util.Purchase;
import com.asb.goldtrap.models.buyables.BuyableType;
import com.asb.goldtrap.models.iap.IAPCreditor;
import com.asb.goldtrap.models.iap.factory.IAPCreditorFactory;

import org.json.JSONObject;

/**
 * Abstract Purchase Activity.
 * Created by arjun on 12/06/16.
 */
public abstract class AbstractPurchaseActivity extends AppCompatActivity
        implements IabBroadcastReceiver.IabBroadcastListener {
    private static final String TAG = AbstractPurchaseActivity.class.getSimpleName();
    protected IabHelper iabHelper;
    protected IabBroadcastReceiver mBroadcastReceiver;
    protected IAPCreditorFactory iapCreditorFactory;
    protected String[] consumables;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        iapCreditorFactory = new IAPCreditorFactory();
        consumables = getResources().getStringArray(R.array.iap_sku);
        setupIAP();
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
                    mBroadcastReceiver = new IabBroadcastReceiver(AbstractPurchaseActivity.this);
                    IntentFilter broadcastFilter = new IntentFilter(IabBroadcastReceiver.ACTION);
                    registerReceiver(mBroadcastReceiver, broadcastFilter);
                    loadMyInventory();
                }
            }
        });
    }

    protected void handleInAppPurchase(String sku, int requestCode, String developerPayload,
                                       final JSONObject item) {
        try {
            if (null != iabHelper) iabHelper.flagEndAsync();
            iabHelper.launchPurchaseFlow(this, sku, requestCode,
                    new IabHelper.OnIabPurchaseFinishedListener() {
                        @Override
                        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
                            if (result.isSuccess()) {
                                Log.i(TAG, "Purchasing " + purchase.getSku() +
                                        " is Successful, let's consume it now");
                                consumeIAP(purchase, item);
                            }
                        }
                    }, developerPayload);
        } catch (IabHelper.IabAsyncInProgressException e) {
            Log.e(TAG, "Purchase Exception", e);
        }
    }

    protected void handleInAppPurchase(BuyableType buyableType) {
        this.handleInAppPurchase(buyableType.getSku(), buyableType.getRequestCode(),
                buyableType.getDeveloperPayload(), new JSONObject());
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
                                consumeIAP(inv.getPurchase(consumable), new JSONObject());
                            }
                        }
                    }
                }
            });
        } catch (IabHelper.IabAsyncInProgressException e) {
            Log.e(TAG, "Exception in querying my inventory", e);
        }
    }

    protected void consumeIAP(Purchase consumable, final JSONObject item) {
        try {
            iabHelper.consumeAsync(consumable, new IabHelper.OnConsumeFinishedListener() {
                @Override
                public void onConsumeFinished(Purchase purchase, IabResult result) {
                    if (result.isSuccess()) {
                        Log.i(TAG, "Consumption of " + purchase.getSku() +
                                " is Successful, let's update the db.");
                        IAPCreditor iapCreditor = iapCreditorFactory
                                .getIAPCreditor(purchase.getSku(), getApplicationContext());
                        iapCreditor.creditItems(item);
                        onConsumptionComplete();
                    }
                }
            });
        } catch (IabHelper.IabAsyncInProgressException e) {
            Log.e(TAG, "Exception in consuming purchase. Lulz!", e);
        }
    }

    protected abstract void onConsumptionComplete();
}
